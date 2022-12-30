package cursoApi.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class UserXMLTest {

	public static RequestSpecification reqSpec;
	public static ResponseSpecification resSpec;	
	
	//BeforeClass	->	Teste a ser realizado antes dos outros testes
	//As configurações nesse metodo é para garantir que as outras ocorram com as mesmas caracteristicas
	//Dessa forma não é necessario passar todo o caminho da URL nos gets de todos os metodos (facilita controle e manutencao)
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "https://restapi.wcaquino.me";
//		RestAssured.port = 443	//caso necessario pode-se informar a porta tambem
//		RestAssured.basePath = "/v2";
		
		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.log(LogDetail.ALL);	//Funcao para pegar todo o log da requisicao
		reqSpec = reqBuilder.build();
		
		ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
		resBuilder.expectStatusCode(200);
		resSpec = resBuilder.build();
		
		RestAssured.requestSpecification = reqSpec;
		RestAssured.responseSpecification = resSpec;
	}
	
	@Test
	public void usandoXML() {
		given()
//			.spec(reqSpec)		//ira exibir o log da requisiçao 
								//Nao é mais necessario usar isso devido a instanciacao da variavel reqSpec na funcao RestAssured.requestSpecification
								//logo isso sera automatico
		.when()
			.get("/usersXML/3")
		.then()
//			.statusCode(200)		//nao é mais necessario devido ao responseSpecBuilder
//			.spec(resSpec)			//Nao é mais necessario usar isso devido a instanciacao da variavel resSpec na funcao RestAssured.responseSpecification
									//logo isso sera automatico
			
			.rootPath("user") 	//rootPath -> caminho raiz (sem ele, todos os campos do body que chamar deve ter um "user." antes
			.body("name", is("Ana Julia"))
			.body("@id", is("3")) 	//O @ é usado para referenciar atributos	
			
			.rootPath("user.filhos") 	//usando rootPath novamente
			.body("name.size()", is(2))
						
			.detachRootPath("filhos") 	//removendo o rootPath "filhos" 
			.body("filhos.name[0]", is("Zezinho"))
			.body("filhos.name[1]", is("Luizinho"))		
			
			.appendRootPath("filhos") 	//adicionando atributo ao rootPath
			.body("name", hasItem("Zezinho"))
			.body("name", hasItems("Luizinho", "Zezinho"));
	}
	
	@Test
	public void pesquisasAvancadasXML() {
		given()
			.spec(reqSpec)
		.when()
			.get("/usersXML")
		.then()
			.spec(resSpec)
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1", "2", "3"))
			.body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
			.body("users.user.age.collect{it.toInteger()*2}", hasItems(40,50,60))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));
	}
	
	@Test
	public void pesquisasXMLEJava() {
		ArrayList<NodeImpl> nomes = given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString()));
	}
	
	@Test
	public void pesquisasAvancadasXPath() {
		given()
		.when()
			.get("/usersXML")
		.then()
			.statusCode(200)
			.body(hasXPath("count(/users/user)", is("3")))	//count	->	metodo do xPath para realizar contagem
			.body(hasXPath("/users/user[@id = '1']"))		//procurar por atributo especifico
			.body(hasXPath("//user[@id = '2']"))			//utiliza-se o // para encontrar o primeiro elemento user
			.body(hasXPath("//name[text() = 'Zezinho']/../../name", is("Ana Julia")))
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))
			.body(hasXPath("/users/user/name", is("João da Silva")))
			.body(hasXPath("//name", is("João da Silva")))
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))	//last realiza a busca do ultimo elemento
			.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
			.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
			.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
			.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")));	//Mesma forma de validar a linha de cima
			
			//Site com documentacao de metodos Xpath: ferramenta Rosetta Stone Xpath
	}
}
