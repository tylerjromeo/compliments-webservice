package org.romeo.compliments.users;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: tylerromeo
 * Date: 6/15/16
 * Time: 8:34 PM
 */

@RestController
public class Controller {

    @RequestMapping("/users/{id}")
    public Model getById(@PathVariable("id") String id) {
        return new Model(id, "Tyler@example.com", "http://placekitten.com/400/200");
    }
}
