package Datos;

import com.mysql.cj.xdevapi.Result;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.*;

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

        return DriverManager.getConnection(url, user, pasword);

    }

    public void Desconectar(Connection conexion) {
        try {
            conexion.close();
            System.out.println("Se desconecto de la base de datos");
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
