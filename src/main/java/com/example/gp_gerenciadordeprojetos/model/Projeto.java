package com.example.gp_gerenciadordeprojetos.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Projeto implements Parcelable {

    private int matricula;
    private String nome;
    private String descricao;
    private String dataInicio;
    private String dataFinal;
    private String fase;


    public Projeto() {
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    protected Projeto(Parcel in) {
        matricula = Integer.parseInt(in.readString());
        nome = in.readString();
        descricao = in.readString();
        dataInicio = in.readString();
        dataFinal = in.readString();
        fase = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(matricula));
        dest.writeString(nome);
        dest.writeString(descricao);
        dest.writeString(dataInicio);
        dest.writeString(dataFinal);
        dest.writeString(fase);
    }
    public static final Creator<Projeto> CREATOR = new Creator<Projeto>() {
        @Override
        public Projeto createFromParcel(Parcel in) {
            return new Projeto(in);
        }

        @Override
        public Projeto[] newArray(int size) {
            return new Projeto[size];
        }
    };

}