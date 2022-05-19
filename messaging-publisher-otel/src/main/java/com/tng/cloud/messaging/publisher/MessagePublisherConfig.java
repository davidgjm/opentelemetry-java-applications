package com.tng.cloud.messaging.publisher;

import io.cloudevents.CloudEvent;
import io.cloudevents.spring.messaging.CloudEventMessageConverter;
import io.cloudevents.spring.webflux.CloudEventHttpMessageReader;
import io.cloudevents.spring.webflux.CloudEventHttpMessageWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.codec.CodecCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Configuration
public class MessagePublisherConfig {

    @Bean
    public Sinks.Many<Message<CloudEvent>> many() {
        return Sinks.many().unicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<Message<? extends CloudEvent>>> messagePublisher(Sinks.Many<Message<? extends CloudEvent>> many) {
        return () -> many.asFlux()
                .doOnNext(m -> log.info("Manually sending message {}", m))
                .doOnError(t -> log.error("Error encountered", t))
                .subscribeOn(Schedulers.boundedElastic())
                ;
    }


    @Bean
    public Consumer<Message<CloudEvent>> demoConsumer() {
        return message -> {
            log.info("New message received! ce-id={}", message.getHeaders().get("ce-id"));
            message.getHeaders().forEach((k,v) -> log.debug("Message header {}={}", k,v));
        };
    }

    @Configuration
    static class CloudEventCodec implements CodecCustomizer {
        @Override
        public void customize(CodecConfigurer configurer) {
            configurer.customCodecs().register(new CloudEventHttpMessageReader());
            configurer.customCodecs().register(new CloudEventHttpMessageWriter());
        }

    }
    @Configuration
    public static class CloudEventMessageConverterConfiguration {
        @Bean
        public CloudEventMessageConverter cloudEventMessageConverter() {
            return new CloudEventMessageConverter();
        }
    }

}
