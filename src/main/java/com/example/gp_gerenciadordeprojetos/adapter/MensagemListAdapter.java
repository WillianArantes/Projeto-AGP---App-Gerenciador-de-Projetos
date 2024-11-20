package com.example.gp_gerenciadordeprojetos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gp_gerenciadordeprojetos.R;
import com.example.gp_gerenciadordeprojetos.dao.MensagemDao;
import com.example.gp_gerenciadordeprojetos.model.Mensagem;

import java.util.ArrayList;

public class MensagemListAdapter extends
        RecyclerView.Adapter<MensagemListAdapter.ViewHolder> {
    private ArrayList<Mensagem> listaMensagens;
    private Context context;

    public MensagemListAdapter(ArrayList<Mensagem> listaMensagens, Context context) {
        this.listaMensagens = listaMensagens;
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
        View itemList = inflater.inflate(R.layout.item_list_mensagem,
                parent, false);

        return new ViewHolder(itemList);
    }
    /**
     * Método que adiciona os dados do Projeto na tela
     */
    @Override
    public void onBindViewHolder(@NonNull MensagemListAdapter.ViewHolder holder, int position) {
        Mensagem mensagem = listaMensagens.get(position);

        holder.tvIdMensagem.setText(String.valueOf(mensagem.getId()));
        holder.tvUsuarioMensagem.setText(mensagem.getNomeUsuario());
        holder.tvDataEnvio.setText(mensagem.getDataEnvio());
        holder.tvTextoMensagem.setText(mensagem.getTextoMensagem());
    }
    /**
     * Determina a quantidade de elementos na lista
     * @return
     */
    @Override
    public int getItemCount() {

        return listaMensagens.size();
    }

    /**Clase que vincula os componentes do xml
     * para cada posiçao da lista*/
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvUsuarioMensagem;
        public TextView tvTextoMensagem;
        public TextView tvDataEnvio;
        private TextView tvIdMensagem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvIdMensagem = itemView.findViewById(R.id.tvIdMensagem);
            this.tvUsuarioMensagem = itemView.findViewById(R.id.tvUsuarioMensagem);
            this.tvTextoMensagem = itemView.findViewById(R.id.tvTextoMensagem);
            this.tvDataEnvio = itemView.findViewById(R.id.tvDataEnvio);
        }
    }
}
