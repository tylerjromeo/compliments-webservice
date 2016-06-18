package org.romeo.compliments.persistence;

import org.romeo.compliments.persistence.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 10:04 AM
 */
public interface UserRepository extends CrudRepository<User, Long>{
}
