package com.sm.integramovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sm.integramovil.models.ArticulosModels;
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.responses.ArticuloReponse;
import com.sm.integramovil.responses.CarpetasResponse;
import com.sm.integramovil.services.ApiEndpointInterface;
import com.sm.integramovil.services.UtilesServices;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductoActivity extends AppCompatActivity {

    public AlertDialog waitingDialog;
    MaterialButton btnOk;
    ArticulosModels articulosModels;
    TextInputEditText txtPesoProducto, txtArticuloProducto, txtCodigoArticuloProducto, txtPackProducto, txtLargoProducto, txtAnchoProducto, txtAlturaProducto, txtCantidadProducto;
    FloatingActionButton  btnGuardarProducto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);
        setTitle(getString(R.string.art_culo));
        Intent intent = getIntent();
        articulosModels = (ArticulosModels) intent.getSerializableExtra("articulo");

        btnGuardarProducto=findViewById(R.id.btnGuardarProducto);
        txtArticuloProducto = findViewById(R.id.txtArticuloProducto);
        txtCodigoArticuloProducto = findViewById(R.id.txtCodigoArticuloProducto);
        txtPackProducto = findViewById(R.id.txtPackProducto);
        txtLargoProducto = findViewById(R.id.txtLargoProducto);
        txtAnchoProducto = findViewById(R.id.txtAnchoProducto);
        txtAlturaProducto = findViewById(R.id.txtAlturaProducto);
        txtCantidadProducto = findViewById(R.id.txtCantidadProducto);
        txtPesoProducto= findViewById(R.id.txtPesoProducto);

        txtArticuloProducto.setText(articulosModels.Descripcion);
        txtCodigoArticuloProducto.setText(articulosModels.Codigo);
        txtPackProducto.setText(articulosModels.Package);
        txtLargoProducto.setText(String.valueOf(articulosModels.Largo));
        txtAnchoProducto.setText(String.valueOf(articulosModels.Ancho));
        txtAlturaProducto.setText(String.valueOf(articulosModels.Altura));
        txtCantidadProducto.setText(String.valueOf(articulosModels.Cantidad));
        txtPesoProducto.setText(String.valueOf(articulosModels.Peso));

        txtPesoProducto.setInputType(0);
        txtPesoProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });

        txtLargoProducto.setInputType(0);
        txtLargoProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });

        txtAnchoProducto.setInputType(0);
        txtAnchoProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });

        txtAlturaProducto.setInputType(0);
        txtAlturaProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });

        txtCantidadProducto.setInputType(0);
        txtCantidadProducto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });


        btnGuardarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(ProductoActivity.this)
                        .setTitle(R.string.guardar_cambios)
                        .setPositiveButton(R.string.guardar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //articulosModels

                                String largo= txtLargoProducto.getText().toString();
                                String ancho= txtAnchoProducto.getText().toString();
                                String altura= txtAlturaProducto.getText().toString();
                                String cantidad= txtCantidadProducto.getText().toString();
                                String peso= txtPesoProducto.getText().toString();

                                String error="";

                                boolean esLargo=isNumeric(largo);
                                boolean esAncho=isNumeric(ancho);
                                boolean esAltura=isNumeric(altura);
                                boolean esCantidad=isNumeric(cantidad);
                                boolean esPeso=isNumeric(peso);

                                if(!esLargo)
                                {
                                    error="El largo no es correcto";

                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                    materialAlertDialogBuilder.setMessage(error);
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            txtLargoProducto.requestFocus();
                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                                else if(!esAncho)
                                {
                                    error="El ancho no es correcto";

                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                    materialAlertDialogBuilder.setMessage(error);
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            txtAnchoProducto.requestFocus();
                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                                else if(!esAltura)
                                {
                                    error="La altura no es correcto";

                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                    materialAlertDialogBuilder.setMessage(error);
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            txtAlturaProducto.requestFocus();
                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                                else if(!esCantidad)
                                {
                                    error="La cantidad no es correcta";

                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                    materialAlertDialogBuilder.setMessage(error);
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            txtCantidadProducto.requestFocus();
                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                                else if(!esPeso)
                                {
                                    error="El peso no es correcto";

                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                    materialAlertDialogBuilder.setMessage(error);
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            txtPesoProducto.requestFocus();
                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                                else {
                                    waitingDialog = new
                                            SpotsDialog.Builder().setContext(ProductoActivity.this).build();
                                    waitingDialog.setTitle("Uploading Post");
                                    waitingDialog.setMessage(getString(R.string.espere_por_favor));
                                    waitingDialog.show();

                                    ApiEndpointInterface mAPIService = UtilesServices.getAPIService(ProductoActivity.this);

                                    float flargo= Float.parseFloat(txtLargoProducto.getText().toString());
                                    float fancho= Float.parseFloat(txtAnchoProducto.getText().toString());
                                    float faltura= Float.parseFloat(txtAlturaProducto.getText().toString());
                                    int fcantidad= Integer.parseInt(txtCantidadProducto.getText().toString());
                                    float fpeso= Float.parseFloat(txtPesoProducto.getText().toString());

                                    articulosModels.Largo =flargo;
                                    articulosModels.Ancho =fancho;
                                    articulosModels.Altura =faltura;
                                    articulosModels.Cantidad =fcantidad;
                                    articulosModels.Peso =fpeso;

                                    Call<ArticuloReponse> call = mAPIService.updatearticulo(articulosModels);

                                    call.enqueue(new Callback<ArticuloReponse>() {
                                        @Override
                                        public void onResponse(Call<ArticuloReponse> call, Response<ArticuloReponse> response) {
                                            int statusCode = response.code();
                                            if (response.isSuccessful()) {
                                                Log.i("TAG", "post submitted to API." + response.body().toString());

                                                if (response.body().Status.equals("OK")) {

                                                    waitingDialog.dismiss();
                                                    Intent returnIntent = new Intent();
                                                    returnIntent.putExtra("result",articulosModels);
                                                    setResult(Activity.RESULT_OK,returnIntent);
                                                    finish();
                                                } else {
                                                    waitingDialog.dismiss();
                                                    String error = response.body().Mensaje;
                                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
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
                                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
                                                materialAlertDialogBuilder.setTitle(R.string.login);
                                                materialAlertDialogBuilder.setMessage("Se ha producido un error al intentar actualizar el producto. Intentelo nuevamente mas tarde.");
                                                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                });
                                                materialAlertDialogBuilder.show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArticuloReponse> call, Throwable t) {
                                            waitingDialog.dismiss();
                                            String error = t.getMessage();

                                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ProductoActivity.this);
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
                            }
                        })
                        .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.show();


            }
        });
        /*btnOk =findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","hola");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });*/


    }



    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}