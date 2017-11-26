package com.example.suzanne.recipesapp.models;

/**
 * Created by suzanne on 26/11/2017.
 */

public enum MeasurementType {

    CUP,
    TBLSP,
    TSP,
    K,
    G,
    OZ,
    UNIT;

    public static MeasurementType getType(String typeName) {
        switch (typeName.toLowerCase()){
            case "cup":
                return CUP;
            case "tblsp":
                return TBLSP;
            case "tsp":
                return TSP;
            case "k":
                return K;
            case "g":
                return G;
            case "oz":
                return OZ;
            case "unit":
                return UNIT;
            default:
                return UNIT;
        }
    }


    public String toDisplayString(){
        switch(this){
            case CUP:
                return " cup";
            case TBLSP:
                return " tblsp";
            case TSP:
                return " tsp";
            case K:
                return "kg";
            case G:
                return "g";
            case OZ:
                return "oz";
            case UNIT:
                return "";
            default:
                return "";
        }
    }
}
