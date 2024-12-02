package org.example.Model.Test.Mesa;

import org.example.Model.DAO.MesaDAO;
import org.example.Model.Entity.Mesa;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.example.Model.Entity.TipoMesa.TERRAZA;

public class InsertMesa {
    public static void main(String[] args) throws SQLException {
        LocalDate fechaActual1 = LocalDate.now();
        LocalTime horaActual1 = LocalTime.now();

        String fechaString1 = fechaActual1.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String horaString1 = horaActual1.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        Mesa mesa1 = new Mesa(TERRAZA, 2, fechaString1, horaString1);

//        LocalDate fechaActual2 = LocalDate.now();
//        LocalTime horaActual2 = LocalTime.now();
//
//        String fechaString2 = fechaActual2.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//        String horaString2 = horaActual2.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
//        Mesa mesa2 = new Mesa(CAFETERIA, 6, fechaString2, horaString2);
//
          MesaDAO mesaDAO = new MesaDAO();
          System.out.println(mesaDAO.save(mesa1));
//        System.out.println(mesaDAO.save(mesa2));

    }
}