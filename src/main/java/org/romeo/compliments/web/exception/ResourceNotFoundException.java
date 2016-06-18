package org.romeo.compliments.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 11:12 AM
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {
}
