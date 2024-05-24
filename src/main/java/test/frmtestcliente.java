package test;

import Datos.ClienteDao;
import Datos.Conexion1;
import Domain.Cliente;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JOptionPane;
import java.sql.*;

/**
 *
 * @author User
 */
public class frmtestcliente extends javax.swing.JFrame {

    /**
     * Creates new form frmtestcliente
     */
    ClienteDao clienteDao = new ClienteDao();
    Cliente cliente = new Cliente();

    //variable de tipo conexoin
    Connection conexion = null;
    Conexion1 instanciaMSQL = Conexion1.getInstancia();
    
    public frmtestcliente() {
        initComponents();
        this.setTitle("Control de Clientes");
        this.setLocationRelativeTo(null);
        listarDatos();
    }
    
    private void registrarDatos() {
        if (comprobarCampos() == true) {
            JOptionPane.showMessageDialog(rootPane, "Por favor completar Los Campos");
        } else {
            String nombre;
            String apellido;
            String email;
            String telefono;
            double saldo;
            try {
                //captamos los datos
                conexion = instanciaMSQL.ConectarBd();

                //si esta conexion 
                if (conexion.getAutoCommit()) {
                    conexion.setAutoCommit(false);
                }
                nombre = txtNombre.getText().trim();
                apellido = txtApellido.getText().trim();
                email = txtEmail.getText().trim();
                telefono = txtTelefono.getText().trim();
                saldo = Double.parseDouble(txtSaldo.getText().trim());
                
                cliente = new Cliente(nombre, apellido, email, telefono, saldo);
                
                clienteDao.insertar(cliente);

                //hacemos un comit
                conexion.commit();
                JOptionPane.showMessageDialog(rootPane, "Cliente => " + nombre + " Registrado");
                
                this.limpiarCampos();
                
                listarDatos();
            } catch (HeadlessException | NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(rootPane, "Error en Boton Registrar => " + e.getMessage());
                
                try {
                    //retresedemos y evitar esos errores que han ocurrido
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en boton registrar => " + e.getMessage());
                }
            }
        }
    }
    
    private boolean BdDatos() {
        try {
            conexion = instanciaMSQL.ConectarBd();
            //si esta conexion podemos que autocommit sea falso por defecto
            if (conexion.getAutoCommit()) {
                conexion.setAutoCommit(false);
            }
            
            String consulta = "Select * from clientes";
            
            PreparedStatement consultapre = conexion.prepareStatement(consulta);
            ResultSet seleccion = consultapre.executeQuery();
            
            return seleccion.next();
            
        } catch (SQLException e) {
            System.out.println("Error al confirmar si hay datos => " + e.getMessage());
        } finally {
            instanciaMSQL.Desconectar(conexion);
        }
        return false;
        
    }
    
    private void listarDatos() {
        if (BdDatos() == false) {
            JOptionPane.showMessageDialog(rootPane, "La base de Datos no tiene Informacion a mostrar");
        } else {
            try {
                conexion = instanciaMSQL.ConectarBd();
                //si esta conexion podemos que autocommit sea falso por defecto
                if (conexion.getAutoCommit()) {
                    conexion.setAutoCommit(false);
                }
                List<Cliente> clientes = clienteDao.listar();
                jTextArea1.setText("");
                for (Cliente cliente1 : clientes) {
                    jTextArea1.append(cliente1.toString());
                    jTextArea1.append("");
                    jTextArea1.append("");
                }

                //hacemos una confirmacion
                conexion.commit();
            } catch (SQLException ex) {
                System.out.println("Error en boton listar => " + ex.getMessage());
                try {
                    conexion.rollback();
                } catch (SQLException ex1) {
                    System.out.println("Error en boton listar => " + ex1.getMessage());
                }
            }
        }
    }
    
    private boolean comprobarCampos() {
        return txtNombre.getText().trim().isEmpty()
                || txtApellido.getText().trim().isEmpty()
                || txtEmail.getText().trim().isEmpty()
                || txtSaldo.getText().trim().isEmpty()
                || txtTelefono.getText().trim().isEmpty();
        
    }
    
    private void limpiarCampos() {
        txtApellido.setText(null);
        txtEmail.setText(null);
        txtID.setText(null);
        txtNombre.setText(null);
        txtSaldo.setText(null);
        txtTelefono.setText(null);
        
        txtNombre.requestFocus();
    }
    
