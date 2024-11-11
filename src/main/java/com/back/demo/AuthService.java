package com.back.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	//   ta listo CAMBIAR. ESTA FUNCION DEBE DEVOLVER TRUE SI LA MATRIZ DE USUARIO COINCIDE. SOLO DEBE HACER PULL DE LA DE USUARIO Y HACER LA MATRIZ DEL AUDIO SELECCIONADO
    public boolean authenticateWithAudio(MultipartFile audioFile, String username) {
		if (audioFile != null && !audioFile.isEmpty()) {
			try {
				Optional<User> userOptional = userRepository.findByUsername(username);
				User user = userOptional.get();

				System.out.println(audioFile.getOriginalFilename());
				byte[] audioBytes = audioFile.getBytes();
				//Hay que cambiar bytes per frame
				double[] audioDouble = Matrix.convertToDoubleArray(audioBytes, 1);
				double[] fft = FFT.Fourier(audioDouble);

				double[][] matrix = Matrix.generarMatrix(fft, 5, 300, 1500, 16000.0f);
				double[][] matrixUser = user.getMatrix();

				if(Matrix.compareMatrix(matrix, matrixUser, 0.04)){
					return true;
				}else{
					return false;
				}
			} catch (Exception e) {
				System.out.println("no se pudo");
			}
		}
		return false; // Si no hay archivo o no es válido
	}

	public boolean userExists(String username){
		return userRepository.findByUsername(username).isPresent();
	}

	
	public double[][] getUserKeyMatrix(String username){
		Optional<User> userOptional = userRepository.findByUsername(username);
		return userOptional.get().getMatrix();

	}

	public boolean saveUserToDatabase(String username, double[][] matrixKey){

		try {
			System.out.println("NOMBRE DE USUARIO:");
			System.out.println("NOMBRE DE USUARIO" + username);
			Matrix.printKeyMatrix(matrixKey);
			User newUser = new User(username, matrixKey);
			userRepository.save(newUser);
		} catch (Exception e) {
			System.out.println("error al subir usuario a db: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean registerUser(List<MultipartFile> audioList, String username){
		try {
			List<double[][]> matrixList = new ArrayList<>();

			for (int i = 0; i < audioList.size(); i++) {
				byte[] byteAudio = audioList.get(i).getBytes();
				double[] audioData = Matrix.convertToDoubleArray(byteAudio, 1); //ver los frames per second
				double[] fourier = FFT.Fourier(audioData);
				double[][] matrix = Matrix.generarMatrix(fourier, 5, 300, 2500, 16000.0f);
				matrixList.add(matrix);
			}
			double[][] matrixKey = Matrix.generarMatrixKey(matrixList);
			if(saveUserToDatabase(username, matrixKey)){
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}


	public byte[] encryptFile(String username, byte[] fileBytes){
		try {
			double[][] matrixKey = getUserKeyMatrix(username);
			SecretKeySpec keySpec = Cifrar.generateAESKey(matrixKey);

			//int[][] matrixInt = Matrix.convertMatrixToInt(matrixKey);
			//byte[] matrixBytes = Cifrar.encryptWithMatrix(fileBytes, matrixKey);

			byte[] encryptedData = Cifrar.encryptWithAES(fileBytes, keySpec);

			return encryptedData;
		} catch (Exception e) {
			System.out.println("error en encryptFile");
			return null;
			// TODO: handle exception
		}
	}

	public byte[] decryptFile(String username, byte[] fileBytes){
		try {
			double[][] matrixKey = getUserKeyMatrix(username);
			SecretKeySpec keySpec = Cifrar.generateAESKey(matrixKey);

			//int[][] matrixInt = Matrix.convertMatrixToInt(matrixKey);

			byte[] decryptedData = Cifrar.decryptWithAES(fileBytes, keySpec);
			//byte[] matrixBytes = Cifrar.decryptWithMatrix(decryptedData, matrixKey);

			return decryptedData;
		} catch (Exception e) {
			System.out.println("error en encryptFile");
			return null;
			// TODO: handle exception
		}
	}
}
