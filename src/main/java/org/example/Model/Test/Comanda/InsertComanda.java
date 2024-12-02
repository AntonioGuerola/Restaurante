package org.example.Model.Test.Comanda;

import org.example.Model.DAO.ComandaDAO;
import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Comanda;
import org.example.Model.Entity.Mesa;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class InsertComanda {
    public static void main(String[] args) throws SQLException {
        MesaDAO mesaDAO = new MesaDAO();
        Mesa mesaAInsertarCesta = mesaDAO.findById(2);
        LocalTime horaActual1 = LocalTime.now();

        String horaString1 = horaActual1.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Comanda comanda = new Comanda(horaString1, mesaAInsertarCesta);

        ComandaDAO cestaDAO = new ComandaDAO();
        System.out.println(cestaDAO.save(comanda));
    }
}