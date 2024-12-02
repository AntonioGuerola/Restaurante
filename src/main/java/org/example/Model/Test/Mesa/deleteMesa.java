package org.example.Model.Test.Mesa;

import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;

import java.sql.SQLException;

public class deleteMesa {
    public static void main(String[] args) throws SQLException {
        MesaDAO mesaDAO = new MesaDAO();
        Mesa mesaToDelete = mesaDAO.findById(5);
        System.out.println(mesaDAO.delete(mesaToDelete));
    }
}