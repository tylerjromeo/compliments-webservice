package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.persistence.ComplimentRepository;
import org.romeo.compliments.web.domain.Compliment;
import org.romeo.compliments.web.domain.PaginatedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
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
        //TODO: return a 400 error if both to and from are supplied
        String idParam = "";
        Page<org.romeo.compliments.persistence.domain.Compliment> complimentPage;
        if (to != null) {
            complimentPage = complimentRepository.findByToId(to, new PageRequest(page, size));
            idParam = String.format("&to=%d", to);
        } else if (from != null) {
            complimentPage = complimentRepository.findByFromId(from, new PageRequest(page, size));
            idParam = String.format("&from=%d", from);
        } else {
            //TODO: return a 400 error if neither to or from are supplied
            //one day we might just return all compliments if none are supplied
            return null;
        }

        if (complimentPage != null) {

            String next = "";
            if (complimentPage.hasNext()) {
                next = String.format("/compliments?page=%d&size=%d%s", page + 1, size, idParam);
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
    public Compliment add(@RequestBody @Validated Compliment compliment) {
        return compliment;
    }
}
