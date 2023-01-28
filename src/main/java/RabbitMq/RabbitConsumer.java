package RabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {

    public static void main(String[] args) throws Exception {


        CamelContext context = new DefaultCamelContext();
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("amqp://guest:guest@localhost:5672/");
        Channel channel = connection.createChannel();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("rabbitmq://localhost:5672/username=guest&password=guest")
                        .to("stream:out");
            }
        });

        context.start();

    }
}
