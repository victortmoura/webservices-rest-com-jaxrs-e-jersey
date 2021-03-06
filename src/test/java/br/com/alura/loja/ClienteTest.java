package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class ClienteTest {
	
	private HttpServer server;
	private WebTarget target;
	private Client client;

	@Before
	public void startaServidor() {
		this.server = Servidor.inicializaServidor();
		ClientConfig config = new ClientConfig();
		config.register(new LoggingFilter());
//		cliente http para acessar o servidor
		this.client = ClientBuilder.newClient(config);
//		queremos usar uma URI base,a URI do servidor, para fazer v�rias requisi��es.
		target = client.target("http://localhost:8080");
	}
	
	@After
	public void mataServidor() {
		server.stop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
// 		requisi��o para uma URI espec�fica
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		
		System.out.println(conteudo);
			
//		Queremos agora ter certeza que o conte�do contem a 'Rua Vergueiro 3185'
//		que ela contem o peda�o do XML que nos interessa. 
//		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
		
//		mais bonito do que eu garantir somente a rua atrav�s desse contains, ser� 
//		eu deserializar esse XML de volta a um objeto do tipo Carrinho e ai garantir 
//		que esse carrinho � exatamente o que eu esperava.
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testaCriacaoDeUmNovoCarrinho() {
//		queremos usar uma URI base,a URI do servidor, para fazer v�rias requisi��es.
		target = client.target("http://localhost:8080");
		
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "iPhone", 999, 1));
		carrinho.setRua("Rua Alvarenga");
		carrinho.setCidade("S�o Paulo");
		String xml = carrinho.toXML();
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		
		Response response = target.path("/carrinhos").request().post(entity);
		Assert.assertEquals(201, response.getStatus());
		
		String location = response.getHeaderString("Location"); 
		String conteudo = client.target(location).request().get(String.class);
		Assert.assertTrue(conteudo.contains("iPhone"));
		
	}
	
}
