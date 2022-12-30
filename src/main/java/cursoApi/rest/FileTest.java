package cursoApi.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FileTest {

	@Test
	public void forcarEnvioArquivo(){
		given()
			.log().all()
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(404) //Esse tipo de erro deveria retornar 400 
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void realizarUploadArquivo(){
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/paginaSiteAquinoCursoAPI.pdf"))	// multiPart = usado para enviar arquivo
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.statusCode(200) 
			.body("name", is("paginaSiteAquinoCursoAPI.pdf"))
		;
	}
	
	@Test
	public void proibirUploadDeArquivosGrandes(){
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/algumArquivoDesejado"))	// multiPart = usado para enviar arquivo
		.when()
			.post("http://restapi.wcaquino.me/upload")
		.then()
			.log().all()
			.time(lessThan(5000L))	//Definir tempo máximo para resposta
			.statusCode(200) 
		;
	}
	
	@Test
	public void realizarDownloadArquivo() throws IOException{
		byte[] image = given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/download")
		.then()
//			.log().all()
			.statusCode(200) 
			.extract().asByteArray()	// Extrair como um byteArray para armazenar em uma variavel do tipo Array de Byte (chamada de image nessa funcao)
		;
		
		File imagem = new File("src/main/resources/file.jpg");	// Definir arquivo, passando o caminho dele
		OutputStream out = new FileOutputStream(imagem);	//	Preparar o arquivo para receber
		out.write(image);	// Inserir o array de byte nessa imagem
		out.close();		// Fechar arquivo
		
		Assert.assertThat(imagem.length(), lessThan(100000L) );
	}
}
