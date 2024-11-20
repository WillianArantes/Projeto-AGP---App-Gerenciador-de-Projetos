package com.example.gp_gerenciadordeprojetos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gp_gerenciadordeprojetos.R;
import com.example.gp_gerenciadordeprojetos.controller.ProjetoController;
import com.example.gp_gerenciadordeprojetos.model.Projeto;

import java.util.ArrayList;

public class ProjetoListAdapter extends
        RecyclerView.Adapter<ProjetoListAdapter.ViewHolder> {

    private ArrayList<Projeto> listaProjetos;
    private Context context;
    ProjetoController controller;

    public ProjetoListAdapter(ArrayList<Projeto> listaProjetos, Context context) {
        this.listaProjetos = listaProjetos;
        this.context = context;
    }

    /**
     * Método responsável em carregar o arquivo xml do layout para
     * cada elemento da lista
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemList = inflater.inflate(R.layout.item_list_projeto,
                parent, false);

        return new ViewHolder(itemList);
    }
    /**
     * Método que adiciona os dados do Projeto na tela
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Projeto projeto = listaProjetos.get(position);

        holder.tvNome.setText(projeto.getNome());
        holder.tvFase.setText(projeto.getFase());
        holder.tvDataFinal.setText(projeto.getDataFinal());
    }
    /**
     * Determina a quantidade de elementos na lista
     * @return
     */
    @Override
    public int getItemCount() {

        return listaProjetos.size();
    }

    /**Clase que vincula os componentes do xml
     * para cada posiçao da lista*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNome;
        public TextView tvFase;
        public TextView tvDataFinal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNome = itemView.findViewById(R.id.tvNome);
            this.tvFase = itemView.findViewById(R.id.tvFase);
            this.tvDataFinal = itemView.findViewById(R.id.tvDataFinal);
        }
    }
    public void removeProjeto(int position) {
        if (position >= 0 && position < listaProjetos.size()) {
            listaProjetos.remove(position);
            notifyItemRemoved(position);
        }

    }
}