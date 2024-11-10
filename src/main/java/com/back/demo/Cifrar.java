package com.back.demo;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class Cifrar {


    public static SecretKeySpec generateAESKey(double[][] matrix) throws Exception {
        double matrixValue = Matrix.singleValue(matrix);
        
        // Convertir el valor double a un array de bytes
        byte[] valueBytes = Double.toString(matrixValue).getBytes();

        // Usar un hash (SHA-256) para asegurar que el tamaño de la clave sea adecuado (16 bytes para AES-128)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = digest.digest(valueBytes);
        
        // Usar solo los primeros 16 bytes para AES-128 o 32 bytes para AES-256
        byte[] keyBytes = Arrays.copyOf(hashedBytes, 16);  // Para AES-128 (32 bytes para AES-256)

        // Crear y devolver la clave AES
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static byte[] encryptWithAES(byte[] data, SecretKeySpec aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(data);
    }

    public static byte[] decryptWithAES(byte[] encryptedData, SecretKeySpec aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(encryptedData);
    }
    
}
