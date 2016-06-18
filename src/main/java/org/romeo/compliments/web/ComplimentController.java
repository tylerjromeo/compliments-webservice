package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.domain.Compliment;
import org.romeo.compliments.domain.PaginatedList;
import org.romeo.compliments.domain.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping(method = RequestMethod.GET, path = "/compliments", produces = "application/json")
    @ApiOperation(value = "Get Compliments", nickname = "Get Compliments")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "to", value = "Id for the user the compliments were sent to", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "from", value = "Id for the user the compliments were sent by", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "offset", value = "Number of results in the set to skip", defaultValue = "0", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "numResults", value = "Total number of results to return. Maximum 100", defaultValue = "10", required = false, dataType = "int", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public PaginatedList<Compliment> getCompliments(
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String from,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int numResults) {

        if(to == null) to = "1";
        if(from == null) from = "2";
        List<Compliment> compliments = new ArrayList<Compliment>(2);
        compliments.add(new Compliment.Builder().id("1").toId(to).fromId(from).contents("You are great!").sendDate(new Date()).build());
        compliments.add(new Compliment.Builder().id("2").toId(to).fromId(from).contents("You are really great!").sendDate(new Date()).build());

        return new PaginatedList<Compliment>(2, offset, numResults, "http://localhost:8080/compliments?to=1&numResults=10&offset=10", compliments);
    }
}