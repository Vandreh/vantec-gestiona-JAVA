package ventanas;

import clases.Conexion;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Vandreh Esmeraldo
 */
public class Mensajes extends javax.swing.JFrame {

    String user;
    DefaultTableModel cumpleanos;
    DefaultTableModel c2;
    DefaultTableModel c14;
    DefaultTableModel c60;
    DefaultTableModel p6;
    DefaultTableModel p5;
    DefaultTableModel p4;
    DefaultTableModel p3;
    DefaultTableModel p2;
    DefaultTableModel p1;
    DefaultTableModel p0;

    public Mensajes() {
        initComponents();
        user = Login.user;
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Avisos importantes - Sesión de " + user);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jLabel_NombreUsuario.setText("Avisos importantes - Sesion de " + user);

        cumpleanos = new DefaultTableModel();
        c2 = new DefaultTableModel();
        c14 = new DefaultTableModel();
        c60 = new DefaultTableModel();
        p6 = new DefaultTableModel();
        p5 = new DefaultTableModel();
        p4 = new DefaultTableModel();
        p3 = new DefaultTableModel();
        p2 = new DefaultTableModel();
        p1 = new DefaultTableModel();
        p0 = new DefaultTableModel();

        cumpleanos.addColumn("Cliente");
        cumpleanos.addColumn("Telefono");
        cumpleanos.addColumn("Cumpleaños");

        c2.addColumn("Id Venta");
        c2.addColumn("Cliente");
        c2.addColumn("Producto");
        c2.addColumn("Precio Venta");
        c2.addColumn("Fecha Venta");

        c14.addColumn("Id Venta");
        c14.addColumn("Cliente");
        c14.addColumn("Producto");
        c14.addColumn("Precio Venta");
        c14.addColumn("Fecha Venta");

        c60.addColumn("Id Venta");
        c60.addColumn("Cliente");
        c60.addColumn("Producto");
        c60.addColumn("Precio Venta");
        c60.addColumn("Fecha Venta");

        p6.addColumn("Id Stock");
        p6.addColumn("Producto");
        p6.addColumn("Vencimiento");

        p5.addColumn("Id Stock");
        p5.addColumn("Producto");
        p5.addColumn("Vencimiento");

        p4.addColumn("Id Stock");
        p4.addColumn("Producto");
        p4.addColumn("Vencimiento");

        p3.addColumn("Id Stock");
        p3.addColumn("Producto");
        p3.addColumn("Vencimiento");

        p2.addColumn("Id Stock");
        p2.addColumn("Producto");
        p2.addColumn("Vencimiento");

        p1.addColumn("Id Stock");
        p1.addColumn("Producto");
        p1.addColumn("Vencimiento");

        p0.addColumn("Id Stock");
        p0.addColumn("Producto");
        p0.addColumn("Vencimiento");

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement(
                    "select nombre_cliente, tel_cliente, cumpleanos from clientes where MONTH(cumpleanos) = MONTH(CURRENT_DATE())");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                cumpleanos.addRow(fila);
            }
            cn.close();
            jTable_cumpleanos.setModel(cumpleanos);
        } catch (SQLException e) {
            System.err.println("Error en el llenado de la tabla cumpleaños.");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_venta, cliente_venta, producto_venta, precio_venta, DATE_FORMAT(fecha_venta, '%d-%m-%Y') from venta where DATE_ADD(fecha_entrega,INTERVAL 2 DAY) = Date_format(now(),'%Y-%m-%d')");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[5];
                for (int i = 0; i < 5; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                c2.addRow(fila);
            }
            cn.close();
            jTable_c2.setModel(c2);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que se pasaron 2 dias apos una entrega!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_venta, cliente_venta, producto_venta, precio_venta, DATE_FORMAT(fecha_venta, '%d-%m-%Y') from venta where DATE_ADD(fecha_entrega,INTERVAL 14 DAY) = Date_format(now(),'%Y-%m-%d')");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[5];
                for (int i = 0; i < 5; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                c14.addRow(fila);
            }
            cn.close();
            jTable_c14.setModel(c14);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que se pasaron 14 dias apos una entrega!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_venta, cliente_venta, producto_venta, precio_venta, DATE_FORMAT(fecha_venta, '%d-%m-%Y') from venta where DATE_ADD(fecha_entrega,INTERVAL 60 DAY) = Date_format(now(),'%Y-%m-%d')");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[5];
                for (int i = 0; i < 5; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                c60.addRow(fila);
            }
            cn.close();
            jTable_c60.setModel(c14);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que se pasaron 60 dias apos una entrega!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') BETWEEN  DATE_ADD(vencimiento,INTERVAL -180 DAY) AND DATE_ADD(vencimiento,INTERVAL -150 DAY)");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p6.addRow(fila);
            }
            cn.close();
            jTable_p6.setModel(p6);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 6 meses para vencimiento del producto!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') BETWEEN  DATE_ADD(vencimiento,INTERVAL -150 DAY) AND DATE_ADD(vencimiento,INTERVAL -120 DAY)");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p5.addRow(fila);
            }
            cn.close();
            jTable_p5.setModel(p5);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 5 meses para vencimiento del producto!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') BETWEEN  DATE_ADD(vencimiento,INTERVAL -120 DAY) AND DATE_ADD(vencimiento,INTERVAL -90 DAY)");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p4.addRow(fila);
            }
            cn.close();
            jTable_p4.setModel(p4);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 4 meses para vencimiento del producto!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') BETWEEN  DATE_ADD(vencimiento,INTERVAL -90 DAY) AND DATE_ADD(vencimiento,INTERVAL -60 DAY)");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p3.addRow(fila);
            }
            cn.close();
            jTable_p3.setModel(p3);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 3 meses para vencimiento del producto!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') BETWEEN  DATE_ADD(vencimiento,INTERVAL -60 DAY) AND DATE_ADD(vencimiento,INTERVAL -30 DAY)");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p2.addRow(fila);
            }
            cn.close();
            jTable_p2.setModel(p2);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 2 meses para vencimiento del producto!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') BETWEEN  DATE_ADD(vencimiento,INTERVAL -30 DAY) AND vencimiento");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p1.addRow(fila);
            }
            cn.close();
            jTable_p1.setModel(p1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 1 mes para vencimiento del producto!");
        }

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, producto, vencimiento from estoque where Date_format(now(),'%Y-%m-%d') > vencimiento");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[3];
                for (int i = 0; i < 3; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                p0.addRow(fila);
            }
            cn.close();
            jTable_p0.setModel(p0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al informar que falta 6 meses para vencimiento del producto!");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane_equipos = new javax.swing.JScrollPane();
        jTable_productos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel_NombreUsuario = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane_equipos3 = new javax.swing.JScrollPane();
        jTable_cumpleanos = new javax.swing.JTable();
        jLabel_Nombre20 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane_equipos19 = new javax.swing.JScrollPane();
        jTable_productos12 = new javax.swing.JTable();
        jLabel_Nombre21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane_equipos2 = new javax.swing.JScrollPane();
        jTable_c2 = new javax.swing.JTable();
        jLabel_Nombre11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane_equipos11 = new javax.swing.JScrollPane();
        jTable_productos11 = new javax.swing.JTable();
        jLabel_Nombre14 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane_equipos14 = new javax.swing.JScrollPane();
        jTable_c14 = new javax.swing.JTable();
        jLabel_Nombre17 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane_equipos15 = new javax.swing.JScrollPane();
        jTable_c60 = new javax.swing.JTable();
        jLabel_Nombre18 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane_equipos6 = new javax.swing.JScrollPane();
        jTable_p6 = new javax.swing.JTable();
        jLabel_Nombre6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane_equipos5 = new javax.swing.JScrollPane();
        jTable_p5 = new javax.swing.JTable();
        jLabel_Nombre5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel_Nombre12 = new javax.swing.JLabel();
        jScrollPane_equipos12 = new javax.swing.JScrollPane();
        jTable_p4 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane_equipos13 = new javax.swing.JScrollPane();
        jTable_p3 = new javax.swing.JTable();
        jLabel_Nombre13 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel_Nombre15 = new javax.swing.JLabel();
        jScrollPane_equipos16 = new javax.swing.JScrollPane();
        jTable_p2 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane_equipos17 = new javax.swing.JScrollPane();
        jTable_p1 = new javax.swing.JTable();
        jLabel_Nombre16 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane_equipos18 = new javax.swing.JScrollPane();
        jTable_p0 = new javax.swing.JTable();
        jLabel_Nombre19 = new javax.swing.JLabel();

        jTable_productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos.setViewportView(jTable_productos);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 218, 225));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jLabel_NombreUsuario.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel_NombreUsuario.setForeground(new java.awt.Color(51, 51, 51));
        jLabel_NombreUsuario.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_NombreUsuario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel_NombreUsuario)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel7.setText("Creado por Vandreh Esmeraldo ®");

        jTabbedPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        jTable_cumpleanos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos3.setViewportView(jTable_cumpleanos);

        jLabel_Nombre20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre20.setText("MES DEL CUMPLEAÑOS:");

        jTable_productos12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos19.setViewportView(jTable_productos12);

        jLabel_Nombre21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre21.setText("2 DIAS APOS LA VENTA:");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos19, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Nombre21)
                .addContainerGap(914, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Nombre21)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane_equipos19, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(347, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos3, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_Nombre20, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(503, 503, 503))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel_Nombre20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_equipos3, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel13Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("CUMPLEAÑOS", jPanel13);

        jTable_c2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos2.setViewportView(jTable_c2);

        jLabel_Nombre11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre11.setText("2 DIAS APOS LA VENTA: LLAMAR EL CLIENTE PREGUNTAR SOBRE REACCIONES ALERGICAS.");

        jTable_productos11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos11.setViewportView(jTable_productos11);

        jLabel_Nombre14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre14.setText("2 DIAS APOS LA VENTA:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos11, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Nombre14)
                .addContainerGap(914, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Nombre14)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane_equipos11, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(347, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos2, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_Nombre11)
                .addGap(283, 283, 283))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel_Nombre11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane_equipos2, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("CLIENTE 2", jPanel2);

        jTable_c14.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos14.setViewportView(jTable_c14);

        jLabel_Nombre17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre17.setText("14 DIAS APOS LA VENTA: LLAMAR AL CLIENTE HABLAR SOBRE NOVEDADES DE MARY KAY.");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos14, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(240, 240, 240)
                .addComponent(jLabel_Nombre17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(jLabel_Nombre17)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane_equipos14, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("CLIENTE 14", jPanel5);

        jTable_c60.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos15.setViewportView(jTable_c60);

        jLabel_Nombre18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre18.setText("60 DIAS APOS LA VENTA: Llamar al cliente preguntar sobre necesidad de repuesto.");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos15, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(250, 250, 250)
                .addComponent(jLabel_Nombre18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(jLabel_Nombre18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos15, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("CLIENTE 60", jPanel6);

        jTable_p6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos6.setViewportView(jTable_p6);

        jLabel_Nombre6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre6.setText("5 MESES PARA EL VENCIMIENTO DE LOS PRODUCTOS:");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Nombre6)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane_equipos6, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel_Nombre6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos6, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTO 6", jPanel12);

        jTable_p5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos5.setViewportView(jTable_p5);

        jLabel_Nombre5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre5.setText("5 MESES PARA EL VENCIMIENTO DE LOS PRODUCTOS:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Nombre5)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane_equipos5, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel_Nombre5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos5, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTOS 5", jPanel3);

        jLabel_Nombre12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre12.setText("4 MESES PARA EL VENCIMIENTO DE LOS PRODUCTOS:");

        jTable_p4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos12.setViewportView(jTable_p4);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Nombre12)
            .addComponent(jScrollPane_equipos12, javax.swing.GroupLayout.DEFAULT_SIZE, 1077, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel_Nombre12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos12, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTOS 4", jPanel7);

        jTable_p3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos13.setViewportView(jTable_p3);

        jLabel_Nombre13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre13.setText("3 MESES PARA EL VENCIMIENTO DE LOS PRODUCTOS:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos13)
            .addComponent(jLabel_Nombre13)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel_Nombre13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos13, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTOS 3", jPanel8);

        jLabel_Nombre15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre15.setText("2 MESES PARA EL VENCIMIENTO DE LOS PRODUCTOS:");

        jTable_p2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos16.setViewportView(jTable_p2);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos16)
            .addComponent(jLabel_Nombre15)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel_Nombre15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos16, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTOS 2", jPanel9);

        jTable_p1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos17.setViewportView(jTable_p1);

        jLabel_Nombre16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre16.setText("1 MES PARA EL VENCIMIENTO DE LOS PRODUCTOS:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos17)
            .addComponent(jLabel_Nombre16)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel_Nombre16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos17, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTOS 1", jPanel10);

        jTable_p0.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane_equipos18.setViewportView(jTable_p0);

        jLabel_Nombre19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre19.setText("PRODUCTOS VENCIDOS:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos18)
            .addComponent(jLabel_Nombre19)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jLabel_Nombre19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_equipos18, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PRODUCTOS 0", jPanel11);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(31, 31, 31))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Mensajes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mensajes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mensajes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mensajes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mensajes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel_Nombre11;
    private javax.swing.JLabel jLabel_Nombre12;
    private javax.swing.JLabel jLabel_Nombre13;
    private javax.swing.JLabel jLabel_Nombre14;
    private javax.swing.JLabel jLabel_Nombre15;
    private javax.swing.JLabel jLabel_Nombre16;
    private javax.swing.JLabel jLabel_Nombre17;
    private javax.swing.JLabel jLabel_Nombre18;
    private javax.swing.JLabel jLabel_Nombre19;
    private javax.swing.JLabel jLabel_Nombre20;
    private javax.swing.JLabel jLabel_Nombre21;
    private javax.swing.JLabel jLabel_Nombre5;
    private javax.swing.JLabel jLabel_Nombre6;
    private javax.swing.JLabel jLabel_NombreUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane_equipos;
    private javax.swing.JScrollPane jScrollPane_equipos11;
    private javax.swing.JScrollPane jScrollPane_equipos12;
    private javax.swing.JScrollPane jScrollPane_equipos13;
    private javax.swing.JScrollPane jScrollPane_equipos14;
    private javax.swing.JScrollPane jScrollPane_equipos15;
    private javax.swing.JScrollPane jScrollPane_equipos16;
    private javax.swing.JScrollPane jScrollPane_equipos17;
    private javax.swing.JScrollPane jScrollPane_equipos18;
    private javax.swing.JScrollPane jScrollPane_equipos19;
    private javax.swing.JScrollPane jScrollPane_equipos2;
    private javax.swing.JScrollPane jScrollPane_equipos3;
    private javax.swing.JScrollPane jScrollPane_equipos5;
    private javax.swing.JScrollPane jScrollPane_equipos6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable_c14;
    private javax.swing.JTable jTable_c2;
    private javax.swing.JTable jTable_c60;
    private javax.swing.JTable jTable_cumpleanos;
    private javax.swing.JTable jTable_p0;
    private javax.swing.JTable jTable_p1;
    private javax.swing.JTable jTable_p2;
    private javax.swing.JTable jTable_p3;
    private javax.swing.JTable jTable_p4;
    private javax.swing.JTable jTable_p5;
    private javax.swing.JTable jTable_p6;
    private javax.swing.JTable jTable_productos;
    private javax.swing.JTable jTable_productos11;
    private javax.swing.JTable jTable_productos12;
    // End of variables declaration//GEN-END:variables
}
