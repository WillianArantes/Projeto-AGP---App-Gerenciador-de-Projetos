package com.example.gp_gerenciadordeprojetos.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteDataHelper  extends SQLiteOpenHelper {
    public SQLiteDataHelper(@Nullable Context context,
                            @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Criação da tabela Projeto
        db.execSQL("CREATE TABLE PROJETO(" +
                "MATRICULA INTEGER PRIMARY KEY," +
                "NOME VARCHAR(100), " +
                "DESCRICAO VARCHAR(200)," +
                "DATAINICIO VARCHAR(15)," +
                "DATAFINAL VARCHAR(15), " +
                "FASE VARCHAR(15))");

        db.execSQL("CREATE TABLE MENSAGEM(" +
                "ID INTEGER PRIMARY KEY," +
                "NOMEUSUARIO VARCHAR(100), " +
                "TEXTOMENSAGEM VARCHAR(1000)," +
                "DATAENVIO VARCHAR(15))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PROJETO");
        db.execSQL("DROP TABLE IF EXISTS MENSAGEM");
        onCreate(db);
    }
}
