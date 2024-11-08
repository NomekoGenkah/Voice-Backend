package com.back.demo;
import java.util.ArrayList;
import java.util.List;

public class Logica {
    List<double[][]> matrices = new ArrayList<>();
    //necesario keyMatrix?
    double[][] keyMatrix;
    
    //login: base de datos
    public boolean comparar(double[][] matrixBase, double[][] matrix){
        return true;
    }

    //register base de datos;
    public boolean uploadUser(String user, double[][] matrixKey){
        return true;
    }

    public String getUsername(String username){
        return username;
    }
    
}
