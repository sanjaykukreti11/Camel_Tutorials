package RabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Rabbit {

    public static void main(String[] args) throws  Exception {

        CamelContext context = new DefaultCamelContext();

        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = factory.newConnection("amqp://guest:guest@localhost:5672/");
        Channel channel = connection.createChannel();
        channel.queueDeclare("camelQueue", true, false,false,null);

        try{

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:Rabbit").split(xpath("//order[@product='soaps']/items")).to("rabbitmq://localhost:5672/username=guest&password=guest");
                }
            });

            context.start();

            ProducerTemplate orderProducerTemplate = context.createProducerTemplate();
            InputStream myText = Files.newInputStream(Paths.get("order.xml"));

            orderProducerTemplate.sendBody("direct:Rabbit" , myText );



        }finally {
            context.stop();
        }

    }

}
