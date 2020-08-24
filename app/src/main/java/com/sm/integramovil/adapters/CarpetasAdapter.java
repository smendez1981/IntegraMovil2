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
import com.sm.integramovil.models.CarpetasModels;
import com.sm.integramovil.models.ProveedorModels;
import com.sm.integramovil.ui.home.RecepcionCarpetasFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarpetasAdapter extends RecyclerView.Adapter<CarpetasAdapter.MyViewHolder>  implements Filterable {

    public android.app.AlertDialog waitingDialog;
    private ArrayList<CarpetasModels> dataSet;
    private ArrayList<CarpetasModels> dataSetAll;
    private CarpetasAdapter.OnItemClickListener mListener;

    private Context context;
    private SharedPreferences preferences;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onPhoneClick(int position);
        void onLocationClick(int position);
    }

    public void setOnItemClickListener(CarpetasAdapter.OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCodigoCarpetaCard, txtDescripcionCarpetaCard;

        public MyViewHolder(View itemView, final CarpetasAdapter.OnItemClickListener listener) {
            super(itemView);
            this.txtCodigoCarpetaCard = (TextView) itemView.findViewById(R.id.txtCodigoCarpetaCard);
            this.txtDescripcionCarpetaCard = (TextView) itemView.findViewById(R.id.txtDescripcionCarpetaCard);

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

    public CarpetasAdapter(ArrayList<CarpetasModels> data) {
        this.dataSet = data;
        dataSetAll = new ArrayList<>();
        dataSetAll.addAll(data);
    }

    @Override
    public CarpetasAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_carpeta_item, parent, false);
        view.setOnClickListener(RecepcionCarpetasFragment.myOnClickListener);
        CarpetasAdapter.MyViewHolder myViewHolder = new CarpetasAdapter.MyViewHolder(view,mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CarpetasAdapter.MyViewHolder holder, final int listPosition) {

        TextView txtCodigoCarpetaCard= holder.txtCodigoCarpetaCard;
        TextView txtDescripcionCarpetaCard= holder.txtDescripcionCarpetaCard;

        txtCodigoCarpetaCard.setText(String.valueOf(dataSet.get(listPosition).Numero));
        txtDescripcionCarpetaCard.setText(String.valueOf(dataSet.get(listPosition).Descripcion));

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

            List<CarpetasModels> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(dataSetAll);
            } else {
                for (CarpetasModels item: dataSetAll) {
                    if (item.Descripcion.toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            dataSet.addAll((Collection<? extends CarpetasModels>) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
