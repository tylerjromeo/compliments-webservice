package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.domain.PaginatedList;
import org.romeo.compliments.domain.User;
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

    @RequestMapping(method = RequestMethod.GET, path = "/users", produces = "application/json")
    @ApiOperation(value = "get all Users", nickname = "get All Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "email", value = "User email to filter on", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "Number of results in the set to skip", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "numResults", value = "Total number of results to return. Maximum 100", defaultValue = "10", required = false, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public PaginatedList<User> getAll(
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int numResults) {
        String email1 = (email == null) ? "Tyler@example.com" : email;
        String email2 = (email == null) ? "Kevin@example.com" : email;
        List<User> users = new ArrayList<User>(2);
        users.add(new User("0", email1, "Tyler Romeo", "http://placekitten.com/400/200", 0, 0));
        users.add(new User("1", email2, "Kevin Welcher", "http://placekitten.com/300/200", 0, 0));
        return new PaginatedList<User>(2, offset, numResults, "http://localhost:8080/users?offset=10&numResults=10", users);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}", produces = "application/json")
    @ApiOperation(value = "get all Users", nickname = "get All Users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "User's id", required = true, dataType = "string", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public User getById(@PathVariable("id") String id) {
        return new User(id, "Tyler@example.com", "Tyler Romeo", "http://placekitten.com/400/200", 0, 0);
    }
}
