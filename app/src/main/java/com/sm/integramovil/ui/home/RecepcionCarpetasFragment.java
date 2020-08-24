package com.sm.integramovil.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.sm.integramovil.ListaCarpetasProveedor;
import com.sm.integramovil.R;
import com.sm.integramovil.adapters.ProveedoresAdapter;
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.models.ProveedorModels;
import com.sm.integramovil.responses.CarpetasResponse;
import com.sm.integramovil.services.ApiEndpointInterface;
import com.sm.integramovil.services.UtilesServices;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecepcionCarpetasFragment extends Fragment {


    public android.app.AlertDialog waitingDialog;
    public static View.OnClickListener myOnClickListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static ProveedoresAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private TextView empty_view;
    private static ArrayList<ProveedorModels> data;
    private static ArrayList<CarpetasModels> dataCarpetas;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recepcioncarpetas, container, false);
        context = root.getContext();

        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view_carpetas);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());

        adapter=null;
        empty_view=(TextView) root.findViewById(R.id.empty_view);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.accent);

        getActivity().setTitle(getString(R.string.recepcion_carpetas));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CargarCarpetas();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        CargarCarpetas();



        return root;
    }




    public void CargarCarpetas() {


        waitingDialog = new
                SpotsDialog.Builder().setContext(getContext()).build();
        waitingDialog.setTitle("Uploading Post");
        waitingDialog.setMessage(getString(R.string.espere_por_favor));
        waitingDialog.show();

        ApiEndpointInterface mAPIService = UtilesServices.getAPIService(getContext());

        Call<CarpetasResponse> call = mAPIService.listaCarpetas();

        call.enqueue(new Callback<CarpetasResponse>() {
            @Override
            public void onResponse(Call<CarpetasResponse> call, Response<CarpetasResponse> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Log.i("TAG", "post submitted to API." + response.body().toString());

                    if (response.body().Status.equals("OK")) {

                        waitingDialog.dismiss();

                        data =new ArrayList<>();

                        dataCarpetas=response.body().Carpetas;

                        for (CarpetasModels carpeta: response.body().Carpetas) {

                            Boolean existe=false;

                            for(ProveedorModels proveedor: data)
                            {
                                if(proveedor.Codigo == carpeta.CodigoProveedor)
                                {
                                    existe = true;
                                }
                            }

                            if(!existe)
                            {
                                ProveedorModels prv=new ProveedorModels();
                                prv.Codigo=carpeta.CodigoProveedor;
                                prv.Nombre=carpeta.NombreProveedor;

                                int cantCarpetas=0;
                                for (CarpetasModels carpetainterna: response.body().Carpetas)
                                {
                                    if(carpetainterna.CodigoProveedor ==  prv.Codigo)
                                    {
                                        cantCarpetas++;
                                    }
                                }
                                prv.CantidadCarpetas=cantCarpetas;

                                data.add(prv);
                            }
                        }

                        adapter = new ProveedoresAdapter(data);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);

                        if(data.isEmpty())
                        {
                            empty_view.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            empty_view.setVisibility(View.INVISIBLE);
                        }
                        recyclerView.setItemAnimator(new DefaultItemAnimator());

                        adapter.setOnItemClickListener(new ProveedoresAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                ProveedorModels item = data.get(position);

                                ArrayList<CarpetasModels> listaCarpetas=new ArrayList<CarpetasModels>();

                                for(CarpetasModels carpeta : dataCarpetas)
                                {
                                    if(carpeta.CodigoProveedor==item.Codigo)
                                    {
                                        listaCarpetas.add(carpeta);
                                    }
                                }

                                Intent intent=new Intent(context, ListaCarpetasProveedor.class);

                                intent.putExtra("codigoProveedor", item.Codigo);

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
                        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
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
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
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
                waitingDialog.dismiss();
                String error = t.getMessage();
                       /* Toast toast=Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT);
                        toast.setMargin(50,50);
                        toast.show();*/
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getContext());
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
