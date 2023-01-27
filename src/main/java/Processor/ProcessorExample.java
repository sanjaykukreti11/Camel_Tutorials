package Processor;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class ProcessorExample {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                           String message = exchange.getIn().getBody(String.class);
                           message+= "--- Processor";
                           exchange.getOut().setBody(message);
                    }
                }).to("seda:end");
            }
        });
        context.start();
        ProducerTemplate producer = context.createProducerTemplate();
        producer.sendBody("direct:start", "Hey, How are you.");

        ConsumerTemplate consumer = context.createConsumerTemplate();
        String message = consumer.receiveBody("seda:end", String.class);

        System.out.println(message);

    }

}