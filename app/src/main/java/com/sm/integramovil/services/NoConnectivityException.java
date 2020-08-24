package com.sm.integramovil.services;

import java.io.IOException;

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No hay una conexión a la red activa";
    }

}