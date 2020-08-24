package com.sm.integramovil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.models.CredentialsViewModel;
import com.sm.integramovil.responses.CarpetasResponse;
import com.sm.integramovil.responses.LoginResponse;
import com.sm.integramovil.services.ApiEndpointInterface;
import com.sm.integramovil.services.UtilesServices;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmarDuaActivity extends AppCompatActivity {

    CarpetasModels carpetasModels;
    private InputMethodManager im;
    public EditText txtCarpetaConfirmarDua, txtNombreProveedorConfirmarDua;
    public MaskedEditText txtDuaConfirmarDua;
    public TextInputLayout lblTituloDUA;
    String titulo = "";

    public AlertDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_dua);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        carpetasModels = (CarpetasModels) intent.getSerializableExtra("carpeta");

        int largoDua = carpetasModels.Dua.replace(" ", "").replace("-", "").length();


        if (largoDua > 0) {
            titulo = getString(R.string.confirmar_dua);
        } else {
            titulo = getString(R.string.ingresar_dua);
        }

        setTitle(titulo);
        txtDuaConfirmarDua = findViewById(R.id.txtDuaConfirmarDua);
        txtDuaConfirmarDua.setText(carpetasModels.Dua);
        txtCarpetaConfirmarDua = findViewById(R.id.txtCarpetaConfirmarDua);
        txtCarpetaConfirmarDua.setText(String.valueOf(carpetasModels.Numero) + "-" + carpetasModels.Descripcion);
        txtNombreProveedorConfirmarDua = findViewById(R.id.txtNombreProveedorConfirmarDua);
        txtNombreProveedorConfirmarDua.setText(carpetasModels.NombreProveedor);
        lblTituloDUA = findViewById(R.id.lblTituloDUA);
        lblTituloDUA.setHint(titulo);


        txtDuaConfirmarDua.setInputType(0);
        txtDuaConfirmarDua.requestFocus();

        txtDuaConfirmarDua.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if (hasFocus)
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });



        FloatingActionButton fab = findViewById(R.id.btnConfirmarDua);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SACO LOS GUIONES  Y EL LARGO TIENE QUE SER 13
                final String numeroDua=txtDuaConfirmarDua.getText().toString().trim().replace("-","");

                if(numeroDua.length()!=13)
                {
                    String error = "El número de DUA debe de tener el formato ###-####-######";
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ConfirmarDuaActivity.this);
                    materialAlertDialogBuilder.setTitle(R.string.error);
                    materialAlertDialogBuilder.setMessage(error);
                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    materialAlertDialogBuilder.show();
                }
                else
                {

                    new MaterialAlertDialogBuilder(ConfirmarDuaActivity.this)
                            .setTitle(titulo)
                            .setMessage(R.string.confirmar_dua_pregunta)
                            .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    waitingDialog = new
                                            SpotsDialog.Builder().setContext(ConfirmarDuaActivity.this).build();
                                    waitingDialog.setTitle("Uploading Post");
                                    waitingDialog.setMessage(getString(R.string.espere_por_favor));
                                    waitingDialog.show();

                                    ApiEndpointInterface mAPIService = UtilesServices.getAPIService(ConfirmarDuaActivity.this);

                                    carpetasModels.Dua=txtDuaConfirmarDua.getText().toString();
                                    Call<CarpetasResponse> call = mAPIService.confirmardua(carpetasModels);

                                    call.enqueue(new Callback<CarpetasResponse>() {
                                        @Override
                                        public void onResponse(Call<CarpetasResponse> call, Response<CarpetasResponse> response) {
                                            int statusCode = response.code();
                                            if (response.isSuccessful()) {
                                                Log.i("TAG", "post submitted to API." + response.body().toString());

                                                if (response.body().Status.equals("OK")) {

                                                    waitingDialog.dismiss();

                                                    //SALTA A LA OTRA ACTIVIDAD
                                                    Intent intent = new Intent(ConfirmarDuaActivity.this, IngresoRecepcionesActivity.class);
                                                    intent.putExtra("carpeta", carpetasModels);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    waitingDialog.dismiss();
                                                    String error = response.body().Mensaje;
                                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ConfirmarDuaActivity.this);
                                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                                    materialAlertDialogBuilder.setMessage(error);
                                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                        }
                                                    });

                                                    materialAlertDialogBuilder.show();
                                                }
                                            } else {
                                                waitingDialog.dismiss();
                                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ConfirmarDuaActivity.this);
                                                materialAlertDialogBuilder.setTitle(R.string.login);
                                                materialAlertDialogBuilder.setMessage("Se ha producido un error al intentar confirmar el número de DUA. Intentelo nuevamente mas tarde.");
                                                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                materialAlertDialogBuilder.show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CarpetasResponse> call, Throwable t) {
                                            waitingDialog.dismiss();
                                            String error = t.getMessage();

                                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ConfirmarDuaActivity.this);
                                            materialAlertDialogBuilder.setTitle(R.string.error);
                                            materialAlertDialogBuilder.setMessage(error);
                                            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            materialAlertDialogBuilder.show();
                                        }
                                    });


                                    waitingDialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                }


            }
        });
    }
}