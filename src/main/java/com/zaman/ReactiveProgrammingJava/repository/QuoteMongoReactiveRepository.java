package com.zaman.ReactiveProgrammingJava.repository;

import com.zaman.ReactiveProgrammingJava.domain.Quote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface QuoteMongoReactiveRepository extends ReactiveCrudRepository<Quote, String> {
    Flux<Quote> findAllByIdNotNullOrderByIdAsc(final Pageable page);
}
