package com.sm.integramovil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sm.integramovil.models.ArticulosModels;
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.models.RecepcionModels;
import com.sm.integramovil.models.UbicacionModels;
import com.sm.integramovil.modelsfilters.ArticulosModelsFiltro;
import com.sm.integramovil.responses.ArticuloReponse;
import com.sm.integramovil.responses.CarpetasResponse;
import com.sm.integramovil.responses.RecepcionResponse;
import com.sm.integramovil.responses.UbicacionResponse;
import com.sm.integramovil.services.ApiEndpointInterface;
import com.sm.integramovil.services.UtilesServices;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sm.integramovil.ProductoActivity.isNumeric;

public class IngresoRecepcionesActivity extends AppCompatActivity {

    int LAUNCH_SECOND_ACTIVITY = 1;
    private RelativeLayout mRelativeLayout;
    TextInputEditText txtNombreProveedorRecepcion, txtNombreCarpetaIngresoRecepciones, txtCodigoBarrasIngresoRecepciones;
    private PopupWindow mPopupWindow;
    TextInputEditText txtArticuloIngresoRecepciones, txtLargoIngresoRecepciones, txtAnchoIngresoRecepciones,
            txtAlturaIngresoRecepciones, txtCantidadIngresoRecepciones,txtCantidadAcumuladaIngresoRecepciones,
    txtIngresarCantidadIngresoRecepciones,txtLpnIngresoRecepciones;
    FloatingActionButton btnGuardarRecepcion;

    ArticulosModels articulosModels;
    CarpetasModels carpetasModels;
    public AlertDialog waitingDialog;

