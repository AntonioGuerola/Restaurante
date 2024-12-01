package Model.Test.Comanda;

import Model.DAO.ComandaDAO;
import Model.Entity.Comanda;

import java.sql.SQLException;

public class deleteComanda {
    public static void main(String[] args) throws SQLException {
        ComandaDAO cestaDAO = new ComandaDAO();
        Comanda comanda = (Comanda) cestaDAO.findById(1);
        System.out.println(cestaDAO.delete(comanda));
    }
}