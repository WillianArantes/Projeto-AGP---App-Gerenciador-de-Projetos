package com.example.gp_gerenciadordeprojetos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gp_gerenciadordeprojetos.helper.SQLiteDataHelper;
import com.example.gp_gerenciadordeprojetos.model.Mensagem;
import com.example.gp_gerenciadordeprojetos.model.Projeto;

import java.util.ArrayList;

public class MensagemDao implements IGenericDao<Mensagem> {
    //abrir conexao
    private SQLiteOpenHelper openHelper;

    private SQLiteDatabase baseDados;

    //Nome das colunas da Tabela
    private String[] colunas = {"id","NOMEUSUARIO", "TEXTOMENSAGEM", "DATAENVIO"};

    //Nome da tabela
    private String tabela = "MENSAGEM";

    //View que chamara essa classe
    private Context context;

    private static MensagemDao instancia;

    public static MensagemDao getInstancia(Context context){
        if(instancia == null){
            instancia = new MensagemDao(context);
            return instancia;
        }else{
            return instancia;
        }
    }

    public MensagemDao(Context context) {
        this.context = context;

        //Abrir a conexão com a base de dados
        openHelper = new SQLiteDataHelper(this.context,
                "BD_GPAPP", null, 1);

        baseDados = openHelper.getWritableDatabase();
    }
    @Override
    public long insert(Mensagem obj) {
        try{
            ContentValues valores = new ContentValues();

            valores.put(colunas[0], obj.getId());
            valores.put(colunas[1], obj.getNomeUsuario());
            valores.put(colunas[2], obj.getTextoMensagem());
            valores.put(colunas[3], obj.getDataEnvio());

            return baseDados.insert(tabela,
                    null, valores);

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: MensagemDao.insert(): "+ex.getMessage());
        }
        return 0;
    }


    @Override
    public long delete(Mensagem obj) {
        try{

            String[]identificador = {String.valueOf(obj.getId())};

            return baseDados.delete(tabela,
                    colunas[0]+" = ?", identificador);

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: MensagemDao.delete(): "+ex.getMessage());
        }
        return 0;
    }

    @Override
    public long update(Mensagem obj) {
        return 0;
    }

    @Override
    public Mensagem getById(int id) {
        try{
            String[]identificador = {String.valueOf(id)};
            Cursor cursor = baseDados.query(tabela,
                    colunas, colunas[0]+" = ?",
                    identificador, null,
                    null, null);

            //verifica se retornou dados da tabela
            if(cursor.moveToFirst()){
                Mensagem mensagem = new Mensagem();
                mensagem.setId(cursor.getInt(0));
                mensagem.setNomeUsuario(cursor.getString(1));
                mensagem.setTextoMensagem(cursor.getString(2));
                mensagem.setDataEnvio(cursor.getString(3));

                return mensagem;
            }


        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: MensagemDao.getById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Mensagem> getAll() {
        ArrayList<Mensagem> lista = new ArrayList<>();
        try{
            Cursor cursor = baseDados.query(tabela,
                    colunas, null, null,
                    null, null, colunas[0]);

            Log.d("GPAPP", "Número de registros encontrados: " + cursor.getCount());

            if(cursor.moveToFirst()){
                do{
                    Mensagem mensagem = new Mensagem();
                    mensagem.setId(cursor.getInt(0));
                    mensagem.setNomeUsuario(cursor.getString(1));
                    mensagem.setTextoMensagem(cursor.getString(2));
                    mensagem.setDataEnvio(cursor.getString(3));


                    lista.add(mensagem);
                }while (cursor.moveToNext());
            }
            cursor.close();
            return lista;

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: Mensagem.getAll(): "+ex.getMessage());
        }
        return lista;
    }
}
