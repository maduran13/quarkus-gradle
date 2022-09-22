package co.com.bancolombia.quarkus;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Camel route definitions.
 */
public class Routes extends RouteBuilder {
    private final Set<Account> affiliateAccounts = Collections.synchronizedSet(new LinkedHashSet<>());

    public Routes(){
        this.affiliateAccounts.add(new Account("358","Ahorros","Cuenta Juan"));
    }
    @Override
    public void configure() throws Exception {

        restConfiguration().bindingMode(RestBindingMode.json);

        rest("/membership")
                .get()
                .to("direct:isAffiliate")

                .post()
                .type(Account.class)
                .to("direct:requestMembership");
                //.to("direct:postAccount");


        from("direct:isAffiliate")
                .setBody().constant(affiliateAccounts);

        from("direct:requestMembership")
                .marshal().json()
                .to("kafka:{{kafka.topic.request.membership}}")
                .unmarshal().json();
        from("kafka:{{kafka.topic.confirmed.membership}}")
        //from("direct:postAccount")
                .unmarshal().json(JsonLibrary.Jackson, Account.class)
                .log("Received : ${body}")
                .process().body(Account.class, affiliateAccounts::add)
                .setBody().constant(affiliateAccounts);
    }
}