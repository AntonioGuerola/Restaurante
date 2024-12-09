package org.example.Model.Test.Mesa;

import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;

import java.sql.SQLException;

public class updateTiempo {
    public static void main(String[] args) throws SQLException {
        MesaDAO mesaDAO = new MesaDAO();
        Mesa mesa = mesaDAO.findById(94);

        mesaDAO.actualizarTiempo(mesa.getId(), 120);
    }
}
