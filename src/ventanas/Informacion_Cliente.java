/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import java.sql.*;
import clases.Conexion;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Vandreh Esmeraldo
 */
public class Informacion_Cliente extends javax.swing.JFrame {

    DefaultTableModel model = new DefaultTableModel();

    int IDcliente_update = 0;
    public static int IDventa_update = 0;
    String user = "", nombre_cliente = "";
    TableRowSorter trs1;

    /**
     * Creates new form Informacion_Cliente
     */
    public Informacion_Cliente() {
        initComponents();
        user = Login.user;
        IDcliente_update = GestionarClientes.IDcliente_update;
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        try {
            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select * from clientes where id_cliente = '" + IDcliente_update + "'");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                setTitle("Información del cliente " + rs.getString("nombre_cliente") + " - Sesión de " + user);
                jLabel_Titulo.setText("Información del cliente " + rs.getString("nombre_cliente"));

                nombre_cliente = rs.getString("nombre_cliente");
                txt_nombre.setText(rs.getString("nombre_cliente"));
                txt_mail.setText(rs.getString("mail_cliente"));
                txt_telefono.setText(rs.getString("tel_cliente"));
                txt_direccion.setText(rs.getString("dir_cliente"));
                cmb_favorito.setSelectedItem(rs.getString("favorito"));
                cmb_tipoDePiel.setSelectedItem(rs.getString("tipo_de_piel"));
                Date cumpleanos = rs.getDate("cumpleanos");
                jDateCumpleanos.setDate(cumpleanos);
                Date registro = rs.getDate("fecha_de_registro");
                jDateRegistro.setDate(registro);

            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error en cargar usuario." + e);
            JOptionPane.showMessageDialog(
                    null, "¡¡ERROR al cargar!!, contacte al administrador.");
        }

        try {
            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement(
                    "select id_venta, cliente_venta, producto_venta, descuento_venta, delivery, DATE_FORMAT(fecha_entrega, '%d-%m-%Y'), precio_compra, precio_venta, DATE_FORMAT(fecha_venta, '%d-%m-%Y'), observaciones from venta where cliente_venta = '" + nombre_cliente + "'");
            ResultSet rs = pst.executeQuery();

            jTable_ventas = new JTable(model);
            jScrollPane_equipos.setViewportView(jTable_ventas);

            model.addColumn("ID Venta");
            model.addColumn("Cliente");
            model.addColumn("Producto");
            model.addColumn("Descuento");
            model.addColumn("Delivery");
            model.addColumn("Fecha de Entrega");
            model.addColumn("Precio compra");
            model.addColumn("Precio venta");
            model.addColumn("Fecha de venta");
            model.addColumn("Observaciones");

            while (rs.next()) {
                Object[] fila = new Object[10];

                for (int i = 0; i < 10; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
            }
            cn.close();
            trs1 = new TableRowSorter(model);
            jTable_ventas.setRowSorter(trs1);

            int contar = (jTable_ventas.getRowCount());
            String contarstr = String.valueOf(contar);
            txt_totalventas.setText(contarstr);

        } catch (SQLException e) {
            System.err.println("Error en el llenado de la tabla de ventas");
        }
        
        TableColumnModel columnModel = jTable_ventas.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(100);
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setMaxWidth(300);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(300);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(3).setMaxWidth(200);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable_ventas.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        jTable_ventas.getColumnModel().getColumn(6).setCellRenderer(cellRenderer);
        jTable_ventas.getColumnModel().getColumn(7).setCellRenderer(cellRenderer);
        jTable_ventas.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        jTable_ventas.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jTable_ventas.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);

        jTable_ventas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                IDventa_update = (int) jTable_ventas.getValueAt(jTable_ventas.getSelectedRow(), 0);

                if (IDventa_update > -1) {
                    RegistrarVenta registrarventa = new RegistrarVenta();
                    registrarventa.setVisible(true);
                    IDventa_update = 0;
                    dispose();
                }
            }
        });

        float lucro = 0;
        int contarCompra = jTable_ventas.getRowCount();
        float sumaCompra = 0;
        for (int i = 0; i < contarCompra; i++) {
            sumaCompra = sumaCompra + Float.parseFloat(jTable_ventas.getValueAt(i, 6).toString());
        }
        float sumaVenta = 0;
        for (int i = 0; i < contarCompra; i++) {
            sumaVenta = sumaVenta + Float.parseFloat(jTable_ventas.getValueAt(i, 7).toString());
        }
        lucro = sumaVenta - sumaCompra;
        String lucrostr = String.valueOf(lucro);
        txt_lucro_total.setText(lucrostr);
        String totalventa = String.valueOf(sumaVenta);
        txt_total_venta.setText(totalventa);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel_Titulo = new javax.swing.JLabel();
        jLabel_Nombre = new javax.swing.JLabel();
        txt_nombre = new javax.swing.JTextField();
        jLabel_mail = new javax.swing.JLabel();
        txt_mail = new javax.swing.JTextField();
        jLabel_telefono = new javax.swing.JLabel();
        txt_telefono = new javax.swing.JTextField();
        jLabel_direccion = new javax.swing.JLabel();
        txt_direccion = new javax.swing.JTextField();
        jLabel_footer = new javax.swing.JLabel();
        jButton_Actualizar = new javax.swing.JButton();
        jButton_Registrar = new javax.swing.JButton();
        jButton_ImprimirReporte = new javax.swing.JButton();
        jScrollPane_equipos = new javax.swing.JScrollPane();
        jTable_ventas = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cmb_tipoDePiel = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        cmb_favorito = new javax.swing.JComboBox<>();
        jDateCumpleanos = new com.toedter.calendar.JDateChooser();
        jDateRegistro = new com.toedter.calendar.JDateChooser();
        jLabel_Nombre1 = new javax.swing.JLabel();
        txt_total_venta = new javax.swing.JTextField();
        jLabel_mail1 = new javax.swing.JLabel();
        txt_lucro_total = new javax.swing.JTextField();
        txt_totalventas = new javax.swing.JTextField();
        jLabel_Nombre17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 218, 225));

        jLabel_Titulo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel_Titulo.setText("Información del cliente");

        jLabel_Nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre.setText("Nombre:");

        txt_nombre.setBackground(new java.awt.Color(153, 153, 255));
        txt_nombre.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_nombre.setForeground(new java.awt.Color(255, 255, 255));
        txt_nombre.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nombre.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_mail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_mail.setText("email:");

        txt_mail.setBackground(new java.awt.Color(153, 153, 255));
        txt_mail.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_mail.setForeground(new java.awt.Color(255, 255, 255));
        txt_mail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_mail.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_telefono.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_telefono.setText("Télefono:");

        txt_telefono.setBackground(new java.awt.Color(153, 153, 255));
        txt_telefono.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_telefono.setForeground(new java.awt.Color(255, 255, 255));
        txt_telefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_telefono.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_direccion.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_direccion.setText("Dirección:");

        txt_direccion.setBackground(new java.awt.Color(153, 153, 255));
        txt_direccion.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_direccion.setForeground(new java.awt.Color(255, 255, 255));
        txt_direccion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_direccion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_footer.setText("Creado por Vandreh Esmeraldo ®");

        jButton_Actualizar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Actualizar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Actualizar.setText("Actualizar cliente");
        jButton_Actualizar.setBorder(null);
        jButton_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ActualizarActionPerformed(evt);
            }
        });

        jButton_Registrar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Registrar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Registrar.setText("Registrar Venta");
        jButton_Registrar.setBorder(null);
        jButton_Registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RegistrarActionPerformed(evt);
            }
        });

        jButton_ImprimirReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/impresora.png"))); // NOI18N
        jButton_ImprimirReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ImprimirReporteActionPerformed(evt);
            }
        });

        jTable_ventas.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane_equipos.setViewportView(jTable_ventas);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Fecha de Cumpleaños:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Fecha de registro:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setText("Tipo de piel:");

        cmb_tipoDePiel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mixta", "Normal", "Seca", "Oleosa" }));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setText("Favorito:");

        cmb_favorito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cuidado de la piel", "Maquillaje", "Fragrancia" }));

        jDateCumpleanos.setDateFormatString("dd.MM.yyyy");
        jDateCumpleanos.setMaxSelectableDate(new java.util.Date(253370786507000L));

        jDateRegistro.setDateFormatString("dd.MM.yyyy");
        jDateRegistro.setMaxSelectableDate(new java.util.Date(253370786507000L));

        jLabel_Nombre1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre1.setText("Total de las ventas: $");

        txt_total_venta.setBackground(new java.awt.Color(153, 153, 255));
        txt_total_venta.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_total_venta.setForeground(new java.awt.Color(255, 255, 255));
        txt_total_venta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_total_venta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_mail1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_mail1.setText("Lucro total: $");

        txt_lucro_total.setBackground(new java.awt.Color(153, 153, 255));
        txt_lucro_total.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_lucro_total.setForeground(new java.awt.Color(255, 255, 255));
        txt_lucro_total.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_lucro_total.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txt_totalventas.setBackground(new java.awt.Color(153, 153, 255));
        txt_totalventas.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_totalventas.setForeground(new java.awt.Color(255, 255, 255));
        txt_totalventas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_totalventas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre17.setText("Total de ventas: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane_equipos)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Titulo)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_mail)
                                    .addComponent(txt_mail, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jDateRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Nombre)
                                    .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jDateCumpleanos, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_direccion))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(cmb_tipoDePiel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_favorito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton_Registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addComponent(jButton_ImprimirReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_mail1)
                                .addComponent(txt_lucro_total, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel_Nombre1)
                                .addComponent(txt_total_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_telefono)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_Nombre17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_totalventas, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(114, 114, 114)
                                .addComponent(jLabel_footer)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_Titulo)
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_Nombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(4, 4, 4)
                                .addComponent(jDateCumpleanos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_mail)
                                .addGap(5, 5, 5)
                                .addComponent(txt_mail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(jDateRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_Nombre1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_total_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel_mail1)
                        .addGap(5, 5, 5)
                        .addComponent(txt_lucro_total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_telefono)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jButton_Registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(15, 15, 15)
                                        .addComponent(jButton_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jButton_ImprimirReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(txt_telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel_direccion)
                                .addGap(0, 0, 0)
                                .addComponent(txt_direccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_tipoDePiel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_favorito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(53, 53, 53)
                .addComponent(jScrollPane_equipos, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel_Nombre17)
                        .addComponent(txt_totalventas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel_footer))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActualizarActionPerformed

        int validacion = 0, diaCumpleanos, mesCumpleanos, anoCumpleanos, diaRegistro, mesRegistro, anoRegistro;
        String nombre, mail, telefono, direccion, cumpleanos, fecha_de_registro, favorito, tipo_de_piel;
        diaCumpleanos = jDateCumpleanos.getCalendar().get(Calendar.DAY_OF_MONTH);
        mesCumpleanos = jDateCumpleanos.getCalendar().get(Calendar.MONTH) + 1;
        anoCumpleanos = jDateCumpleanos.getCalendar().get(Calendar.YEAR);
        cumpleanos = anoCumpleanos + "." + mesCumpleanos + "." + diaCumpleanos;
        diaRegistro = jDateRegistro.getCalendar().get(Calendar.DAY_OF_MONTH);
        mesRegistro = jDateRegistro.getCalendar().get(Calendar.MONTH) + 1;
        anoRegistro = jDateRegistro.getCalendar().get(Calendar.YEAR);
        fecha_de_registro = anoRegistro + "." + mesRegistro + "." + diaRegistro;

        nombre = txt_nombre.getText().trim();
        mail = txt_mail.getText().trim();
        telefono = txt_telefono.getText().trim();
        direccion = txt_direccion.getText().trim();
        favorito = cmb_favorito.getSelectedItem().toString();
        tipo_de_piel = cmb_tipoDePiel.getSelectedItem().toString();

        if (nombre.equals("")) {
            txt_nombre.setBackground(Color.red);
            validacion++;
        }
        if (mail.equals("")) {
            txt_mail.setBackground(Color.red);
            validacion++;
        }
        if (telefono.equals("")) {
            txt_telefono.setBackground(Color.red);
            validacion++;
        }
        if (direccion.equals("")) {
            txt_direccion.setBackground(Color.red);
            validacion++;
        }
        if (cumpleanos.equals("")) {
            jDateCumpleanos.setBackground(Color.red);
            validacion++;
        }
        if (fecha_de_registro.equals("")) {
            jDateRegistro.setBackground(Color.red);
            validacion++;
        }

        if (validacion == 0) {

            try {

                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement(
                        "update clientes set nombre_cliente=?, mail_cliente=?, tel_cliente=?, dir_cliente=?, cumpleanos=?, fecha_de_registro=?, favorito=?, tipo_de_piel=?" + "where id_cliente = '" + IDcliente_update + "'");

                pst.setString(1, nombre);
                pst.setString(2, mail);
                pst.setString(3, telefono);
                pst.setString(4, direccion);
                pst.setString(5, cumpleanos);
                pst.setString(6, fecha_de_registro);
                pst.setString(7, favorito);
                pst.setString(8, tipo_de_piel);

                pst.executeUpdate();
                cn.close();

                JOptionPane.showMessageDialog(null, "Actualización correcta.");

            } catch (SQLException e) {
                System.err.println("Error en actualizar cliente." + e);
                JOptionPane.showMessageDialog(null, "¡¡ERROR al actualizar cliente!!, contacte al administrador.");
            }

            try {
                model.setRowCount(0);
                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement(
                        "select id_venta, cliente_venta, producto_venta, descuento_venta, delivery, DATE_FORMAT(fecha_entrega, '%d-%m-%Y'), precio_compra, precio_venta, DATE_FORMAT(fecha_venta, '%d-%m-%Y'), observaciones from venta where cliente_venta = '" + nombre_cliente + "'");
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Object[] fila = new Object[10];

                    for (int i = 0; i < 10; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    model.addRow(fila);
                }
                cn.close();
                trs1 = new TableRowSorter(model);
                jTable_ventas.setRowSorter(trs1);
            } catch (SQLException e) {
                System.err.println("Error en el llenado de la tabla de ventas");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos.");
        }
    }//GEN-LAST:event_jButton_ActualizarActionPerformed

    private void jButton_RegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RegistrarActionPerformed

        RegistrarVenta registrar_venta = new RegistrarVenta();
        registrar_venta.setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton_RegistrarActionPerformed

    private void jButton_ImprimirReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ImprimirReporteActionPerformed

        Document documento = new Document();

        try {

            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/" + txt_nombre.getText().trim() + ".pdf"));

            com.itextpdf.text.Image header = com.itextpdf.text.Image.getInstance("src/images/BannerPDF.jpg");
            header.scaleToFit(650, 1000);
            header.setAlignment(Chunk.ALIGN_CENTER);

            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Información del cliente. \n \n");
            parrafo.setFont(FontFactory.getFont("Tahoma", 14, Font.BOLD, BaseColor.DARK_GRAY));

            documento.open();
            documento.add(header);
            documento.add(parrafo);

            PdfPTable tablaCliente = new PdfPTable(5);
            tablaCliente.addCell("ID");
            tablaCliente.addCell("Nombre");
            tablaCliente.addCell("email");
            tablaCliente.addCell("Télefono");
            tablaCliente.addCell("Dirección");
            tablaCliente.addCell("Cumpleaños");
            tablaCliente.addCell("Fecha de registro");
            tablaCliente.addCell("favorito");
            tablaCliente.addCell("Tipo de piel");

            try {

                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement(
                        "select * from clientes where id_cliente = '" + IDcliente_update + "'");

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    do {

                        tablaCliente.addCell(rs.getString(1));
                        tablaCliente.addCell(rs.getString(2));
                        tablaCliente.addCell(rs.getString(3));
                        tablaCliente.addCell(rs.getString(4));
                        tablaCliente.addCell(rs.getString(5));
                        tablaCliente.addCell(rs.getString(6));
                        tablaCliente.addCell(rs.getString(7));
                        tablaCliente.addCell(rs.getString(8));
                        tablaCliente.addCell(rs.getString(9));

                    } while (rs.next());

                    documento.add(tablaCliente);
                }

                Paragraph parrafo2 = new Paragraph();
                parrafo2.setAlignment(Paragraph.ALIGN_CENTER);
                parrafo2.add("\n \n Ventas registradas. \n \n");
                parrafo2.setFont(FontFactory.getFont("Tahoma", 14, Font.BOLD, BaseColor.DARK_GRAY));

                documento.add(parrafo2);

                PdfPTable tablaVenta = new PdfPTable(10);
                tablaVenta.addCell("ID venta");
                tablaVenta.addCell("Cliente");
                tablaVenta.addCell("Producto");
                tablaVenta.addCell("Descuento");
                tablaVenta.addCell("Delivery");
                tablaVenta.addCell("Cantidad");
                tablaVenta.addCell("Fecha de entrega");
                tablaVenta.addCell("Precio");
                tablaVenta.addCell("Fecha de venta");
                tablaVenta.addCell("Observaciones");

                try {

                    Connection cn2 = Conexion.conectar();
                    PreparedStatement pst2 = cn2.prepareStatement(
                            "select id_venta, cliente_venta, producto_venta, descuento_venta, delivery, cantidad_venta, DATE_FORMAT(fecha_entrega, '%d-%m-%Y'), precio_compra, DATE_FORMAT(fecha_venta, '%d-%m-%Y'), observaciones from venta where id_cliente = '" + IDcliente_update + "'");

                    ResultSet rs2 = pst2.executeQuery();

                    if (rs2.next()) {
                        do {
                            tablaVenta.addCell(rs2.getString(1));
                            tablaVenta.addCell(rs2.getString(2));
                            tablaVenta.addCell(rs2.getString(3));
                            tablaVenta.addCell(rs2.getString(4));
                            tablaVenta.addCell(rs2.getString(5));
                            tablaVenta.addCell(rs2.getString(6));
                            tablaVenta.addCell(rs2.getString(7));
                            tablaVenta.addCell(rs2.getString(8));
                            tablaVenta.addCell(rs2.getString(9));
                            tablaVenta.addCell(rs2.getString(10));

                        } while (rs2.next());
                        documento.add(tablaVenta);
                    }

                } catch (SQLException e) {
                    System.err.println("Error al cargar ventas " + e);
                }

            } catch (SQLException e) {
                System.err.print("Error al obtener datos del clientes. " + e);
            }

            documento.close();
            JOptionPane.showMessageDialog(null, "Reporte creado correctamente.");

        } catch (DocumentException | IOException e) {
            System.err.println("Error en PDF o ruta de imagen" + e);
            JOptionPane.showMessageDialog(null, "Error al generar PDF, contacte al administrador");
        }
    }//GEN-LAST:event_jButton_ImprimirReporteActionPerformed

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
            java.util.logging.Logger.getLogger(Informacion_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Informacion_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Informacion_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Informacion_Cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Informacion_Cliente().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmb_favorito;
    private javax.swing.JComboBox<String> cmb_tipoDePiel;
    private javax.swing.JButton jButton_Actualizar;
    private javax.swing.JButton jButton_ImprimirReporte;
    private javax.swing.JButton jButton_Registrar;
    private com.toedter.calendar.JDateChooser jDateCumpleanos;
    private com.toedter.calendar.JDateChooser jDateRegistro;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel_Nombre;
    private javax.swing.JLabel jLabel_Nombre1;
    private javax.swing.JLabel jLabel_Nombre17;
    private javax.swing.JLabel jLabel_Titulo;
    private javax.swing.JLabel jLabel_direccion;
    private javax.swing.JLabel jLabel_footer;
    private javax.swing.JLabel jLabel_mail;
    private javax.swing.JLabel jLabel_mail1;
    private javax.swing.JLabel jLabel_telefono;
    private javax.swing.JScrollPane jScrollPane_equipos;
    private javax.swing.JTable jTable_ventas;
    private javax.swing.JTextField txt_direccion;
    private javax.swing.JTextField txt_lucro_total;
    private javax.swing.JTextField txt_mail;
    private javax.swing.JTextField txt_nombre;
    private javax.swing.JTextField txt_telefono;
    private javax.swing.JTextField txt_total_venta;
    private javax.swing.JTextField txt_totalventas;
    // End of variables declaration//GEN-END:variables

    public void Limpiar() {
        txt_nombre.setText("");
        txt_mail.setText("");
        txt_telefono.setText("");
        txt_direccion.setText("");
        jDateCumpleanos.setDate(null);
        jDateRegistro.setDate(null);
    }
}
