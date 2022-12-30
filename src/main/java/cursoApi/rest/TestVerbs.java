package cursoApi.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import io.restassured.http.ContentType;

public class TestVerbs {

	@Test
	public void salvarUsuario() {	//Metodo POST
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Vinicius\", \"age\" : 22}")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Vinicius"))
			.body("age", is(22))
		;		
	}
	
	@Test 
	public void erroSalvarUsuarioSemNome() {	//Metodo POST
		given()
			.log().all()
			.contentType("application/json")
			.body("{ \"age\" : 22 }")
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", is("Name é um atributo obrigatório"))
		;	
	}
	
	@Test
	public void salvarUsuarioXML() {	//Metodo POST
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Vinicius</name><age>22</age></user>")
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Vinicius"))
			.body("user.age", is("22"))
		;		
	}
	
	@Test
	public void alterarUsuario() {	//Metodo PUT
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Novo Usuario\", \"age\" : 40  }")
		.when()
			.put("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Novo Usuario"))
			.body("age", is(40))
			.body("salary", is(1234.5678f))
		;		
	}

	@Test
	public void customizarURL() {	
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Novo Usuario\", \"age\" : 40  }")
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}", "users", "1")	
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Novo Usuario"))
			.body("age", is(40))
			.body("salary", is(1234.5678f))
		;		
	}
	
	@Test
	public void customizarURLparte2() {	
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\" : \"Novo Usuario\", \"age\" : 40  }")
			.pathParam("entidade", "users")
			.pathParam("userId", 1)
		.when()
			.put("https://restapi.wcaquino.me/{entidade}/{userId}")	
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name", is("Novo Usuario"))
			.body("age", is(40))
			.body("salary", is(1234.5678f))
		;		
	}
	
	@Test
	public void removerUsuario() {
		given().
			log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1")
		.then()
			.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void erroRemoverUsuarioInexistente() {
		given().
			log().all()
		.when()
			.delete("https://restapi.wcaquino.me/users/1000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
	
	@Test
	public void salvarUsuarioUsandoMAP() {	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Vinicius Via MAP");
		params.put("age", 22);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Vinicius Via MAP"))
			.body("age", is(22))
		;		
	}
	
	@Test
	public void serializarObjetoAoSalvarUser() {	
		User user = new User("Vinicius Serializar", 22);
		
		given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Vinicius Serializar"))
			.body("age", is(22))
		;		
	}	
	
	@Test
	public void deserializarObjetoAoSalvarUser() {	
		User user = new User("Vinicius Deserializado", 22);
		
		User usuarioInserido = given()
			.log().all()
			.contentType("application/json")
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;		
		
		System.out.println(usuarioInserido);
		Assert.assertEquals("Vinicius Deserializado", usuarioInserido.getName());
		Assert.assertThat(usuarioInserido.getAge(), is(22));
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
	}
	
	@Test
	public void salvarUsuarioViaXMLUsandoObjeto() {	//Metodo POST
		User user = new User("Vinicius XML", 30);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Vinicius XML"))
			.body("user.age", is("30"))
		;		
	}
	
	@Test
	public void deserializarXMLAoSalvarUser() {	//Metodo POST
		User user = new User("Vinicius XML", 30);
		
		User usuarioInserido = given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("https://restapi.wcaquino.me/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().body().as(User.class)
		;	
		
		Assert.assertThat(usuarioInserido.getId(), notNullValue());
		Assert.assertThat(usuarioInserido.getName(), is("Vinicius XML"));
		Assert.assertThat(usuarioInserido.getAge(), is(30));
		Assert.assertThat(usuarioInserido.getSalary(), nullValue()); 
	}
}