package co.com.bancolombia.quarkus;

import org.apache.camel.builder.RouteBuilder;

import javax.inject.Inject;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class LegumeRoute extends RouteBuilder {

    private final Set<Legume> legumes = Collections.synchronizedSet(new LinkedHashSet<>());

    public LegumeRoute() {
        /* Let's add some initial legumes */
        this.legumes.add(new Legume("Carrot", "Root vegetable, usually orange"));
        this.legumes.add(new Legume("Zucchini", "Summer squash"));
    }

    @Override
    public void configure() throws Exception {
        from("direct:getLegumes")
                .setBody().constant(legumes);

        from("direct:addLegume")
                .choice()
                .when(simple("${body.getName()}").endsWith("1"))
                .bean("legumeBean","setHeaderComo(${exchange})")
                .log("${header.COMO}")
                .otherwise()
                .bean("legumeBean","setHeaderComo2(${exchange})")
                .log("${header.COMO2}")
                .end()
                .process().body(Legume.class, legumes::add)
                .setBody().constant(legumes);
    }
}
