package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.web.domain.PaginatedList;
import org.romeo.compliments.web.domain.User;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public PaginatedList<User> getAll(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        //TODO: filter by email
        List<User> users = new ArrayList<>();
        Page<org.romeo.compliments.persistence.domain.User> dbUsersPage = userRepository.findAll(new PageRequest(page, size));
        for(org.romeo.compliments.persistence.domain.User dbUser : dbUsersPage) {
            users.add(User.fromDbUser(dbUser));
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
        User user = User.fromDbUser(userRepository.findOne(id));
        if(user == null) {
            throw new ResourceNotFoundException();
        }
        return user;
    }

}
