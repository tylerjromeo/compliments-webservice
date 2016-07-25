package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.domain.Compliment;
import org.romeo.compliments.persistence.ComplimentRepository;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.domain.User;
import org.romeo.compliments.web.domain.ComplimentRequest;
import org.romeo.compliments.web.domain.PaginatedList;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tylerromeo
 * Date: 6/17/16
 * Time: 8:48 PM
 */

@RestController
public class ComplimentController {

    @Autowired
    ComplimentRepository complimentRepository;

    @Autowired
    UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET, path = "/compliments", produces = "application/json")
    @ApiOperation(value = "Get Compliments", nickname = "Get Compliments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "to", value = "Id for the user the compliments were sent to", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "from", value = "Id for the user the compliments were sent by", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Page number of results to return", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Total number of results to return. Maximum 100", defaultValue = "10", required = false, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public PaginatedList<Compliment> getAll(
            @RequestParam(required = false) Long to,
            @RequestParam(required = false) Long from,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<Compliment> compliments = new ArrayList<>();
        String idParam;
        Page<org.romeo.compliments.domain.Compliment> complimentPage;
        if(to != null && from != null) {
            complimentPage = complimentRepository.findByToIdAndFromId(to, from, new PageRequest(page, size));
            idParam = String.format("&to=%d&from=%d", to, from);
        } else if (to != null) {
            complimentPage = complimentRepository.findByToId(to, new PageRequest(page, size));
            idParam = String.format("&to=%d", to);
        } else if (from != null) {
            complimentPage = complimentRepository.findByFromId(from, new PageRequest(page, size));
            idParam = String.format("&from=%d", from);
        } else {
            complimentPage = complimentRepository.findAll(new PageRequest(page, size));
            idParam = "";
        }

        if (complimentPage != null) {

            String next = "";
            if (complimentPage.hasNext()) {
                next = String.format("/compliments?page=%d&size=%d%s", page + 1, size, idParam);
            }

            for(org.romeo.compliments.domain.Compliment c: complimentPage.getContent()) {
                compliments.add(c);
            }

            return new PaginatedList<>(complimentPage.getTotalElements(), complimentPage.getNumber(), complimentPage.getNumberOfElements(), next, compliments);
        } else {
            //TODO: return error
            return null;
        }
    }

    @RequestMapping(method = RequestMethod.POST, path = "/compliments", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Add Compliment", nickname = "Add Compliment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "compliment", value = "compliment to send with to user id filled out", required = true, dataType = "Compliment", paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Compliment add(@RequestBody @Validated ComplimentRequest complimentRequest) {
        User user = userRepository.findByEmail(complimentRequest.getToEmail());
        if(user == null) {
            //TODO: users added this way should be marked so when they do log in we can ask for their name/picture
            user = userRepository.save(new User(null, complimentRequest.getToEmail(), null));
        }
        Compliment compliment = new Compliment(new User(complimentRequest.getFromId()), new User(user.getId()), complimentRequest.getContents());

        return complimentRepository.save(compliment);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/compliments/{complimentId}/reactions", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Add Reaction to Compliment", nickname = "Add Reaction to Compliment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reaction", value = "reaction to add to the compliment.", required = true, dataType = "Compliment", paramType = "body")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public Compliment.Reaction addReaction(@PathVariable long complimentId, @RequestBody @Validated Compliment.Reaction reaction) throws ResourceNotFoundException {
        Compliment compliment = complimentRepository.findOne(complimentId);
        if(compliment == null) {
            throw new ResourceNotFoundException();
        }

        if(compliment.getReactions() == null) {
            compliment.setReactions(new ArrayList<>());
        }
        Compliment.Reaction existingReaction = null;
        long maxId = 0L;
        for(Compliment.Reaction r : compliment.getReactions()) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
            if(r.getReaction().equals(reaction.getReaction())) {
                existingReaction = r;
            }
        }

        if(existingReaction != null) {
            return existingReaction;
        } else {
            reaction.setId(maxId + 1);
            compliment.getReactions().add(reaction);
            complimentRepository.save(compliment);
            return reaction;
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/compliments/{complimentId}/reactions/{reactionId}", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Remove Reaction", nickname = "Remove Reaction")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> deleteReaction(@PathVariable long complimentId, @PathVariable long reactionId) throws ResourceNotFoundException {
        Compliment compliment = complimentRepository.findOne(complimentId);
        if(compliment == null) {
            throw new ResourceNotFoundException();
        }
        if(compliment.getReactions() == null) {
            throw new ResourceNotFoundException();
        }

        Compliment.Reaction removedCompliment = null;
        for(Compliment.Reaction r : compliment.getReactions()) {
            if(r.getId().equals(reactionId)) {
                removedCompliment = r;
                break;
            }
        }

        if(removedCompliment == null) {
            throw new ResourceNotFoundException();
        }

        compliment.getReactions().remove(removedCompliment);
        complimentRepository.save(compliment);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
