package org.example;

import java.sql.*;

public class ConexionBasica {
    private static final String URL = "jdbc:mysql://localhost:3306/colegio";
    private static final String USER = "root";
    private static final String PASS = "";

    public static void main(String[] args) {
        Connection con = null;
        ResultSet resultSet = null;

        try {
            //1. Establecer conexión
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión exitosa");

            //2. Crear objeto Statement
            Statement statement = con.createStatement();


            //3. Escribimos la consulta que queramos
            String query = "SELECT * FROM alumno";
            resultSet = statement.executeQuery(query);

            //4. Procesar el resultado
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                //int id = resultSet.getInt(1);
                String nombre = resultSet.getString("nombre");
                String telefono = resultSet.getString("telefono");
                int edad = resultSet.getInt("edad");
                System.out.println("Alumno "+ id + ", Nombre= " + nombre + ", Telefono= " + telefono + ", Edad= " + edad);
            }

            //++ Código para insertar un alumno a "hierro"
            //paso 1 y 2 ya están hechos

            //paso 3
            /*String insertQuery = "INSERT INTO alumno(id, nombre, telefono, edad) VALUES(3, 'Paco Perez', '666666666', 20)";
            int insertRow = statement.executeUpdate(insertQuery);
            if (insertRow > 0){
                System.out.println("Alumno insertado");
            }*/

            //++ Código para insertar un alumno cuyos datos están en variables
            int id = 4;
            String nombre = "Carmen Ruz";
            String telefono = "141414141";
            int edad = 21;
            String insertQuery2 = "INSERT INTO alumno (id, nombre, telefono, edad) VALUES (" + id + ", '" + nombre + "', '" + telefono + "', " + edad + ")";
            int insertRow2 = statement.executeUpdate(insertQuery2);
            if (insertRow2 > 0) {
                System.out.println("Alumno insertado");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally{
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
