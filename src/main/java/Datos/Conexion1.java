package Datos;

import java.sql.*;
public class Conexion1 {

    private Conexion1() {

    }
    private static final String url = "jdbc:mysql://localhost/controlclientes";
    private static final String user = "root";
    private static final String pasword = "1234";

    private static Conexion1 instancia;
//
//    public DataSource ObtenerFuenteDatos() {
//        BasicDataSource datos = new BasicDataSource();
//        datos.setUrl(url);
//        datos.setUsername(user);
//        datos.setPassword(pasword);
//
//        datos.setInitialSize(50); // tamaño de conexiones a la base de datos
//        return datos;
//    }

    public Connection ConectarBd() throws SQLException {
        System.out.println("Conectado");
        return DriverManager.getConnection(url, user, pasword);

    }

    public void Desconectar(Connection conexion) {
        try {
            conexion.close();
            System.out.println("Desconectado");
        } catch (SQLException e) {
            System.out.println("ERROR AL DESCONECTAR: " + e.getMessage());
        }
    }

    public void cerrarResultado(ResultSet resultado) {
        try {
            resultado.close();
        } catch (SQLException e) {
            System.out.println("Error en result set: " + e.getMessage());
        }
    }

    public void CerrarStemenyt(PreparedStatement statement) {
        try {
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error en preparet statemet: " + e.getMessage());
        }
    }

    public static Conexion1 getInstancia() {
        if (instancia == null) {
            instancia = new Conexion1();
        }

        return instancia;
    }
}
