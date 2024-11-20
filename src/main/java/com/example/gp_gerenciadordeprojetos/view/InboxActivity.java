package com.example.gp_gerenciadordeprojetos.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gp_gerenciadordeprojetos.R;
import com.example.gp_gerenciadordeprojetos.adapter.MensagemListAdapter;
import com.example.gp_gerenciadordeprojetos.controller.MensagemController;
import com.example.gp_gerenciadordeprojetos.model.Mensagem;

import java.util.ArrayList;

public class InboxActivity extends AppCompatActivity {
    private ImageButton btNovaMensagem,btMainProjetos;
    private View viewNovaMensagem, viewPesquisarProjeto;
    private View viewAlert;
    private AlertDialog dialog;
    private MensagemController controller;
    private EditText edId, edNomeUsuario, edDataEnvio, edTextoMensagem, edNumMatricula;
    private RecyclerView rvMensagens;
    private ImageButton btPesquisarProjeto;
    private ArrayList<Mensagem> listaMensagens;
    private MensagemListAdapter adapter;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbox);

        controller = new MensagemController(this);
        rvMensagens = findViewById(R.id.rvMensagens);
        btNovaMensagem = findViewById(R.id.btNovaMensagem);
        btMainProjetos = findViewById(R.id.btMainProjetos);
        btPesquisarProjeto = findViewById(R.id.btPesquisarProjeto);
        listaMensagens = new ArrayList<>();
        listaMensagens = controller.retornarTodasMensagens();

        atualizaListaMensagens();

        //verificar se a lista de mensagens está vazia ou nula
        if(listaMensagens != null && listaMensagens.isEmpty()){
            adapter = new MensagemListAdapter(listaMensagens, this);
            rvMensagens.setAdapter(adapter);
            Toast.makeText(this,
                    "Nenhuma mensagem disponível", Toast.LENGTH_SHORT).show();
        }else{
            adapter = new MensagemListAdapter(listaMensagens, this);
            rvMensagens.setAdapter(adapter);
        }

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull
            RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Chama o método removeMensagen() para remover o item da lista
                if(position>= 0 && position < listaMensagens.size()){
                    //remover a mensagem da lista
                    Mensagem mensagemRemovida = listaMensagens.get(position);
                    listaMensagens.remove(position);
                    //remover a mensagem do bd
                    controller.removerMensagemBd(mensagemRemovida);
                    //notificar o adaptador que foi removido
                    adapter.notifyItemRemoved(position);
                }else{
                    // Em caso de erro, você pode adicionar um log ou uma ação de fallback
                    Log.e("RecyclerView", "Posição inválida: " + position);

                }
                //caso haja mais itens e a posição tenha mudado, motifique o adaptador
                if (position < listaMensagens.size()) {
                    adapter.notifyItemRangeChanged(position, listaMensagens.size());
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rvMensagens);

        btNovaMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarNovaMensagem();
            }
        });
        btMainProjetos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxActivity.this,
                        MainActivity.class);

                startActivity(intent);
            }
        });
        btPesquisarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesquisarProjeto();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaListaMensagens();
    }
    private void pesquisarProjeto(){
        viewPesquisarProjeto  = getLayoutInflater().inflate(R.layout.activity_pesquisar_projeto,
                null);

        edNumMatricula = viewPesquisarProjeto.findViewById(R.id.edNumMatricula);

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Pesquisar Projeto por Mátricula"); //seta o titulo do popup
        builder.setView(viewPesquisarProjeto); // seta o layout no popup
        builder.setCancelable(false); //nao deixa fechar o popup ao clic

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialog.dismiss();
            }
        } );

        builder.setPositiveButton("Pesquisar", null);
        dialog = builder.create(); //cria o popup em memoria

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btPesquisar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btPesquisar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Salvar o projeto
                        Intent intent = new Intent(InboxActivity.this,
                                ProjetoPesquisadoActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        dialog.show();
    }
    private void enviarNovaMensagem(){
        //carrega o arquivo XML do layout de cadastro
        viewNovaMensagem = getLayoutInflater()
                .inflate(R.layout.diaalog_nova_mensagem, null);

        edId = viewNovaMensagem.findViewById(R.id.edId);
        edNomeUsuario = viewNovaMensagem.findViewById(R.id.edNomeUsuario);
        edTextoMensagem = viewNovaMensagem.findViewById(R.id.edTextoMensagem);
        edDataEnvio = viewNovaMensagem.findViewById(R.id.edDataEnvio);


        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Nova Mensagem");
        builder.setView(viewNovaMensagem);
        builder.setCancelable(false);

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialog.dismiss();
            }
        } );
        builder.setPositiveButton("Salvar", null);
        dialog = builder.create(); //cria o popup em memoria

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btSalvar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Salvar a mensagem
                        salvarDados();
                    }
                });
            }
        });
        dialog.show();
    }
    private void salvarDados(){
        String retorno = controller.salvarMensagem(
                edId.getText().toString(),
                edNomeUsuario.getText().toString(),
                edTextoMensagem.getText().toString(),
                edDataEnvio.getText().toString()
                );

        if(retorno.contains("ID")){
            edId.setError(retorno);
            edId.requestFocus();
        }
        if(retorno.contains("NOMEUSUARIO")){
            edNomeUsuario.setError(retorno);
            edNomeUsuario.requestFocus();
        }
        if(retorno.contains("TEXTOMENSAGEM")){
            edTextoMensagem.setError(retorno);
            edTextoMensagem.requestFocus();
        }
        if(retorno.contains("DATAENVIO")){
            edDataEnvio.setError(retorno);
            edDataEnvio.requestFocus();
        }
        Toast.makeText(this, retorno, Toast.LENGTH_SHORT).show();
        if(retorno.contains("Sucesso")){
            atualizaListaMensagens();
            dialog.dismiss();  //Fecha a tela de cadastro
        }else if(retorno.contains("ERRO")){
            dialog.dismiss(); //Fecha a tela de cadastro
        }
    }
    private void atualizaListaMensagens(){
        try{
            ArrayList<Mensagem> listaMensagens =
                    controller.retornarTodasMensagens();

            MensagemListAdapter adapter =
                    new MensagemListAdapter(listaMensagens,this);


            rvMensagens.setLayoutManager(
                    new LinearLayoutManager(this));

            rvMensagens.setAdapter(adapter);

        }catch (Exception ex){
            Toast.makeText(this,
                    "Erro ao carregar lista",
                    Toast.LENGTH_LONG).show();
        }
    }
}