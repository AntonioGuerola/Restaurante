package org.example.Model.Test.Comanda;

import org.example.Model.DAO.ComandaDAO;
import org.example.Model.Entity.Comanda;

import java.sql.SQLException;

public class deleteComanda {
    public static void main(String[] args) throws SQLException {
        ComandaDAO cestaDAO = new ComandaDAO();
        Comanda comanda = (Comanda) cestaDAO.findById(1);
        System.out.println(cestaDAO.delete(comanda));
    }
}