package com.rybakov.eps.models;

public class Type {
    private int idType;
    private String typeName;

    public Type(int idType, String typeName) {
        this.idType = idType;
        this.typeName = typeName;
    }

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public Type(){}

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