    boolean esProductoValido=false;
    boolean esCantidadValida=false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_recepciones);
        setTitle(getString(R.string.ingreso_recepciones));

        mRelativeLayout = (RelativeLayout) findViewById(R.id.layout_ingreso_recepciones);
        txtNombreProveedorRecepcion = (TextInputEditText) findViewById(R.id.txtNombreProveedorRecepcion);
        txtNombreCarpetaIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtNombreCarpetaIngresoRecepciones);
        txtCodigoBarrasIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtCodigoBarrasIngresoRecepciones);
        txtArticuloIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtArticuloIngresoRecepciones);
        txtLargoIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtLargoIngresoRecepciones);
        txtAnchoIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtAnchoIngresoRecepciones);
        txtAlturaIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtAlturaIngresoRecepciones);
        txtCantidadIngresoRecepciones = (TextInputEditText) findViewById(R.id.txtCantidadIngresoRecepciones);
        txtCantidadAcumuladaIngresoRecepciones= (TextInputEditText) findViewById(R.id.txtCantidadAcumuladaIngresoRecepciones);
        txtIngresarCantidadIngresoRecepciones= (TextInputEditText) findViewById(R.id.txtIngresarCantidadIngresoRecepciones);
        txtLpnIngresoRecepciones= (TextInputEditText) findViewById(R.id.txtLpnIngresoRecepciones);
        btnGuardarRecepcion = (FloatingActionButton) findViewById(R.id.btnGuardarRecepcion);

        btnGuardarRecepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                materialAlertDialogBuilder.setTitle(R.string.guardar_recepcion);
                materialAlertDialogBuilder.setMessage(R.string.guardar_cambios);
                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        waitingDialog = new
                                SpotsDialog.Builder().setContext(IngresoRecepcionesActivity.this).build();
                        waitingDialog.setTitle("Uploading Post");
                        waitingDialog.setMessage(getString(R.string.espere_por_favor));
                        waitingDialog.show();

                        RecepcionModels recepcionModels =new RecepcionModels();

                        recepcionModels.PrvNum=carpetasModels.CodigoProveedor;
                        recepcionModels.CarSer =  carpetasModels.Serie;
                        recepcionModels.CarNum = carpetasModels.Numero;
                        recepcionModels.CarLin = articulosModels.LineaCarpeta;
                        recepcionModels.ArtCod=articulosModels.Codigo;

                        String cantidadCampo=txtIngresarCantidadIngresoRecepciones.getText().toString().trim();

                        if(isNumeric(cantidadCampo)) {
                            float  cantidad = Float.parseFloat(cantidadCampo);
                            recepcionModels.CarCanRec=cantidad;
                        }
                        else
                        {
                            waitingDialog.dismiss();

                            String error = "Ingrese la cantidad";
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                            materialAlertDialogBuilder.setTitle(R.string.error);
                            materialAlertDialogBuilder.setMessage(error);
                            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    txtIngresarCantidadIngresoRecepciones.requestFocus();
                                }
                            }).show();

                        }

                        String codigoLpn=txtLpnIngresoRecepciones.getText().toString().trim();

                        if(codigoLpn.length()>0) {
                            recepcionModels.UbiCodDes = codigoLpn;                        }
                        else
                        {
                            waitingDialog.dismiss();
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                            materialAlertDialogBuilder.setTitle(R.string.error);
                            materialAlertDialogBuilder.setMessage(R.string.ingrese_lpn_destino);
                            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    txtLpnIngresoRecepciones.requestFocus();
                                }
                            }).show();

                        }

                        SharedPreferences pref = getSharedPreferences("integramovil",MODE_PRIVATE);
                        String email = pref.getString("email", null);
                        recepcionModels.UsrCod=email;

                        ApiEndpointInterface mAPIService = UtilesServices.getAPIService(IngresoRecepcionesActivity.this);

                        Call<RecepcionResponse> call = mAPIService.recibircarpeta(recepcionModels);

                        call.enqueue(new Callback<RecepcionResponse>() {
                            @Override
                            public void onResponse(Call<RecepcionResponse> call, Response<RecepcionResponse> response) {
                                int statusCode = response.code();
                                if (response.isSuccessful()) {
                                    Log.i("TAG", "post submitted to API." + response.body().toString());

                                    if (response.body().Status.equals("OK")) {

                                        waitingDialog.dismiss();

                                        finish();
                                    } else {
                                        waitingDialog.dismiss();
                                        String error = response.body().Mensaje;
                                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
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
                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.login);
                                    materialAlertDialogBuilder.setMessage("Se ha producido un error al intentar al guardar la recepción. Intentelo nuevamente mas tarde.");
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RecepcionResponse> call, Throwable t) {
                                waitingDialog.dismiss();
                                String error = t.getMessage();

                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
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
                });
                materialAlertDialogBuilder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                materialAlertDialogBuilder.show();

            }
        });


        txtIngresarCantidadIngresoRecepciones.setInputType(0);
        txtIngresarCantidadIngresoRecepciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });

        txtCodigoBarrasIngresoRecepciones.setInputType(0);
        txtCodigoBarrasIngresoRecepciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });

        txtLpnIngresoRecepciones.setInputType(0);
        txtLpnIngresoRecepciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        v.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
                    }
                }
            }
        });


        Intent intent = getIntent();
        carpetasModels = (CarpetasModels) intent.getSerializableExtra("carpeta");
        txtNombreProveedorRecepcion.setText(carpetasModels.NombreProveedor);
        txtNombreCarpetaIngresoRecepciones.setText(String.valueOf(carpetasModels.Numero) + " - " + carpetasModels.Descripcion);

        txtCodigoBarrasIngresoRecepciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    String codigo=txtCodigoBarrasIngresoRecepciones.getText().toString().trim();



                         BuscarProducto(codigo);

                }
            }
        });

        txtCodigoBarrasIngresoRecepciones.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

                    String codigo=txtCodigoBarrasIngresoRecepciones.getText().toString().trim();

                    if(codigo.length()>0) {
                        BuscarProducto(codigo);
                    }
                    else
                    {
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                        materialAlertDialogBuilder.setTitle(R.string.error);
                        materialAlertDialogBuilder.setMessage("No se ha leído un código de barras");
                        materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                txtCodigoBarrasIngresoRecepciones.requestFocus();
                            }
                        });

                        materialAlertDialogBuilder.show();


                        txtCodigoBarrasIngresoRecepciones.requestFocus();
                    }
                    return true;
                }

                return false;
            }
        });


        txtIngresarCantidadIngresoRecepciones.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

                    ValidarCantidadIngresada();
                    return true;
                }

                return false;
            }
        });


        txtIngresarCantidadIngresoRecepciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(esProductoValido)
                    {
                        ValidarCantidadIngresada();
                    }

                }

                if(hasFocus)
                {
                    if(!esProductoValido)
                    {
                        txtCodigoBarrasIngresoRecepciones.requestFocus();
                    }
                }
            }
        });


        txtCodigoBarrasIngresoRecepciones.requestFocus();

        txtLpnIngresoRecepciones.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    if(!esCantidadValida)
                    {
                        txtIngresarCantidadIngresoRecepciones.requestFocus();
                    }
                }

                if(!hasFocus)
                {
                    String codigoUbicacion=txtLpnIngresoRecepciones.getText().toString().trim();

                    if(codigoUbicacion.length()==0)
                    {
                       /* MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                        materialAlertDialogBuilder.setTitle(R.string.error);
                        materialAlertDialogBuilder.setMessage(R.string.ingrese_codigo_ubicacion);
                        materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                txtLpnIngresoRecepciones.requestFocus();
                            }
                        });
                        materialAlertDialogBuilder.show();*/
                    }
                    else {

                        UbicacionModels ubicacionModels=new UbicacionModels();
                        ubicacionModels.Codigo=codigoUbicacion;
                        ubicacionModels.CodigoArticulo=articulosModels.Codigo;

                        waitingDialog = new
                                SpotsDialog.Builder().setContext(IngresoRecepcionesActivity.this).build();
                        waitingDialog.setTitle("Uploading Post");
                        waitingDialog.setMessage(getString(R.string.espere_por_favor));
                        waitingDialog.show();

                        ApiEndpointInterface mAPIService = UtilesServices.getAPIService(IngresoRecepcionesActivity.this);

                        Call<UbicacionResponse> call = mAPIService.getubicacion(ubicacionModels);

                        call.enqueue(new Callback<UbicacionResponse>() {
                            @Override
                            public void onResponse(Call<UbicacionResponse> call, Response<UbicacionResponse> response) {
                                int statusCode = response.code();
                                if (response.isSuccessful()) {
                                    Log.i("TAG", "post submitted to API." + response.body().toString());

                                    if (response.body().Status.equals("OK")) {

                                        waitingDialog.dismiss();



                                    } else {
                                        waitingDialog.dismiss();
                                        String error = response.body().Mensaje;
                                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                                        materialAlertDialogBuilder.setTitle(R.string.error);
                                        materialAlertDialogBuilder.setMessage(error);
                                        materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                txtLpnIngresoRecepciones.setText("");
                                                txtLpnIngresoRecepciones.requestFocus();
                                            }
                                        });

                                        materialAlertDialogBuilder.show();

                                    }
                                } else {
                                    waitingDialog.dismiss();
                                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                                    materialAlertDialogBuilder.setTitle(R.string.error);
                                    materialAlertDialogBuilder.setMessage("Se ha producido un error al consultar la úbicacion. Intentelo nuevamente mas tarde.");
                                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    materialAlertDialogBuilder.show();
                                    esProductoValido=false;
                                }
                            }

                            @Override
                            public void onFailure(Call<UbicacionResponse> call, Throwable t) {
                                waitingDialog.dismiss();
                                String error = t.getMessage();

                                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
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

                    }

                }
            }
        });

    }

    private void ValidarCantidadIngresada() {
        String cantidadCampo=txtIngresarCantidadIngresoRecepciones.getText().toString().trim();

        if(isNumeric(cantidadCampo)) {

            float  cantidad = Float.parseFloat(cantidadCampo);

            if(cantidad==0)
            {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                materialAlertDialogBuilder.setTitle(R.string.error);
                materialAlertDialogBuilder.setMessage(R.string.la_cantidad_no_puede_ser_cero);
                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtCantidadIngresoRecepciones.requestFocus();
                    }
                });
                materialAlertDialogBuilder.show();
                esCantidadValida=false;
            }
            else if(cantidad<0)
            {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                materialAlertDialogBuilder.setTitle(R.string.error);
                materialAlertDialogBuilder.setMessage(R.string.error_cantidad_menor_cero);
                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtCantidadIngresoRecepciones.requestFocus();
                    }
                });
                materialAlertDialogBuilder.show();

                esCantidadValida=false;
            }
            else
            {
                float cantidadTotal=articulosModels.LineaCantidadRecibida + cantidad;

                if(cantidadTotal > articulosModels.LineaCantidad)
                {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                    materialAlertDialogBuilder.setTitle(R.string.aviso);
                    materialAlertDialogBuilder.setMessage(R.string.aviso_cantidad_acumulada);
                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            esCantidadValida=true;
                            txtLpnIngresoRecepciones.requestFocus();

                        }
                    });
                    materialAlertDialogBuilder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            esCantidadValida=false;
                            txtCantidadIngresoRecepciones.requestFocus();

                        }
                    });
                    materialAlertDialogBuilder.show();


                }
                else
                {

                    txtLpnIngresoRecepciones.requestFocus();
                    esCantidadValida=true;
                }

            }

        }
        else
        {
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
            materialAlertDialogBuilder.setTitle(R.string.error);
            materialAlertDialogBuilder.setMessage(R.string.cantidad_incorrecta);
            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txtCantidadIngresoRecepciones.requestFocus();
                }
            });
            materialAlertDialogBuilder.show();

            esCantidadValida=false;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_productos, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                ArticulosModels resultado= (ArticulosModels) data.getSerializableExtra("result");

                if(resultado != null)
                {
                    BuscarProducto(resultado.CodigoBarras);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//on


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
    // ActivityResult
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_producto) {

            if(articulosModels!=null)
            {
                Intent intent = new Intent(this, ProductoActivity.class);
                intent.putExtra("articulo", articulosModels);
                startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
            }

            return true;
        }
        else if(id == R.id.action_etiqueta) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void BuscarProducto(String codigoBarras) {

        final String codigo=codigoBarras;

        if (codigo.length() == 0) {
            String error = "No se ha leído un código de barras";
            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
            materialAlertDialogBuilder.setTitle(R.string.error);
            materialAlertDialogBuilder.setMessage(error);
            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    txtCodigoBarrasIngresoRecepciones.requestFocus();
                }
            });

            materialAlertDialogBuilder.show();

            esProductoValido= false;

        } else {
            waitingDialog = new
                    SpotsDialog.Builder().setContext(IngresoRecepcionesActivity.this).build();
            waitingDialog.setTitle("Uploading Post");
            waitingDialog.setMessage(getString(R.string.espere_por_favor));
            waitingDialog.show();

            ApiEndpointInterface mAPIService = UtilesServices.getAPIService(IngresoRecepcionesActivity.this);

            final ArticulosModelsFiltro articulosModelsFiltro = new ArticulosModelsFiltro();
            articulosModelsFiltro.CodigoBarras = codigo;
            articulosModelsFiltro.CodigoProveedor = carpetasModels.CodigoProveedor;
            articulosModelsFiltro.NumeroCarpeta = carpetasModels.Numero;
            articulosModelsFiltro.SerieCarpeta = carpetasModels.Serie;

            Call<ArticuloReponse> call = mAPIService.getarticuloporcodigoBarras(articulosModelsFiltro);
            call.enqueue(new Callback<ArticuloReponse>() {
                @Override
                public void onResponse(Call<ArticuloReponse> call, Response<ArticuloReponse> response) {
                    int statusCode = response.code();
                    if (response.isSuccessful()) {
                        Log.i("TAG", "post submitted to API." + response.body().toString());

                        if (response.body().Status.equals("OK")) {

                            waitingDialog.dismiss();
                            articulosModels=response.body().Articulo;
                            articulosModels.CodigoBarras=codigo;
                            txtCodigoBarrasIngresoRecepciones.setText(codigo);
                            txtArticuloIngresoRecepciones.setText(articulosModels.Descripcion);
                            txtLargoIngresoRecepciones.setText(String.valueOf(articulosModels.Largo));
                            txtAnchoIngresoRecepciones.setText(String.valueOf(articulosModels.Ancho));
                            txtAlturaIngresoRecepciones .setText(String.valueOf(articulosModels.Altura));
                            txtCantidadIngresoRecepciones.setText(String.valueOf(articulosModels.Cantidad));
                            txtCantidadAcumuladaIngresoRecepciones.setText(String.valueOf(articulosModels.LineaCantidadRecibida));
                            //txtIngresarCantidadIngresoRecepciones.setText(String.valueOf(articulosModels.LineaCantidad));

                            txtIngresarCantidadIngresoRecepciones.requestFocus();
                            esProductoValido=true;

                        } else {
                            waitingDialog.dismiss();
                            String error = response.body().Mensaje;
                            MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                            materialAlertDialogBuilder.setTitle(R.string.error);
                            materialAlertDialogBuilder.setMessage(error);
                            materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    txtCodigoBarrasIngresoRecepciones.setText("");
                                    txtCodigoBarrasIngresoRecepciones.requestFocus();
                                }
                            });

                            materialAlertDialogBuilder.show();
                            esProductoValido=false;
                        }
                    } else {
                        waitingDialog.dismiss();
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                        materialAlertDialogBuilder.setTitle(R.string.error);
                        materialAlertDialogBuilder.setMessage("Se ha producido un error al consultar el código de barras. Intentelo nuevamente mas tarde.");
                        materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        materialAlertDialogBuilder.show();
                        esProductoValido=false;
                    }
                }

                @Override
                public void onFailure(Call<ArticuloReponse> call, Throwable t) {
                    waitingDialog.dismiss();
                    String error = t.getMessage();

                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(IngresoRecepcionesActivity.this);
                    materialAlertDialogBuilder.setTitle(R.string.error);
                    materialAlertDialogBuilder.setMessage(error);
                    materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    materialAlertDialogBuilder.show();
                    esProductoValido=false;
                }
            });


        }
    }
}