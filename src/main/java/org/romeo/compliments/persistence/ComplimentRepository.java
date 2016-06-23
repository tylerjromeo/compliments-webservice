package org.romeo.compliments.persistence;

import org.romeo.compliments.persistence.domain.Compliment;
import org.romeo.compliments.persistence.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 10:04 AM
 */
public interface ComplimentRepository extends PagingAndSortingRepository<Compliment, Long> {

    Page<Compliment> findByToId(long toId, Pageable pageable);

    Page<Compliment> findByFromId(long fromId, Pageable pageable);

    Page<Compliment> findByToIdAndFromId(long toId, long fromId, Pageable pageable);
}
