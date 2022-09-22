package co.com.bancolombia;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // produces messages to kafka
        /*from("timer:foo?period={{timer.period}}&delay={{timer.delay}}")
                .routeId("FromTimer2Kafka")
                .setBody().simple("Message #${exchangeProperty.CamelTimerCounter}")
                .to("kafka:{{kafka.topic.name}}")
                .log("Message sent correctly sent to the topic! : \"${body}\" ");*/

        // kafka consumer
        from("kafka:{{kafka.topic.request.membership}}")
                .routeId("FromKafka2Seda")
                .log("Received : \"${body}\"")
                .delay(500)
                .to("kafka:{{kafka.topic.confirmed.membership}}");
    }
}
