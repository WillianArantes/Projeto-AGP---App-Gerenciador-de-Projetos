package com.example.gp_gerenciadordeprojetos.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gp_gerenciadordeprojetos.R;
import com.example.gp_gerenciadordeprojetos.adapter.ProjetoListAdapter;
import com.example.gp_gerenciadordeprojetos.controller.ProjetoController;
import com.example.gp_gerenciadordeprojetos.model.Projeto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton btCadastrarProjeto;
    private RecyclerView rvProjetos;
    private ProjetoController controller;
    private EditText edMatricula, edNomeProjeto, edDescricaoProjeto, edDataInicioProjeto
            ,edDatafinalProjeto,edFaseProjeto, edNumMatricula;
    private ImageButton btInbox, btPesquisarProjeto;
    private View viewAlert;
    private View viewCadastro, viewPesquisarProjeto;
    private AlertDialog dialog;
    private ProjetoListAdapter adapter;
    private ArrayList<Projeto> listaProjetos;


    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        edNomeProjeto = findViewById(R.id.edNomeProjeto);
        edDescricaoProjeto = findViewById(R.id.edDescricaoProjeto);
        edDataInicioProjeto = findViewById(R.id.edDataInicioProjeto);
        edDatafinalProjeto = findViewById(R.id.edDatafinalProjeto);
        edMatricula = findViewById(R.id.edMatricula);
        edFaseProjeto = findViewById(R.id.edFaseProjeto);

        controller = new ProjetoController(this);
        rvProjetos = findViewById(R.id.rvProjetos);
        btCadastrarProjeto = findViewById(R.id.btCadastrarProjeto);
        btPesquisarProjeto = findViewById(R.id.btPesquisarProjeto);
        btInbox = findViewById(R.id.btInbox);

        atualizaListaProjetos();

        listaProjetos = new ArrayList<>();
        listaProjetos = controller.retornarTodosProjetos();

        adapter = new ProjetoListAdapter(listaProjetos, this);
        rvProjetos.setAdapter(adapter);
        rvProjetos.setLayoutManager(new LinearLayoutManager(this));

        edNomeProjeto = findViewById(R.id.edNomeProjeto);

        if (listaProjetos != null && !listaProjetos.isEmpty()) {
            adapter = new ProjetoListAdapter(listaProjetos, MainActivity.this);
            rvProjetos.setAdapter(adapter);
        } else {
            // Exiba uma mensagem ou um layout alternativo (ex: "Nenhum projeto disponível")
            Toast.makeText(this,
                    "Nenhum projeto disponível", Toast.LENGTH_SHORT).show();
        }
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false; // aqui nao remove o item
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Chama o método removeProjeto() para remover o item da lista

                if(position>= 0 && position < listaProjetos.size()){
                    //remover o projeto da lista
                    Projeto projetoRemovido = listaProjetos.get(position);
                    listaProjetos.remove(position);
                    //remover projeto do bd
                    controller.removerProjetoBd(projetoRemovido);
                    //notificar o adaptador que foi removido
                    adapter.notifyItemRemoved(position);
                }else{
                    // Em caso de erro, você pode adicionar um log ou uma ação de fallback
                    Log.e("RecyclerView", "Posição inválida: " + position);
                }

                    //caso haja mais itens e a posição tenha mudado, motifique o adaptador
                if (position < listaProjetos.size()) {
                    adapter.notifyItemRangeChanged(position, listaProjetos.size());
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rvProjetos);

        btCadastrarProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirCadastro();
            }
        });
        btInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //utilizado para chamar nova activity
                Intent intent = new Intent(MainActivity.this, InboxActivity.class);
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
        atualizaListaProjetos();
    }

    public void pesquisarProjeto(){
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
                        String matriculaStr = edNumMatricula.getText().toString().trim();

                        if (matriculaStr.isEmpty() || !matriculaStr.matches("\\d+")) {
                            Toast.makeText(view.getContext(),
                                    "Por favor, insira uma matrícula válida.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int matricula = Integer.parseInt(matriculaStr);
                        Projeto projeto = buscarProjeto(matricula);

                        if (projeto == null) {
                            Toast.makeText(view.getContext(),
                                    "Projeto não encontrado.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(MainActivity.this,
                                ProjetoPesquisadoActivity.class);
                        intent.putExtra("projeto", projeto); // Adiciona o objeto Projeto à Intent
                        startActivity(intent);
                    }
                });
            }
        });
        dialog.show();
    }
    private Projeto buscarProjeto(int matricula) {
        Projeto projeto = null;
        try {
            projeto = controller.retornarProjetoPorId(matricula);

        } catch (Exception ex) {
            Toast.makeText(this,
                    "Erro ao carregar projeto",
                    Toast.LENGTH_LONG).show();
        }
        return projeto;
    }
    private void abrirCadastro(){
        //carrega o arquivo XML do layout de cadastro
        viewCadastro = getLayoutInflater()
                .inflate(R.layout.activity_cadastro_projeto, null);

        edNomeProjeto = viewCadastro.findViewById(R.id.edNomeProjeto);
        edDescricaoProjeto = viewCadastro.findViewById(R.id.edDescricaoProjeto);
        edDataInicioProjeto = viewCadastro.findViewById(R.id.edDataInicioProjeto);
        edDatafinalProjeto = viewCadastro.findViewById(R.id.edDatafinalProjeto);
        edMatricula = viewCadastro.findViewById(R.id.edMatricula);
        edFaseProjeto = viewCadastro.findViewById(R.id.edFaseProjeto);


        if (edNomeProjeto != null) {
            edNomeProjeto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    // Código para lidar com o evento de foco
                    if (hasFocus) {
                        // Por exemplo, aumenta a altura
                        edNomeProjeto.setHeight(400);
                    }
                }
            });
        }

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Cadastro de Projeto"); //seta o titulo do popup
        builder.setView(viewCadastro); // seta o layout no popup
        builder.setCancelable(false); //nao deixa fechar o popup ao clic

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
                        //Salvar o projeto
                        salvarDados();
                    }
                });
            }
        });
        dialog.show();
    }

    private void salvarDados() {
        String retorno = controller.salvarProjeto(
                edMatricula.getText().toString(),
                edNomeProjeto.getText().toString(),
                edDescricaoProjeto.getText().toString(),
                edDataInicioProjeto.getText().toString(),
                edDatafinalProjeto.getText().toString(),
                edFaseProjeto.getText().toString()
                );
        if(retorno.contains("MATRICULA")){
            edMatricula.setError(retorno);
            edMatricula.requestFocus();
        }
        if(retorno.contains("NOME")){
            edNomeProjeto.setError(retorno);
            edNomeProjeto.requestFocus();
        }
        if(retorno.contains("DESCRICAO")){
            edDescricaoProjeto.setError(retorno);
            edDescricaoProjeto.requestFocus();
        }

        if(retorno.contains("DATAINICIO")){
            edDataInicioProjeto.setError(retorno);
            edDataInicioProjeto.requestFocus();
        }
        if(retorno.contains("DATAFINAL")){
            edDatafinalProjeto.setError(retorno);
            edDatafinalProjeto.requestFocus();
        }
        if(retorno.contains("FASE")){
            edFaseProjeto.setError(retorno);
            edFaseProjeto.requestFocus();
        }
        Toast.makeText(this, retorno, Toast.LENGTH_SHORT).show();
        if(retorno.contains("sucesso")){
            atualizaListaProjetos();
            dialog.dismiss();  //Fecha a tela de cadastro
        }else if(retorno.contains("ERRO")){
            dialog.dismiss(); //Fecha a tela de cadastro
        }
    }

    private void atualizaListaProjetos(){
        try{
            ArrayList<Projeto> listaProjetos =
                    controller.retornarTodosProjetos();

            ProjetoListAdapter adapter =
                    new ProjetoListAdapter(listaProjetos, this);


            rvProjetos.setLayoutManager(
                    new LinearLayoutManager(this));

            rvProjetos.setAdapter(adapter);

        }catch (Exception ex){
            Toast.makeText(this,
                    "Erro ao carregar lista",
                    Toast.LENGTH_LONG).show();
        }
    }
}