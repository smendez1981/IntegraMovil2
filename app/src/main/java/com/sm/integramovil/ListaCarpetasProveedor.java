package com.sm.integramovil;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sm.integramovil.adapters.CarpetasAdapter;
import com.sm.integramovil.adapters.ProveedoresAdapter;
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.models.ProveedorModels;
import com.sm.integramovil.responses.CarpetasResponse;
import com.sm.integramovil.services.ApiEndpointInterface;
import com.sm.integramovil.services.UtilesServices;
import com.sm.integramovil.ui.home.RecepcionCarpetasFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaCarpetasProveedor extends AppCompatActivity {

    public android.app.AlertDialog waitingDialog;
    public static View.OnClickListener myOnClickListener;

    private static CarpetasAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private TextView empty_view;
    private static int codigoProveedor;
    private static ArrayList<CarpetasModels> dataCarpetas;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carpetas_proveedor);
        adapter=null;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_carpetas_carpetas);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        setTitle(getString(R.string.recepcion_carpetas));

        empty_view=(TextView) findViewById(R.id.empty_view_carpetas);
        codigoProveedor = (int) getIntent().getIntExtra("codigoProveedor",0);

        CargarCarpetas();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    public void CargarCarpetas() {


        waitingDialog = new
                SpotsDialog.Builder().setContext(this).build();
        waitingDialog.setTitle("Uploading Post");
        waitingDialog.setMessage(getString(R.string.espere_por_favor));
        waitingDialog.show();

        ApiEndpointInterface mAPIService = UtilesServices.getAPIService(this);

        Call<CarpetasResponse> call = mAPIService.listaCarpetas();

        call.enqueue(new Callback<CarpetasResponse>() {
            @Override
            public void onResponse(Call<CarpetasResponse> call, Response<CarpetasResponse> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Log.i("TAG", "post submitted to API." + response.body().toString());
                    waitingDialog.dismiss();
                    if (response.body().Status.equals("OK")) {
                        dataCarpetas=new ArrayList<>();
                        for (CarpetasModels carpeta: response.body().Carpetas) {

                            if(carpeta.CodigoProveedor==codigoProveedor) {
                                dataCarpetas.add(carpeta);
                            }
                        }

                        adapter = new CarpetasAdapter(dataCarpetas);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        waitingDialog.dismiss();

                        if(dataCarpetas.isEmpty())
                        {
                            empty_view.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            empty_view.setVisibility(View.INVISIBLE);
                        }
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        adapter.setOnItemClickListener(new CarpetasAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                CarpetasModels item = dataCarpetas.get(position);

                                Intent intent=new Intent(ListaCarpetasProveedor.this, ConfirmarDuaActivity.class);

                                intent.putExtra("carpeta", item);

                                startActivity(intent);
                            }

                            @Override
                            public void onPhoneClick(int position) {

                            }

                            @Override
                            public void onLocationClick(int position) {

                            }
                        });



                    } else {
                        waitingDialog.dismiss();

                        String error = response.body().Mensaje;
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getApplicationContext());
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
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getApplicationContext());
                    materialAlertDialogBuilder.setTitle(R.string.login);
                    materialAlertDialogBuilder.setMessage("Error obteniendo carpetas. Intentelo nuevamente mas tarde.");
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
               /*aitingDialog.dismiss();
                String error = t.getMessage();

                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getApplicationContext());
                materialAlertDialogBuilder.setTitle(R.string.error);
                materialAlertDialogBuilder.setMessage(error);
                materialAlertDialogBuilder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                materialAlertDialogBuilder.show();*/
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        if(adapter!=null)
        {
            adapter.notifyDataSetChanged(); // notify adapter
            CargarCarpetas();
        }
       }

}