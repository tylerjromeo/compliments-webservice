package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.web.domain.PaginatedList;
import org.romeo.compliments.domain.User;
import org.romeo.compliments.web.domain.UserRequest;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tylerromeo
 * Date: 6/15/16
 * Time: 8:34 PM
 */

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    public UserController(){

    }

    @RequestMapping(method = RequestMethod.GET, path = "/users", produces = "application/json")
    @ApiOperation(value = "get all Users", nickname = "get All Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "User email to filter on", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Page number of results to return", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Total number of results to return. Maximum 100", defaultValue = "10", required = false, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public PaginatedList<User> getAll(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        //TODO: filter by email
        List<User> users = new ArrayList<>();
        Page<org.romeo.compliments.domain.User> dbUsersPage = userRepository.findAll(new PageRequest(page, size));
        for(org.romeo.compliments.domain.User dbUser : dbUsersPage) {
            users.add(dbUser);
        }
        String next = "";
        if(dbUsersPage.hasNext()) {
            next = String.format("/users?page=%d&size=%d", page + 1, size);
        }
        return new PaginatedList<>(dbUsersPage.getTotalElements(), dbUsersPage.getNumber(), dbUsersPage.getNumberOfElements(), next, users);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}", produces = "application/json")
    @ApiOperation(value = "get all Users", nickname = "get All Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User's id", required = true, dataType = "long", paramType = "path")
    })
    public User getById(@PathVariable("id") long id) throws ResourceNotFoundException {
        User user = userRepository.findOne(id);
        if(user == null) {
            throw new ResourceNotFoundException();
        }
        return user;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/users", produces = "application/json")
    @ApiOperation(value = "add User", nickname = "add User", notes = "this will create a new user. If the email address is already in the db, the user details will be populated. Otherwise a new user object will be created with the given data. This allows compliments that were sent before a user registered will still go to that user.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public User add(@RequestBody @Validated UserRequest user) {
        org.romeo.compliments.domain.User dbUser = userRepository.findByEmail(user.getEmail());
        if(dbUser == null) {
            dbUser = userRepository.save(new org.romeo.compliments.domain.User(user.getName(), user.getEmail(), user.getImageUrl()));
        } else {
            //TODO: User object will need a flag for if it has been "set up" check that here and throw a 400 if it's true
            dbUser.setName(user.getName());
            dbUser.setImageUrl(user.getImageUrl());
            dbUser = userRepository.save(dbUser);
        }
        return dbUser;
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/users/{id}", produces = "application/json")
    @ApiOperation(value = "edit User", nickname = "edit User", notes = "takes in a partial user object and replaces the provided fields. Omitted fields will not be changes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public User edit(@PathVariable(value = "id") Long id, @RequestBody UserRequest userChanges) throws ResourceNotFoundException {
        org.romeo.compliments.domain.User dbUser = userRepository.findOne(id);
        if(dbUser == null) {
            throw new ResourceNotFoundException();
        }
        if(userChanges.getEmail() != null) {
            dbUser.setEmail(userChanges.getEmail());
        }
        if(userChanges.getName() != null) {
            dbUser.setName(userChanges.getName());
        }
        if(userChanges.getImageUrl() != null) {
            dbUser.setImageUrl(userChanges.getImageUrl());
        }
        org.romeo.compliments.domain.User updatedDbUser = userRepository.save(dbUser);
        return updatedDbUser;
    }

}
