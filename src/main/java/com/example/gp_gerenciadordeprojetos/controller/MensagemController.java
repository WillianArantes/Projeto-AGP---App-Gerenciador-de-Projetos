package com.example.gp_gerenciadordeprojetos.controller;

import android.content.Context;
import android.database.SQLException;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.gp_gerenciadordeprojetos.dao.MensagemDao;
import com.example.gp_gerenciadordeprojetos.model.Mensagem;

import java.util.ArrayList;

public class MensagemController {
    private Context context;

    public MensagemController(Context context) {
        this.context = context;
    }

    public String salvarMensagem(String id, String nomeUsuario,String textoMensagem,
                                 String dataEnvio){
        try{
            //valida se os campos estão preenchidos
            if(TextUtils.isEmpty(id)){
                return "Informe o número de identificação da Mensagem.";
            }

            if(!id.matches("\\d+")){
                return "Informe um número de identificação válido.";
            }
            if(TextUtils.isEmpty(nomeUsuario)){
                return "Informe a nome do usuário da Mensagem.";
            }
            if(!nomeUsuario.matches("[a-zA-Z ]+")){
                return "Informe um nome com apenas letras.";
            }
            if(TextUtils.isEmpty(textoMensagem)){
                return  "Informe o texto da mensagem.";
            }
            if(TextUtils.isEmpty(dataEnvio)){
                return "Informe a data de envio da Mensagem.";
            }
            if(!dataEnvio.matches("\\d{2}/\\d{2}/\\d{4}")){
                return "Informe uma data válida no formato dd/MM/yyyy.";
            }

            //verificar se já existe a id da mensagem cadastrada
            Mensagem mensagem = MensagemDao.getInstancia(context)
                    .getById(Integer.parseInt(id));

            if(mensagem != null) {
                return "A Mensagem (" + id + ") já está cadastrada!";

            }else{
                mensagem = new Mensagem();
                mensagem.setId(Integer.parseInt(id));
                mensagem.setNomeUsuario(nomeUsuario);
                mensagem.setTextoMensagem(textoMensagem);
                mensagem.setDataEnvio(dataEnvio);

                long idMensagem = MensagemDao.getInstancia(context).insert(mensagem);

                if(Integer.parseInt(id) > 0 ){
                    return  "Mensagem enviada com Sucesso.";
                }else{

                    return "Erro ao gravar dados da mensagem na BD.";
                }
            }
        }catch (SQLException ex){
            Toast.makeText(context, "Erro ao gravar dados da Mensagem." +ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        catch (Exception ex){
            Toast.makeText(context, "Erro inesperado ao gravar dados da Mensagem." +
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    /**
     * Retorna todos as mensagem cadastrados no banco
     * @return
     *
     */

    public ArrayList<Mensagem> retornarTodasMensagens(){

        return  MensagemDao.getInstancia(context).getAll();
    }
    public void removerMensagemBd(Mensagem mensagem){
        MensagemDao.getInstancia(context).delete(mensagem);
        Toast.makeText(context,"Mensagem de " + mensagem.getNomeUsuario() +
                        " removida com sucesso!", Toast.LENGTH_SHORT).show();
    }
}
