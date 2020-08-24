package com.sm.integramovil.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class CarpetasModels implements Serializable {
    public String Serie ;
    public int Numero;
    public String Descripcion ;
    public String Dua ;
    public String Fecha  ;
    public int CodigoProveedor ;
    public String NombreProveedor ;

    public CarpetasModels() {
    }
/*
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Numero);
        dest.writeString(Descripcion);
        dest.writeString(Dua);
        dest.writeString(Fecha);
        dest.writeInt(CodigoProveedor);
        dest.writeString(NombreProveedor);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CarpetasModels createFromParcel(Parcel in) {
            return new CarpetasModels(in);
        }

        public CarpetasModels[] newArray(int size) {
            return new CarpetasModels[size];
        }
    };*/

}
