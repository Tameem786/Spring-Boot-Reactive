package com.zaman.ReactiveProgrammingJava.configuration;

import com.zaman.ReactiveProgrammingJava.domain.Quote;
import com.zaman.ReactiveProgrammingJava.repository.QuoteMongoReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Supplier;

@Component
public class QuoteDataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(QuoteDataLoader.class);
    private final QuoteMongoReactiveRepository quoteMongoReactiveRepository;

    public QuoteDataLoader(QuoteMongoReactiveRepository quoteMongoReactiveRepository) {
        this.quoteMongoReactiveRepository = quoteMongoReactiveRepository;
    }

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        if(quoteMongoReactiveRepository.count().block() == 0L) {
            var idSupplier = getIdSequenceSupplier();
            var bufferReader = new BufferedReader(
                    new InputStreamReader(
                            Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("pg2000.txt"))
                    )
            );
            Flux.fromStream(
                    bufferReader.lines().filter(l -> !l.trim().isEmpty())
                            .map(l -> quoteMongoReactiveRepository.save(
                                    new Quote(idSupplier.get(), "El Quote", l)
                            ))
            ).subscribe(m -> log.info("New quote loaded: {}", m.block()));
            log.info("Repository contains now {} entries.", quoteMongoReactiveRepository.count().block());
        }
    }

    private Supplier<String> getIdSequenceSupplier() {
        return new Supplier<String>() {
            Long l = 0L;

            @Override
            public String get() {
                return String.format("%05d", l++);
            }
        };
    }
}
