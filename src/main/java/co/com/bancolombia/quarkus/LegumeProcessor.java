package co.com.bancolombia.quarkus;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("legumeBean")
@ApplicationScoped
@RegisterForReflection
public class LegumeProcessor{

    public void setHeaderComo(Exchange exchange){
        Legume legume = exchange.getIn().getBody(Legume.class);
        exchange.getIn().setHeader("COMO", legume.getName());
    }

    public void setHeaderComo2(Exchange exchange){
        Legume legume = exchange.getIn().getBody(Legume.class);
        exchange.getIn().setHeader("COMO2", legume.getName());
    }
}
