package com.example.gp_gerenciadordeprojetos.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gp_gerenciadordeprojetos.R;
import com.example.gp_gerenciadordeprojetos.model.Projeto;

public class ProjetoPesquisadoActivity extends AppCompatActivity {

    private TextView tvRetMatricula;
    private TextView tvRetNome;
    private TextView tvRetDataInicio;
    private TextView tvRetDataFinal;
    private TextView tvRetFase;
    private TextView tvRetDescricao;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_projeto_pesquisado);

        tvRetDataFinal = findViewById(R.id.tvRetDataFinal);
        tvRetNome = findViewById(R.id.tvRetNome);
        tvRetFase = findViewById(R.id.tvRetFase);
        tvRetDataInicio = findViewById(R.id.tvRetDataInicio);
        tvRetMatricula = findViewById(R.id.tvRetMatricula);
        tvRetDescricao = findViewById(R.id.tvRetDescricao);


       try {
           Projeto projeto = (Projeto) getIntent().getParcelableExtra("projeto");

           tvRetMatricula.setText(String.valueOf(projeto.getMatricula()));
           tvRetNome.setText(projeto.getNome());
           tvRetDescricao.setText(projeto.getDescricao());
           tvRetDataInicio.setText(projeto.getDataInicio());
           tvRetDataFinal.setText(projeto.getDataFinal());
           tvRetFase.setText(projeto.getFase());
       }catch (Exception ex){
           Toast.makeText(this,"Erro, projeto não carregado", Toast.LENGTH_LONG).show();
       }
    }
}