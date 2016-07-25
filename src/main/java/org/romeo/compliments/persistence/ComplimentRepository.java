package org.romeo.compliments.persistence;

import org.romeo.compliments.domain.Compliment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 10:04 AM
 */
public interface ComplimentRepository extends PagingAndSortingRepository<Compliment, Long> {

    Compliment findById(long id);

    Page<Compliment> findByToId(long toId, Pageable pageable);

    Page<Compliment> findByFromId(long fromId, Pageable pageable);

    Page<Compliment> findByToIdAndFromId(long toId, long fromId, Pageable pageable);
}
