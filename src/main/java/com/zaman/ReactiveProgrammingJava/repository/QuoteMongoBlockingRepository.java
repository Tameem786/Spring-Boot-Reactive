package com.zaman.ReactiveProgrammingJava.repository;

import com.zaman.ReactiveProgrammingJava.domain.Quote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuoteMongoBlockingRepository extends CrudRepository<Quote, String> {
    List<Quote> findAllByIdNotNullOrderByIdAsc(final Pageable page);
}
