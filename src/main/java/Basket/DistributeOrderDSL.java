package Basket;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DistributeOrderDSL {

    public static void main(String[] args) throws  Exception {

        CamelContext context = new DefaultCamelContext();
        try{

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:DistributeOrderDSL").split(xpath("//order[@product='soaps']/items")).to("stream:out");
                }
            });

            context.start();

            ProducerTemplate orderProducerTemplate = context.createProducerTemplate();
            InputStream myText = Files.newInputStream(Paths.get("order.xml"));

            orderProducerTemplate.sendBody("direct:DistributeOrderDSL" , myText );



        }finally {
            context.stop();
        }

    }

}
