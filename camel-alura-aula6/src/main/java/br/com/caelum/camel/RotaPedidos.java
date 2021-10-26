package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.xml.sax.SAXParseException;

public class RotaPedidos {

	public static void main(String[] args) throws Exception {

		CamelContext context = new DefaultCamelContext();
		context.addRoutes(new RouteBuilder() { //cuidado, não é RoutesBuilder

			@Override
			public void configure() throws Exception {

//				errorHandler(
//						deadLetterChannel("file:erro").
//								maximumRedeliveries(3).//tente 3 vezes
//								redeliveryDelay(3000). //espera 3 segundo entre as tentativas
//								onRedelivery(new Processor() {
//							@Override
//							public void process(Exchange exchange) throws Exception {
//								int counter = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
//								int max = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
//								System.out.println("Redelivery - " + counter + "/" + max );
//							}
//						})
//				);

				onException(SAXParseException.class).
						handled(true).
						maximumRedeliveries(3).
						redeliveryDelay(4000).
						onRedelivery(new Processor() {

							@Override
							public void process(Exchange exchange) throws Exception {
								int counter = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_COUNTER);
								int max = (int) exchange.getIn().getHeader(Exchange.REDELIVERY_MAX_COUNTER);
								System.out.println("Redelivery - " + counter + "/" + max );;
							}
						});

				from("file:pedidos?delay=5s&noop=true").
						log("${file:name}"). //logando nome do arquivo
						routeId("rota-pedidos").
						delay(1000). //esperando 1 segundo
						to("validator:pedido.xsd");//nova validação
//						multicast().
//						to("direct:soap").
//						log("Chamando soap com ${body}").
//						to("direct:http");
//
//				from("direct:soap").
//						routeId("rota-soap").
//						to("xslt:pedido-para-soap.xslt").
//						log("Resultado do template: ${body}").
//						setHeader(Exchange.CONTENT_TYPE,constant("text/xml")).
//						to("http4://localhost:8080/webservices/financeiro");
//
//				from("direct:http").
//						routeId("rota-http").
//						setProperty("pedidoId", xpath("/pedido/id/text()")).
//						setProperty("email", xpath("/pedido/pagamento/email-titular/text()")).
//						split().
//						xpath("/pedido/itens/item").
//						filter().
//						xpath("/item/formato[text()='EBOOK']").
//						setHeader("CamelFileName", simple("${file:name}.json")).
//						setProperty("ebookId", xpath("/item/livro/codigo/text()")).
//						setProperty("ebookId", xpath("/item/livro/codigo/text()")).
//						setHeader(Exchange.HTTP_QUERY,
//								simple("clienteId=${property.email}&pedidoId=${property.pedidoId}&ebookId=${property.ebookId}")).
//						to("http4://localhost:8080/webservices/ebook/item");
			}
		});

		context.start();
		Thread.sleep(20000);
		context.stop();
	}
}
