package com.sm.integramovil.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sm.integramovil.R;
import com.sm.integramovil.models.ProveedorModels;
import com.sm.integramovil.ui.home.RecepcionCarpetasFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProveedoresAdapter extends RecyclerView.Adapter<ProveedoresAdapter.MyViewHolder>  implements Filterable {


    public android.app.AlertDialog waitingDialog;
    private ArrayList<ProveedorModels> dataSet;
    private ArrayList<ProveedorModels> dataSetAll;
    private OnItemClickListener mListener;

    private Context context;
    private SharedPreferences preferences;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onPhoneClick(int position);
        void onLocationClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombreProveedor, txtCodigoCarpeta, txtCantidadCarpetas;

        public MyViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.txtNombreProveedor = (TextView) itemView.findViewById(R.id.txtNombreProveedor);
            this.txtCodigoCarpeta = (TextView) itemView.findViewById(R.id.txtCodigoCarpeta);
            this.txtCantidadCarpetas = (TextView) itemView.findViewById(R.id.txtCantidadCarpetas);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


    }

    public ProveedoresAdapter(ArrayList<ProveedorModels> data) {
        this.dataSet = data;
        dataSetAll = new ArrayList<>();
        dataSetAll.addAll(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_proveedores_item, parent, false);
        view.setOnClickListener(RecepcionCarpetasFragment.myOnClickListener);
        ProveedoresAdapter.MyViewHolder myViewHolder = new ProveedoresAdapter.MyViewHolder(view,mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProveedoresAdapter.MyViewHolder holder, final int listPosition) {

        TextView txtNombreProveedor= holder.txtNombreProveedor;
        TextView txtCodigoCarpeta= holder.txtCodigoCarpeta;
        TextView txtCantidadCarpetas= holder.txtCantidadCarpetas;

        txtNombreProveedor.setText(String.valueOf(dataSet.get(listPosition).Nombre));
        txtCodigoCarpeta.setText(String.valueOf(dataSet.get(listPosition).Codigo));
        txtCantidadCarpetas.setText(String.valueOf(dataSet.get(listPosition).CantidadCarpetas));

    }



    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public Filter getFilter() {

        return myFilter;
    }

    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<ProveedorModels> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(dataSetAll);
            } else {
                for (ProveedorModels item: dataSetAll) {
                    if (item.Nombre.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataSet.clear();
            dataSet.addAll((Collection<? extends ProveedorModels>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
