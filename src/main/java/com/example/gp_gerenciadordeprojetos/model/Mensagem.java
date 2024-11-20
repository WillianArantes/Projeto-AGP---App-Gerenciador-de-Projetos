package com.example.gp_gerenciadordeprojetos.model;

public class Mensagem {
    private int id;
    private String nomeUsuario;
    private String textoMensagem;
    private String dataEnvio;

    public Mensagem(int id, String nomeUsuario, String textoMensagem, String dataEnvio) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.textoMensagem = textoMensagem;
        this.dataEnvio = dataEnvio;
    }

    public Mensagem() {

    }

    public int getId() {
        return id;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getTextoMensagem() {
        return textoMensagem;
    }

    public void setTextoMensagem(String textoMensagem) {
        this.textoMensagem = textoMensagem;
    }
}
