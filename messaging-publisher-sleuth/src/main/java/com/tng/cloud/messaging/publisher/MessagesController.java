package com.tng.cloud.messaging.publisher;

import io.cloudevents.CloudEvent;
import io.cloudevents.spring.messaging.CloudEventMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/messages")
@Valid
public class MessagesController {
    private final CloudEventMessageConverter cloudEventMessageConverter;

    private final Sinks.Many<Message<CloudEvent>> many;
    private final StreamBridge bridge;

    public MessagesController(CloudEventMessageConverter cloudEventMessageConverter, Sinks.Many<Message<CloudEvent>> many, StreamBridge bridge) {
        this.cloudEventMessageConverter = cloudEventMessageConverter;
        this.many = many;
        this.bridge = bridge;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/cloudevents")
    public void cloudEvent(@RequestParam(name = "topic") @NotBlank String topic, @NotNull @RequestBody CloudEvent cloudEvent) {
        log.info("Attempting to publish message to topic [{}]", topic);
        var messageMono = Mono.just(cloudEvent)
                .doOnNext(event -> log.debug("CloudEvent payload:\n{}", event))
                .map(this::convert)
                ;
//        doSendMessage(topic, message);
        many.tryEmitNext(convert(cloudEvent));
    }

    @SuppressWarnings({"unchecked"})
    private Message<CloudEvent> convert(CloudEvent cloudEvent) {
        return (Message<CloudEvent>) cloudEventMessageConverter.toMessage(cloudEvent, new MessageHeaders(new HashMap<>()));
    }

    private void doSendMessage(String topic, Mono<? extends Message<?>> payload) {
        Mono.defer(() -> payload)
                .doOnNext(msg -> log.info("About to publish message {} to topic {}", msg.getHeaders().getId(), topic))
                .map(message -> bridge.send(topic, message))
                .doOnSuccess(result -> log.info("Is the message sent? {}", result))
                .doOnError(t -> log.error("Failed to send to dynamic destination", t))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe()
        ;
    }
}