    private void Eliminar() {
        int id;
        
        if (txtID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Por favor digita el ID");
        } else {
            try {
                conexion = instanciaMSQL.ConectarBd();

                //si esta conexion 
                if (conexion.getAutoCommit()) {
                    conexion.setAutoCommit(false);
                }
                
                id = Integer.parseInt(txtID.getText().trim());
                cliente = new Cliente(id);

                //llamamos al metodo
                clienteDao.Eliminar(cliente);

                //hace un comit osea una confirmacion
                conexion.commit();
                JOptionPane.showMessageDialog(rootPane, "Cliente con el ID => " + id
                        + "\nEliminado");
                
                txtID.setText(null);

                // listarDatos();
            } catch (HeadlessException | NumberFormatException | SQLException e) {
                System.out.println("Error al eliminar => " + e.getMessage());
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en Eliminar => " + e.getMessage());
                }
            }
        }
        
    }
    
    private void modificar() {
        
        if (txtID.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Por favor Ingresar el ID a modificar");
        } else if (comprobarCampos()) {
            JOptionPane.showMessageDialog(rootPane, "Por favor completar los campos");
        } else {
            try {
                //captamos los datos
                conexion = instanciaMSQL.ConectarBd();

                //si esta conexion 
                if (conexion.getAutoCommit()) {
                    conexion.setAutoCommit(false);
                }
                int id;
                String nombre;
                String apellido;
                String email;
                String telefono;
                double saldo;

                //captamos los datos
                nombre = txtNombre.getText().trim();
                apellido = txtApellido.getText().trim();
                email = txtEmail.getText().trim();
                telefono = txtTelefono.getText().trim();
                saldo = Double.parseDouble(txtSaldo.getText().trim());
                id = Integer.parseInt(txtID.getText().trim());
                
                cliente = new Cliente(id, nombre, apellido, email, telefono, saldo);
                clienteDao.modificar(cliente);
                
                conexion.commit();
                JOptionPane.showMessageDialog(rootPane, "Cliente con el ID: " + id + "\n Modificado");
                
                limpiarCampos();
                // btnRegistrar.setVisible(false);

                listarDatos();
            } catch (HeadlessException | NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(rootPane, "Error en boton Modificar => " + e.getMessage());
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en modificar => " + e.getMessage());
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtSaldo = new javax.swing.JTextField();
        btnRegistrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtID = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        btnConsultar = new javax.swing.JButton();
        btnLimpiarListado = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtNombre.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtNombre.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Digite su nombre:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 12))); // NOI18N

        txtApellido.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtApellido.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Digite su Apellido:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 12))); // NOI18N

        txtEmail.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtEmail.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Digite su Email:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 12))); // NOI18N

        txtTelefono.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtTelefono.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Digite su Telefono:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 12))); // NOI18N

        txtSaldo.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtSaldo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Digite su Saldo:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 12))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ingrese los Datos:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(btnRegistrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTelefono)
                    .addComponent(txtSaldo)
                    .addComponent(txtApellido)
                    .addComponent(txtNombre))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRegistrar)
                .addGap(21, 21, 21))
        );

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Control de Clientes");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtID.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        txtID.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Digite el ID para Modificar O Eliminar:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 12))); // NOI18N

        btnModificar.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnModificar.setText("Moficar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(btnModificar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Cascadia Code", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        btnConsultar.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnConsultar.setText("Consultar");
        btnConsultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConsultarActionPerformed(evt);
            }
        });

        btnLimpiarListado.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        btnLimpiarListado.setText("Limpiar Listado");
        btnLimpiarListado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarListadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane1)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(73, 73, 73)
                                .addComponent(btnConsultar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLimpiarListado)))
                        .addGap(0, 18, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnConsultar)
                            .addComponent(btnLimpiarListado))))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        registrarDatos();
        

    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnConsultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsultarActionPerformed
        // TODO add your handling code here:
        listarDatos();
    }//GEN-LAST:event_btnConsultarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        modificar();
        // btnRegistrar.setVisible(true);


    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        Eliminar();
        listarDatos();
        //btnRegistrar.setVisible(true);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarListadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarListadoActionPerformed
        jTextArea1.setText("");
    }//GEN-LAST:event_btnLimpiarListadoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmtestcliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmtestcliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmtestcliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmtestcliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmtestcliente().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiarListado;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtSaldo;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
