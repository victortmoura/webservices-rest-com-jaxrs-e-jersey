package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;

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
		
//		queremos usar uma URI base,a URI do servidor, para fazer v�rias requisi��es.
		WebTarget target = client.target("http://localhost:8080");
		
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
	
}
