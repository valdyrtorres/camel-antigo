package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() { //cuidado, não é RoutesBuilder

			@Override
			public void configure() throws Exception {
				from("file:pedidos?delay=5s&noop=true").
						routeId("rota-pedidos").
						log("Chamando soap com ${body}").
						to("direct:soap");

				from("direct:soap").
						routeId("rota-soap").
						to("xslt:pedido-para-soap.xslt").
						log("Resultado do template: ${body}").
						end();

			}
		});

		context.start();
		Thread.sleep(20000);
		context.stop();
	}
}
