package co.com.bancolombia.reactive;

import co.com.bancolombia.quarkus.Account;
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
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Camel route definitions.
 */
@ApplicationScoped
public class CamelSub extends RouteBuilder {
    @Inject
    CamelReactiveStreamsService streamsService;

    private final Set<Account> affiliateAccounts = Collections.synchronizedSet(new LinkedHashSet<>());

    @Incoming("confirmedMemberships")
    public Subscriber<Account> requestedMembership() {
        return streamsService.subscriber("seda:confirmedMembership",Account.class);
    }

    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/membership")
                .get()
                .to("direct:isAffiliate");

        from("seda:confirmedMembership")
                .log("Received Sub: ${body}")
                .unmarshal().json(JsonLibrary.Jackson,Account.class)
                .process().body(Account.class, affiliateAccounts::add)
                .setBody().constant(affiliateAccounts);
        from("direct:isAffiliate")
                .setBody().constant(affiliateAccounts);
    }
}