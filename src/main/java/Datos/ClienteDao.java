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

            while (resultado.next()) {//mientras resultadp tenga datos
                int id = resultado.getInt("id");
                String nombre = resultado.getString("nombre");
                String apellido = resultado.getString("apellido");
                String email = resultado.getString("email");
                String telefonoF = resultado.getString("telefono");
                double saldo = resultado.getDouble("saldo");
                cliente = new Cliente(id, nombre, apellido, email, telefonoF, saldo);

                clientes.add(cliente); // agregamos al arraylist
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

    //creamos el metodo para insertar 
    public int insertar(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement consultaPreparada = null;
        int registros = 0;
        try {
            conexion = instanciaMysql.ConectarBd();
            consultaPreparada = conexion.prepareStatement(SQL_INSERT);
            consultaPreparada.setString(1, cliente.getNombre());
            consultaPreparada.setString(2, cliente.getApellido());
            consultaPreparada.setString(3, cliente.getEmail());
            consultaPreparada.setString(4, cliente.getTelefono());
            consultaPreparada.setDouble(5, cliente.getSaldo());

            registros = consultaPreparada.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en insertar => " + e.getMessage());
        } finally {
            instanciaMysql.CerrarStemenyt(consultaPreparada);
            instanciaMysql.Desconectar(conexion);

        }
        return registros;
    }

    //creamos el metodo para modificar registros
    public int modificar(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement consultaPreparada = null;
        int registros = 0;
        try {
            conexion = instanciaMysql.ConectarBd();
            consultaPreparada = conexion.prepareStatement(SQL_UPDATE);
            consultaPreparada.setString(1, cliente.getNombre());
            consultaPreparada.setString(2, cliente.getApellido());
            consultaPreparada.setString(3, cliente.getEmail());
            consultaPreparada.setString(4, cliente.getTelefono());
            consultaPreparada.setDouble(5, cliente.getSaldo());
            consultaPreparada.setInt(6, cliente.getIdCliente());

            registros = consultaPreparada.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en modificar => " + e.getMessage());
        } finally {
            instanciaMysql.CerrarStemenyt(consultaPreparada);
            instanciaMysql.Desconectar(conexion);
        }
        return registros;
    }

    //metodo para eliminar 
    public int Eliminar(Cliente cliente) {
        Connection conexion = null;
        PreparedStatement consultaPreparada = null;
        int registros = 0;
        
        try {
            conexion = instanciaMysql.ConectarBd();
            consultaPreparada = conexion.prepareStatement(SQL_DELETE);
            consultaPreparada.setInt(1, cliente.getIdCliente());
            registros = consultaPreparada.executeUpdate();

            registros = consultaPreparada.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error en Eliminar => " + e.getMessage());
        } finally {
            instanciaMysql.CerrarStemenyt(consultaPreparada);
            instanciaMysql.Desconectar(conexion);
        }
        return registros;
    }
}
