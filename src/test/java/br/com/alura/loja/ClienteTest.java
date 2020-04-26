package br.com.alura.loja;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Assert;
import org.junit.Test;

public class ClienteTest {

	@Test
	public void testaQueAConexaoComOServidorFunciona() {
//		cliente http para acessar o servidor
		Client client = ClientBuilder.newClient();
		
//		queremos usar uma URI base,a URI do servidor, para fazer várias requisições.
		WebTarget target = client.target("http://www.mocky.io");
		
// 		requisição para uma URI específica
		String conteudo = target.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		
		System.out.println(conteudo);
			
//		Queremos agora ter certeza que o conteúdo contem a 'Rua Vergueiro 3185'
//		que ela contem o pedaço do XML que nos interessa. 
		Assert.assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}
	
}
