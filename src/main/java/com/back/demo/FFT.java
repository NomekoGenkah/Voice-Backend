package com.back.demo;
import org.jtransforms.fft.DoubleFFT_1D;

public class FFT {
    public static double[] Fourier(double[] audioData){
        
        int n = audioData.length;
        DoubleFFT_1D fft = new DoubleFFT_1D(n);
        double[] complexData = new double[n * 2];
        
        System.arraycopy(audioData, 0, complexData, 0, n);
        fft.realForwardFull(complexData);

        return complexData;
    }
}