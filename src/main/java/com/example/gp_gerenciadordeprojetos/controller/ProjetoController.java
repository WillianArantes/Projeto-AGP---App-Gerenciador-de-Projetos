package com.example.gp_gerenciadordeprojetos.controller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.IntegerRes;

import com.example.gp_gerenciadordeprojetos.dao.ProjetoDao;
import com.example.gp_gerenciadordeprojetos.model.Projeto;

import java.util.ArrayList;

public class ProjetoController {

    private Context context;

    public ProjetoController(Context context) {
        this.context = context;
    }

    public String salvarProjeto(String matricula, String nome,String descricao, String dataInicio,
                                String dataFinal, String fase){
        try{
            //valida se os campos estão preenchidos
            if(TextUtils.isEmpty(nome)){
                return "Informe o Nome do Projeto.";
            }
            if(TextUtils.isEmpty(descricao)){
                return "Informe a Descrição do Projeto.";
            }
            if(TextUtils.isEmpty(dataInicio)){
                return "Informe a Data de Inicio do Projeto.";
            }
            if (!dataInicio.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return "Informe uma data válida no formato dd/MM/yyyy.";
            }
            if(TextUtils.isEmpty(dataFinal)){
                return "Informe a Data Final do Projeto.";
            }
            if (!dataFinal.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return "Informe uma data válida no formato dd/MM/yyyy.";
            }
            if(TextUtils.isEmpty(String.valueOf(matricula))){
                return "Informe o Numero de Matricula do Projeto.";
            }
            if(!matricula.matches("\\d+")){
                return "Informe um Numero de Matricula válido.";
            }
            if(TextUtils.isEmpty(fase)){
                return "Informe a Fase do Projeto.";
            }
            if(!fase.matches("[a-zA-Z]+")) {
                return "Informe a Fase com apenas letras.";
            }

            //verificar se já existe a matricula cadastrada
            Projeto projeto = ProjetoDao.getInstancia(context)
                    .getById(Integer.parseInt(matricula));

            if(projeto != null) {
                return "A Matricula (" + matricula + ") já está cadastrada!";
            }else{
                projeto = new Projeto();
                projeto.setMatricula(Integer.parseInt(matricula));
                projeto.setNome(nome);
                projeto.setDescricao(descricao);
                projeto.setDataInicio(dataInicio);
                projeto.setDataFinal(dataFinal);
                projeto.setFase(fase);

                long id = ProjetoDao.getInstancia(context).insert(projeto);

                if(Integer.parseInt(matricula) > 0 ){
                    return "Dados do Projeto gravados com sucesso.";
                }else{
                    return"Erro ao gravar dados do Projeto na BD.";
                }
            }
        }catch (Exception ex){
            return"Erro ao Gravar dados do Projeto." +
                    ex.getMessage();
        }
    }

    /**
     * Retorna todos os alunos cadastrados no banco
     * @return
     */
    public ArrayList<Projeto> retornarTodosProjetos(){

      return  ProjetoDao.getInstancia(context).getAll();
    }
    public Projeto retornarProjetoPorId(int id){

     return ProjetoDao.getInstancia(context).getById(id);
    }
    public void removerProjetoBd(Projeto projeto){
        ProjetoDao.getInstancia(context).delete(projeto);
        Toast.makeText(context,"Projeto " + projeto.getNome() + " removido com sucesso!",
                Toast.LENGTH_SHORT).show();
    }
}

