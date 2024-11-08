package com.back.demo;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

public class Matrix {

    public static double[][] generarMatrix(double[] fft, int size, int minFreq, int maxFreq, float sampleRate){
        int n = fft.length / 2;
        double[][] matrix = new double[size][size];

        int minIndex = (int) (minFreq / (sampleRate / n));
        int maxIndex = (int) (maxFreq / (sampleRate / n));


        double[][] magnitudes = new double[maxIndex - minIndex + 1][2];
        for (int i = minIndex; i <= maxIndex; i++) {
            double real = fft[2 * i];
            double imag = fft[2 * i + 1];
            magnitudes[i - minIndex][0] = Math.sqrt(real * real + imag * imag);  // Magnitude
            magnitudes[i - minIndex][1] = i;  // Index
        }


        double maxMagnitude = Arrays.stream(magnitudes).mapToDouble(m -> m[0]).max().orElse(1.0);
        for (double[] magnitude : magnitudes) {
            magnitude[0] /= maxMagnitude;
        }


        Arrays.sort(magnitudes, Comparator.comparingDouble(o -> -o[0]));
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (count < magnitudes.length) {
                    matrix[i][j] = magnitudes[count][0];
                    count++;
                } else {
                    matrix[i][j] = 0;
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
        //System.out.println(count);
        return count > ((maxCount/2) + (maxCount/4)) ;
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
}