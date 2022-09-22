package co.com.bancolombia.quarkus;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.reactive.streams.api.CamelReactiveStreamsService;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Camel route definitions.
 */
@ApplicationScoped
public class CamelPub extends RouteBuilder {
    @Inject
    CamelReactiveStreamsService streamsService;

    @Outgoing("requestedMemberships")
    public Publisher<String> requestedMembership() {
        return streamsService.from("seda:requestedMembership",String.class);
    }

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/membership")
                .post()
                .type(Account.class)
                .to("seda:requestMembership");

        from("seda:requestMembership")
                .marshal().json()
                .log("Received Pub 1: ${body}")
                .to("seda:requestedMembership")
                .unmarshal().json(JsonLibrary.Jackson,Account.class)
                .end();
    }
}