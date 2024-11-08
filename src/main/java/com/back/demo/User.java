package com.back.demo;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
   // private String id;
    private String username;
    private double[][] matrixKey;

    public User(){

    }

    public User(String username, double[][] matrixKey){
        this.username = username;
        this.matrixKey = matrixKey;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public double[][] getMatrix() {
        return matrixKey;
    }

    public void setMatriz(double[][] matrixKey) {
        this.matrixKey = matrixKey;
    }

}
