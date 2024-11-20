package com.example.gp_gerenciadordeprojetos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.gp_gerenciadordeprojetos.helper.SQLiteDataHelper;
import com.example.gp_gerenciadordeprojetos.model.Projeto;

import java.util.ArrayList;

public class ProjetoDao implements IGenericDao<Projeto>{


    //Variavel responsável por abrir a conexão com o BD
    private SQLiteOpenHelper openHelper;

    //Base de Dados
    private SQLiteDatabase baseDados;

    //Nome das colunas da Tabela
    private String[] colunas = {"MATRICULA","NOME", "DESCRICAO", "DATAINICIO","DATAFINAL",
            "FASE"};

    //Nome da tabela
    private String tabela = "PROJETO";

    //View que chamara essa classe
    private Context context;

    private static ProjetoDao instancia;

    public static ProjetoDao getInstancia(Context context){
        if(instancia == null){
            instancia = new ProjetoDao(context);
            return instancia;
        }else{
            return instancia;
        }
    }

    public ProjetoDao(Context context) {
        this.context = context;

        //Abrir a conexão com a base de dados
        openHelper = new SQLiteDataHelper(this.context,
                "BD_GPAPP", null, 1);

        baseDados = openHelper.getWritableDatabase();
    }

    @Override
    public long insert(Projeto obj) {
        try{
            ContentValues valores = new ContentValues();

            valores.put(colunas[0], obj.getMatricula());
            valores.put(colunas[1], obj.getNome());
            valores.put(colunas[2], obj.getDescricao());
            valores.put(colunas[3], obj.getDataInicio());
            valores.put(colunas[4], obj.getDataFinal());
            valores.put(colunas[5], obj.getFase());

            return baseDados.insert(tabela,
                    null, valores);

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: ProjetoDapo.insert(): "+ex.getMessage());
        }
        return 0;
    }

    @Override
    public long update(Projeto obj) {
        try{
            ContentValues valores = new ContentValues();

            valores.put(colunas[1], obj.getNome());
            valores.put(colunas[2], obj.getDescricao());
            valores.put(colunas[3], obj.getDataInicio());
            valores.put(colunas[4], obj.getDataFinal());
            valores.put(colunas[5], obj.getFase());

            String[]identificador = {String.valueOf(obj.getMatricula())};

            return baseDados.update(tabela, valores,
                    colunas[0]+" = ?", identificador);

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: ProjetoDao.update(): "+ex.getMessage());
        }
        return 0;

    }

    @Override
    public long delete(Projeto obj) {
        try{

            String[]identificador = {String.valueOf(obj.getMatricula())};

            return baseDados.delete(tabela,
                    colunas[0]+" = ?", identificador);

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: ProjetoDao.delete(): "+ex.getMessage());
        }
        return 0;
    }



    @Override
    public Projeto getById(int id) {
        try{
            String[]identificador = {String.valueOf(id)};
            Cursor cursor = baseDados.query(tabela,
                    colunas, colunas[0]+" = ?",
                    identificador, null,
                    null, null);

            //verifica se retornou dados da tabela
            if(cursor.moveToFirst()){
                Projeto projeto = new Projeto();
                projeto.setMatricula(cursor.getInt(0));
                projeto.setNome(cursor.getString(1));
                projeto.setDescricao(cursor.getString(2));
                projeto.setDataInicio(cursor.getString(3));
                projeto.setDataFinal(cursor.getString(4));
                projeto.setFase(cursor.getString(5));

                return projeto;
            }


        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: ProjetoDao.getById(): "+ex.getMessage());
        }
        return null;
    }

    @Override
    public ArrayList<Projeto> getAll() {
        ArrayList<Projeto> lista = new ArrayList<>();
        try{
            Cursor cursor = baseDados.query(tabela,
                    colunas, null, null,
                    null, null, colunas[0]);

            Log.d("GPAPP", "Número de registros encontrados: " + cursor.getCount());

            if(cursor.moveToFirst()){
                do{
                    Projeto projeto = new Projeto();
                    projeto.setMatricula(cursor.getInt(0));
                    projeto.setNome(cursor.getString(1));
                    projeto.setDescricao(cursor.getString(2));
                    projeto.setDataInicio(cursor.getString(3));
                    projeto.setDataFinal(cursor.getString(4));
                    projeto.setFase(cursor.getString(5));

                    lista.add(projeto);
                }while (cursor.moveToNext());

            }
            cursor.close();
            return lista;

        }catch (SQLException ex){
            Log.e("GPAPP",
                    "ERRO: Projeto.getAll(): "+ex.getMessage());
        }
        return lista;
    }
}
