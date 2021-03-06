package org.romeo.compliments.persistence;

import org.romeo.compliments.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 10:04 AM
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findByEmail(String email);
}
