package com.sm.integramovil.services;

import com.sm.integramovil.models.ArticulosModels;
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.models.CredentialsViewModel;
import com.sm.integramovil.models.RecepcionModels;
import com.sm.integramovil.models.UbicacionModels;
import com.sm.integramovil.modelsfilters.ArticulosModelsFiltro;
import com.sm.integramovil.responses.ArticuloReponse;
import com.sm.integramovil.responses.CarpetasResponse;
import com.sm.integramovil.responses.LoginResponse;
import com.sm.integramovil.responses.RecepcionResponse;
import com.sm.integramovil.responses.UbicacionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface  ApiEndpointInterface {


    @POST("auth/login")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<LoginResponse> authenticate(@Body CredentialsViewModel usuario);

    @GET("api/carpetas")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<CarpetasResponse> listaCarpetas();

    @POST("api/carpetas/confirmardua")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<CarpetasResponse> confirmardua(@Body CarpetasModels carpetasModels);

    @POST("api/articulos/getarticuloporcodigobarras")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArticuloReponse> getarticuloporcodigoBarras(@Body ArticulosModelsFiltro articulosModelsFiltro);

    @POST("api/articulos/updatearticulo")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ArticuloReponse> updatearticulo(@Body ArticulosModels articulosModels);

    @POST("api/ubicacion")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<UbicacionResponse> getubicacion(@Body UbicacionModels ubicacionModels);

    @POST("api/recepcion/recibircarpeta")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<RecepcionResponse> recibircarpeta(@Body RecepcionModels recepcionModels);





}
