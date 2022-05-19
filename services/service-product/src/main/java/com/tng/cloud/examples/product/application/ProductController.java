package com.tng.cloud.examples.product.application;

import com.tng.cloud.examples.product.application.dto.ProductCreationRequest;
import com.tng.cloud.examples.product.application.service.ProductApplicationService;
import com.tng.cloud.examples.product.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

@Valid
@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductApplicationService applicationService;

    public ProductController(ProductApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public Mono<Void> create(@RequestBody ProductCreationRequest requestMono) {
        Mono.defer(() -> Mono.just(requestMono))
                .doOnNext(request -> log.info("Creating new product with data {}", request))
                .map(applicationService::createProduct)
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe()
        ;

        return Mono.empty();
    }

    @GetMapping
    public Flux<ProductDto> findAll() {
        log.info("Finding all products...");
        return Flux.fromIterable(applicationService.findAll());
    }
}
