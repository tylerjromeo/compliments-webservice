package org.romeo.compliments.web;

import io.swagger.annotations.*;
import org.romeo.compliments.web.domain.Compliment;
import org.romeo.compliments.web.domain.PaginatedList;
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
    public PaginatedList<Compliment> getCompliments(
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String from,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (to == null) to = "1";
        if (from == null) from = "2";
        List<Compliment> compliments = new ArrayList<Compliment>(2);
        compliments.add(new Compliment.Builder().id("1").toId(to).fromId(from).contents("You are great!").sendDate(new Date()).build());
        compliments.add(new Compliment.Builder().id("2").toId(to).fromId(from).contents("You are really great!").sendDate(new Date()).build());

        return new PaginatedList<>(2l, page, size, "http://localhost:8080/compliments?to=1&size=10&page=2", compliments);
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
    public Compliment addCompliment(@RequestBody @Validated Compliment compliment) {
        return compliment;
    }
}
