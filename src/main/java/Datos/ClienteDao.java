package Datos;

import Domain.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDao {

    /*
    nombre varchar (50),
apellido varchar(50),
email varchar (50),
telefono varchar (50),
saldo double
     */
    Conexion1 instanciaMysql = Conexion1.getInstancia();

    private static final String SQL_SELECT = "select * from clientes";
    private static final String SQL_INSERT = "insert into clientes (nombre,apellido,email,telefono,saldo) values (?,?,?,?,?)";
    private static final String SQL_UPDATE = "update clientes set nombre = ?, apellido = ?, email = ?, telefono = ?, saldo = ? where id = ? ";
    private static final String SQL_DELETE = "delete from clientes where id = ?";

    //metodo para listar los datos
    public List<Cliente> listar() {
        Connection conexion = null;
        PreparedStatement consultaPreparada = null;
        ResultSet resultado = null;

        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente;
        try {
            conexion = instanciaMysql.ConectarBd();

            consultaPreparada = conexion.prepareStatement(SQL_SELECT);
            resultado = consultaPreparada.executeQuery();

            while (resultado.next()) {
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String apellido = resultado.getString("apellido");
                String email = resultado.getString("email");
                String telefonoF = resultado.getString("telefono");
                double saldo = resultado.getDouble("saldo");
                cliente = new Cliente(id, nombre, apellido, email, telefonoF, saldo);

                clientes.add(cliente); // agregamos al arryalist
            }
        } catch (SQLException e) {
            System.out.println("ERROR EN LISTAR => " + e.getMessage());
        } finally { //finalmente cerramos las variables
            instanciaMysql.cerrarResultado(resultado);
            instanciaMysql.CerrarStemenyt(consultaPreparada);
            instanciaMysql.Desconectar(conexion);
        }
        return clientes;
    }

}
