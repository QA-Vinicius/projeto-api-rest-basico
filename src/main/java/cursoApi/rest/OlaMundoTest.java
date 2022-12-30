package cursoApi.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class OlaMundoTest {

	@Test
	public void testOlaMundo() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
		Assert.assertTrue(response.statusCode() == 200);
		Assert.assertTrue("O statusCode deveria ser 200", response.statusCode() == 200);
		Assert.assertEquals(200, response.statusCode());
		
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	}
	
	@Test
	public void outrasFormasRestAssured() {
		Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/ola");
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
	
		//outra forma de fazer a mesma validacao acima
		RestAssured.get("https://restapi.wcaquino.me/ola").then().statusCode(200);	
		
		//simplificando ainda mais: Adicionando o import do RestAssured
		get("https://restapi.wcaquino.me/ola").then().statusCode(200);	
		
		//Forma mais recomendada de se fazer a validacao acima, deixando mais intuitivo e inteligivel
		given()	//Pre condicoes 
		.when()	//Acoes (get, post, ...)
			.get("https://restapi.wcaquino.me/ola")
		.then()	//Verificacoes
			.statusCode(200);		
	}
	
	@Test
	public void mathersHamcrest() {		//Validacoes possiveis usando o Matchers do Hamcrest
		Assert.assertThat("Maria", Matchers.is("Maria")); 			//valida igualdade de string
		Assert.assertThat(128, Matchers.is(128));					//valida igualdade inteiro
		Assert.assertThat(200, Matchers.isA(Integer.class));		//valida tipo
		Assert.assertThat(180, Matchers.greaterThan(100));			//valida maior
		Assert.assertThat(180, Matchers.lessThan(1000));			//valida menor		
		
		List<Integer> impares = Arrays.asList(1,3,5,7,9);
		Assert.assertThat(impares, Matchers.hasSize(5)); 			//valida tamanho de lista	
		Assert.assertThat(impares, Matchers.contains(1,3,5,7,9)); 	//valida contains na lista (a ordem precisa ser igual, e deve ser passado todos os elementos)
		Assert.assertThat(impares, Matchers.containsInAnyOrder(1,5,3,9,7));		//valida contains na lista (a ordem nao importa mais)
		Assert.assertThat(impares, Matchers.hasItem(1));	 		//valida se elemento pertence a lista
		Assert.assertThat(impares, Matchers.hasItems(1,3));	 		//valida se elementos pertencem a lista
		
		Assert.assertThat("Maria", Matchers.not("Joao")); 			//valida se um parametro nao é o outro
		Assert.assertThat("Maria", anyOf(Matchers.is("Maria"), Matchers.is("Joaquina")));	//valida se elemento é alguma das opcoes
		Assert.assertThat("Marcos", allOf(startsWith("Mar"), endsWith("cos"), containsString("arc"))); 
	}
	
	@Test
	public void validarBody() {
		given()	
		.when()
			.get("https://restapi.wcaquino.me/ola")
		.then()
			.statusCode(200)
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}
	
	
	
}
