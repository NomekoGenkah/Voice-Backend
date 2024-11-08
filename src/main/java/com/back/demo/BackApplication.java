package com.back.demo;

//import java.net.http.HttpHeaders;
import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import com.mongodb.client.MongoClient;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableMongoRepositories
@RestController
public class BackApplication {
	private final AuthService authService;

	public BackApplication(AuthService authService){
		this.authService = authService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
		//UserTest test = new UserTest();
		//test.testSaveUser();
		System.out.println("nulls");
	}

	@GetMapping
	public List<String> hello(){
		return List.of("Hello", "World");
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam("audioFile") MultipartFile audioFile,
										@RequestParam("username") String username){
        // Lógica para procesar el archivo de audio y autenticar
		if(!authService.userExists(username)){
			System.out.println(username);
			System.out.println("Usuario no encontrado");
			return ResponseEntity.status(401).body("Usuario Incorrecto");
		}

        boolean isAuthenticated = authService.authenticateWithAudio(audioFile, username);
        if (isAuthenticated) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(400).body("Autenticación fallida");
        }
    }

	@CrossOrigin(origins = "*")
	@PostMapping("/uploadRegister")
	public ResponseEntity<String> uploadRegister(@RequestParam("audioFile1") MultipartFile audFile1,
												 @RequestParam("audioFile2") MultipartFile audFile2,
												 @RequestParam("audioFile3") MultipartFile audFile3,
												 @RequestParam("username") String username){
		List<MultipartFile> audioFiles = List.of(audFile1, audFile2, audFile3);
		if(authService.registerUser(audioFiles, username)){
			return ResponseEntity.ok("Perfil creado");

		}
		return ResponseEntity.status(400).body("error al crear usuario");
	}

	@CrossOrigin(origins = "*")
	@PostMapping("/encrypt")
	public ResponseEntity<byte[]> encryptFile(@RequestParam("file") MultipartFile file,
											  @RequestParam("username") String username){
		try {

			byte[] fileContent = file.getBytes();
			byte[] encryptedData = authService.encryptFile(username, fileContent);


            HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename("encriptado.txt").build());

			return ResponseEntity.ok().headers(headers).body(encryptedData);


		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
			// TODO: handle exception
		}
	}


	@CrossOrigin(origins = "*")
	@PostMapping("/decrypt")
	public ResponseEntity<byte[]> decryptFile(@RequestParam("file") MultipartFile file,
											  @RequestParam("username") String username){
		try {

			byte[] fileContent = file.getBytes();
			byte[] decryptedData = authService.decryptFile(username, fileContent);

            HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename("original.txt").build());

			return ResponseEntity.ok().headers(headers).body(decryptedData);


		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
			// TODO: handle exception
		}
	}



}