package com.back.demo;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

public class Matrix {

    public static double[][] generarMatrix(double[] fft, int size, int minFreq, int maxFreq, float sampleRate) {
        int n = fft.length / 2;
        double[][] matrix = new double[size][size];
    
        int minIndex = (int) (minFreq / (sampleRate / n));
        int maxIndex = (int) (maxFreq / (sampleRate / n));
    
        // Asegurar que minIndex y maxIndex estén dentro del rango FFT
        minIndex = Math.max(minIndex, 0);
        maxIndex = Math.min(maxIndex, n - 1);
    
        // Calcular las frecuencias en el rango de índices especificado
        double[][] frequencies = new double[maxIndex - minIndex + 1][2];
        for (int i = minIndex; i <= maxIndex; i++) {
            double frequency = i * (sampleRate / n);
            double magnitude = Math.sqrt(fft[2 * i] * fft[2 * i] + fft[2 * i + 1] * fft[2 * i + 1]);
            frequencies[i - minIndex][0] = frequency;
            frequencies[i - minIndex][1] = magnitude;
        }
    
        // Ordenar frecuencias de mayor a menor magnitud
        Arrays.sort(frequencies, Comparator.comparingDouble(o -> -o[1]));
    
        // Llenar la matriz con las frecuencias ordenadas por magnitud
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (count < frequencies.length) {
                    matrix[i][j] = frequencies[count][0];  // Llenar con la frecuencia
                    count++;
                } else {
                    matrix[i][j] = 0;  // Llenar con ceros si faltan datos
                }
            }
        }
    
        return matrix;
    }
    

    public static double[][] generarMatrixKey(List<double[][]> matrices){
        int size = matrices.get(0).length;

        double[][] keyMatrix = new double[size][size];

        for (double[][] matrix : matrices){
            for(int i = 0; i < size; i++){
                for (int j = 0; j < size; j++){
                    keyMatrix[i][j] += matrix[i][j];
                }
            }
        }

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                keyMatrix[i][j] /= matrices.size();
            }
        }
        return keyMatrix;
    }


    //true si x numero de elementos son "similares"
    public static boolean compareMatrix(double[][] matrixA, double[][] matrixB, double tolerance){
        int count = 0;
        int maxCount = matrixA.length * matrixA[0].length;
        int length = matrixA.length;
        //double tolerance = 0.09;
        
        for(int i = 0; i< length; i++){
            for(int j = 0; j< length; j++){
                if(Math.abs(matrixA[i][j] - matrixB[i][j]) <= tolerance){
                    count++;
                }
            }
        }
        if(compararMatricesEuclidiana(matrixA, matrixB)){
            count += maxCount/10;
        }
            
        int limit = (maxCount * 90)/ 100;
        System.out.println("elementos " + count);
        return count > limit-1 ;
    }

    public static double[] convertToDoubleArray(byte[] audioBytes, int bytesPerFrame) {
        double[] audioData = new double[audioBytes.length / bytesPerFrame];
        for (int i = 0; i < audioData.length; i++) {
            int value = 0;
            for (int byteIndex = 0; byteIndex < bytesPerFrame; byteIndex++) {
                value |= (audioBytes[i * bytesPerFrame + byteIndex] & 0xFF) << (byteIndex * 8);
            }
            audioData[i] = value;
        }
        return audioData;
    }

    public static void printKeyMatrix(double[][] keyMatrix) {
        System.out.println("Matriz Clave:");
        for (double[] row : keyMatrix) {
            for (double value : row) {
                System.out.printf("%.3f ", value);
            }
            System.out.println();
        }
    }

    public static double singleValue(double[][] matrix){
        double value = 0;
        int size = matrix.length * matrix.length;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                value += matrix[i][j];
            }
        }
        return value * size;
    }

    public static boolean compararMatricesEuclidiana(double[][] matrix1, double[][] matrix2) {
        double distancia = 0.0;

        for (int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[i].length; j++) {
                distancia += Math.pow(matrix1[i][j] - matrix2[i][j], 2);
            }
        }
        distancia = Math.sqrt(distancia);
        System.out.println("distancia" + distancia);

    
        return distancia < 1500;
    }

}

