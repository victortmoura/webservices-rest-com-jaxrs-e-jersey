package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sun.javafx.geom.AreaOp.CAGOp;
import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class ClienteTest {
	
	private HttpServer server;

	@Before
	public void startaServidor() {
		this.server = Servidor.inicializaServidor();
	}
	
	@After
	public void mataServidor() {
		server.stop();
	}
	
	@Test
	public void testaQueBuscarUmCarrinhoTrazOCarrinhoEsperado() {
//		cliente http para acessar o servidor
		Client client = ClientBuilder.newClient();
		
//		queremos usar uma URI base,a URI do servidor, para fazer várias requisições.
		WebTarget target = client.target("http://localhost:8080");
		
// 		requisição para uma URI específica
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		
		System.out.println(conteudo);
			
//		Queremos agora ter certeza que o conteúdo contem a 'Rua Vergueiro 3185'
//		que ela contem o pedaço do XML que nos interessa. 
//		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
		
//		mais bonito do que eu garantir somente a rua através desse contains, será 
//		eu deserializar esse XML de volta a um objeto do tipo Carrinho e ai garantir 
//		que esse carrinho é exatamente o que eu esperava.
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}
	
	@Test
	public void testaCriacaoDeUmNovoCarrinho() {
//		cliente http para acessar o servidor
		Client client = ClientBuilder.newClient();
//		queremos usar uma URI base,a URI do servidor, para fazer várias requisições.
		WebTarget target = client.target("http://localhost:8080");
		
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314L, "iPhone", 999, 1));
		carrinho.setRua("Rua Alvarenga");
		carrinho.setCidade("São Paulo");
		String xml = carrinho.toXML();
		
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);
		Response response = target.path("/carrinhos").request().post(entity);
		
		Assert.assertEquals("<status>sucesso</status>", response.readEntity(String.class));
		
		
	}
	
}
