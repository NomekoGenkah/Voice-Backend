
				if(Matrix.compareMatrix(matrix, matrixUser, 0.08)){
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

			byte[] decryptedData = Cifrar.decryptWithAES(fileBytes, keySpec);

			return decryptedData;
		} catch (Exception e) {
			System.out.println("error en encryptFile");
			return null;
			// TODO: handle exception
		}
	}


}
