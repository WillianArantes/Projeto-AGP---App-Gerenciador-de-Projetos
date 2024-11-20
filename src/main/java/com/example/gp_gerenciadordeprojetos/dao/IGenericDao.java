package com.example.gp_gerenciadordeprojetos.dao;

import com.example.gp_gerenciadordeprojetos.model.Projeto;

import java.util.ArrayList;

public interface IGenericDao<Objeto> {

    long insert(Objeto obj);
    long update(Objeto obj);
    long delete(Objeto obj);
    Objeto getById(int id);
    ArrayList<Objeto> getAll();

}
