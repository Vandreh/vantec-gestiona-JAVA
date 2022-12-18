/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import clases.Conexion;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
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
public class RegistrarVenta extends javax.swing.JFrame {

    String user = "";
    int IDcliente_update = 0, IDventa_update = 0;
    DefaultTableModel estoque;
    DefaultTableModel venta;
    DefaultTableModel venta1;
    TableRowSorter trs1;

    /**
     * Creates new form RegistrarVenta
     */
    public RegistrarVenta() {
        initComponents();
        user = Login.user;
        IDcliente_update = GestionarClientes.IDcliente_update;
        IDventa_update = Informacion_Cliente.IDventa_update;
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Registrar venta - Sesión de " + user);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        estoque = new DefaultTableModel();
        venta = new DefaultTableModel();
        venta1 = new DefaultTableModel();

        venta.addColumn("ID Stock");
        venta.addColumn("Producto");
        jTable_venta.setModel(venta);
        TableColumnModel columnModel1 = jTable_venta.getColumnModel();
        columnModel1.getColumn(0).setMaxWidth(100);
        columnModel1.getColumn(0).setPreferredWidth(150);

        venta1.addColumn("ID Stock");
        venta1.addColumn("Tipo Producto");
        venta1.addColumn("Producto");
        venta1.addColumn("Precio Compra");
        venta1.addColumn("Precio Venta");
        venta1.addColumn("Vencimiento");
        venta1.addColumn("Fecha Compra");
        venta1.addColumn("ID Producto");
        jTable_venta1.setModel(venta1);

        Calendar c2 = new GregorianCalendar();
        jDateEntrega.setCalendar(c2);
        jDateVenta.setCalendar(c2);

        try {

            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement(
                    "select * from venta where id_venta = '" + IDventa_update + "'");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                //setTitle("Actualizar venta " + rs.getString("cliente_venta") + " - Sesión de " + user);
                jLabel_Titulo.setText("Actualizar venta " + rs.getString("cliente_venta"));

                txt_nombreCliente.setText(rs.getString("cliente_venta"));
                txt_producto.setText(rs.getString("producto_venta"));
                txt_descuento.setText(rs.getString("descuento_venta"));
                txt_delivery.setText(rs.getString("delivery"));
                txt_precio.setText(rs.getString("precio_compra"));
                txt_precio_venta.setText(rs.getString("precio_venta"));
                jTextPane_observaciones.setText(rs.getString("observaciones"));
                cmb_tipoProducto.setSelectedItem(rs.getString("tipo_producto"));
                txt_id_estoque.setText(rs.getString("id_estoque"));
                Date entrega = rs.getDate("fecha_entrega");
                jDateEntrega.setDate(entrega);
                Date DateVenta = rs.getDate("fecha_venta");
                jDateVenta.setDate(DateVenta);
                Date vencimiento = rs.getDate("vencimiento");
                jDateVencimiento.setDate(vencimiento);
                txt_id_producto.setText(rs.getString("id_producto"));
                Date compra = rs.getDate("fecha_compra");
                jDateCompra.setDate(compra);

            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error en cargar la actualizacion de la venta." + e);
            JOptionPane.showMessageDialog(
                    null, "¡¡ERROR al cargar la actualizacion de la venta!!, contacte al administrador.");
        }

        try {
            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement(
                    "select nombre_cliente from clientes where id_cliente = '" + IDcliente_update + "'");
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                //setTitle("Registro de venta: " + rs.getString("nombre_cliente") + " - Sesión de " + user);
                jLabel_Titulo.setText("Registro de ventas para el Cliente: " + rs.getString("nombre_cliente"));

                txt_nombreCliente.setText(rs.getString("nombre_cliente"));
            }
            cn.close();
        } catch (SQLException e) {
            System.err.println("Error en cargar usuario." + e);
            JOptionPane.showMessageDialog(null, "¡¡ERROR al cargar!!, contacte al administrador.");
        }

        try {
            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_estoque, tipo_producto, producto, precio_producto, DATE_FORMAT(vencimiento, '%d-%m-%Y'), DATE_FORMAT(fecha_compra, '%d-%m-%Y'), id_producto from estoque");
            ResultSet rs = pst.executeQuery();

            jTable_estoque = new JTable(estoque);
            jScrollPane_estoque.setViewportView(jTable_estoque);

            estoque.addColumn("ID Stock");
            estoque.addColumn("Tipo Producto");
            estoque.addColumn("Producto");
            estoque.addColumn("Precio Compra");
            estoque.addColumn("Vencimiento");
            estoque.addColumn("Fecha Compra");
            estoque.addColumn("ID Producto");

            while (rs.next()) {
                Object[] fila = new Object[7];
                for (int i = 0; i < 7; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                estoque.addRow(fila);
            }
            cn.close();
            trs1 = new TableRowSorter(estoque);
            jTable_estoque.setRowSorter(trs1);

            int contar = (jTable_estoque.getRowCount());
            String contarstr = String.valueOf(contar);
            txt_totalestoque.setText(contarstr);

        } catch (SQLException e) {
            System.err.println("Error en el llenado de la tabla de estoque.");
        }

        TableColumnModel columnModel = jTable_estoque.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(100);
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setMaxWidth(300);
        columnModel.getColumn(1).setPreferredWidth(120);
        columnModel.getColumn(2).setPreferredWidth(350);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(3).setMaxWidth(200);
        columnModel.getColumn(6).setMaxWidth(100);
        columnModel.getColumn(6).setPreferredWidth(50);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable_estoque.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        jTable_estoque.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        jTable_estoque.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        jTable_estoque.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        jTable_estoque.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int id_producto = (int) jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 6);

                if (id_producto > 0) {

                    try {
                        txt_precio_venta.setText("");
                        Connection cn = Conexion.conectar();
                        PreparedStatement pst = cn.prepareStatement("select precio from productos where id_producto = '" + id_producto + "'");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            String resultado = rs.getString("precio");
                            txt_precio_venta.setText(resultado);
                        }
                        cn.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "No fue posible cargar el precio de venta del producto. Contarctar al administrador");
                    }

                    try {
                        txt_id_producto.setText("");
                        txt_producto.setText("");
                        txt_precio.setText("");
                        jDateVencimiento.setDate(null);
                        txt_id_estoque.setText("");
                        txt_id_estoque.setText(jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 0).toString());
                        cmb_tipoProducto.setSelectedItem(jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 1).toString());
                        txt_producto.setText(jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 2).toString());
                        txt_precio.setText(jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 3).toString());

                        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

                        String date = (String) jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 4);
                        jDateVencimiento.setDate(formato.parse(date));

                        String date_compra = (String) jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 5);
                        jDateCompra.setDate(formato.parse(date_compra));

                        txt_id_producto.setText(jTable_estoque.getValueAt(jTable_estoque.getSelectedRow(), 6).toString());

                    } catch (ParseException ex) {
                        Logger.getLogger(RegistrarVenta.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
        );

        jTable_venta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = jTable_venta.rowAtPoint(e.getPoint());
                if (fila_point > -1) {
                    venta.removeRow(fila_point);
                    venta1.removeRow(fila_point);
                }
            }
        });

        jTable_venta1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = jTable_venta1.rowAtPoint(e.getPoint());

                if (fila_point > -1) {
                    venta.removeRow(fila_point);
                    venta1.removeRow(fila_point);
                    int contar = (jTable_venta1.getRowCount());
                    String contarstr = String.valueOf(contar);
                    txt_totalproductos.setText(contarstr);
                }
            }
        });
    }

    public String fecha_venta() {
        String fecha_venta = null;
        try {
            int diaVenta, mesVenta, anoVenta;
            diaVenta = jDateVenta.getCalendar().get(Calendar.DAY_OF_MONTH);
            mesVenta = jDateVenta.getCalendar().get(Calendar.MONTH) + 1;
            anoVenta = jDateVenta.getCalendar().get(Calendar.YEAR);
            fecha_venta = anoVenta + "." + mesVenta + "." + diaVenta;
        } catch (Exception e) {
        }
        return fecha_venta;
    }

    public String fecha_entrega() {
        String fecha_entrega = null;
        try {
            int diaEntrega, mesEntrega, anoEntrega;
            diaEntrega = jDateEntrega.getCalendar().get(Calendar.DAY_OF_MONTH);
            mesEntrega = jDateEntrega.getCalendar().get(Calendar.MONTH) + 1;
            anoEntrega = jDateEntrega.getCalendar().get(Calendar.YEAR);
            fecha_entrega = anoEntrega + "." + mesEntrega + "." + diaEntrega;
        } catch (Exception e) {
        }
        return fecha_entrega;
    }

    public String vencimiento() {
        String vencimiento = null;
        try {
            int diaVencimiento, mesVencimiento, anoVencimiento;
            diaVencimiento = jDateVencimiento.getCalendar().get(Calendar.DAY_OF_MONTH);
            mesVencimiento = jDateVencimiento.getCalendar().get(Calendar.MONTH) + 1;
            anoVencimiento = jDateVencimiento.getCalendar().get(Calendar.YEAR);
            vencimiento = anoVencimiento + "." + mesVencimiento + "." + diaVencimiento;
        } catch (Exception e) {
        }
        return vencimiento;
    }

    public String Compra() {
        String compra = null;
        try {
            int diaCompra, mesCompra, anoCompra;
            diaCompra = jDateCompra.getCalendar().get(Calendar.DAY_OF_MONTH);
            mesCompra = jDateCompra.getCalendar().get(Calendar.MONTH) + 1;
            anoCompra = jDateCompra.getCalendar().get(Calendar.YEAR);
            compra = anoCompra + "." + mesCompra + "." + diaCompra;
        } catch (Exception e) {
        }
        return compra;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDayChooser1 = new com.toedter.calendar.JDayChooser();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel_footer = new javax.swing.JLabel();
        jScrollPane_estoque = new javax.swing.JScrollPane();
        jTable_estoque = new javax.swing.JTable();
        jLabel_Nombre10 = new javax.swing.JLabel();
        txt_precio = new javax.swing.JTextField();
        jLabel_Nombre12 = new javax.swing.JLabel();
        txt_id_estoque = new javax.swing.JTextField();
        jLabel_Nombre13 = new javax.swing.JLabel();
        txt_precio_venta = new javax.swing.JTextField();
        jLabel_Nombre14 = new javax.swing.JLabel();
        txt_filtro = new javax.swing.JTextField();
        jLabel_Nombre7 = new javax.swing.JLabel();
        txt_descuento = new javax.swing.JTextField();
        jLabel_Nombre15 = new javax.swing.JLabel();
        jLabel_Nombre8 = new javax.swing.JLabel();
        cmb_tipoProducto = new javax.swing.JComboBox<>();
        txt_delivery = new javax.swing.JTextField();
        jLabel_Nombre9 = new javax.swing.JLabel();
        txt_producto = new javax.swing.JTextField();
        jLabel_Titulo = new javax.swing.JLabel();
        jLabel_Nombre = new javax.swing.JLabel();
        txt_nombreCliente = new javax.swing.JTextField();
        jLabel_Nombre2 = new javax.swing.JLabel();
        jDateVenta = new com.toedter.calendar.JDateChooser();
        jDateEntrega = new com.toedter.calendar.JDateChooser();
        jLabel_Nombre11 = new javax.swing.JLabel();
        jLabel_Nombre5 = new javax.swing.JLabel();
        jDateVencimiento = new com.toedter.calendar.JDateChooser();
        jButton_Eliminar = new javax.swing.JButton();
        jDateCompra = new com.toedter.calendar.JDateChooser();
        jLabel_Nombre1 = new javax.swing.JLabel();
        jLabel_Nombre4 = new javax.swing.JLabel();
        jLabel_Nombre3 = new javax.swing.JLabel();
        jLabel_Nombre16 = new javax.swing.JLabel();
        jButton_Actualizar = new javax.swing.JButton();
        jButton_Registrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane_observaciones = new javax.swing.JTextPane();
        txt_id_producto = new javax.swing.JTextField();
        jScrollPane_estoque2 = new javax.swing.JScrollPane();
        jTable_venta = new javax.swing.JTable();
        jButton_Agregar = new javax.swing.JButton();
        txt_totalestoque = new javax.swing.JTextField();
        jLabel_Nombre18 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane_estoque3 = new javax.swing.JScrollPane();
        jTable_venta1 = new javax.swing.JTable();
        jLabel_footer1 = new javax.swing.JLabel();
        jLabel_Titulo1 = new javax.swing.JLabel();
        txt_totalproductos = new javax.swing.JTextField();
        jLabel_Nombre17 = new javax.swing.JLabel();
        jLabel_Nombre19 = new javax.swing.JLabel();
        txt_preciototal = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 218, 225));

        jLabel_footer.setText("Creado por Vandreh Esmeraldo ®");

        jTable_estoque.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane_estoque.setViewportView(jTable_estoque);

        jLabel_Nombre10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre10.setText("Precio compra:");

        txt_precio.setBackground(new java.awt.Color(153, 153, 255));
        txt_precio.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_precio.setForeground(new java.awt.Color(255, 255, 255));
        txt_precio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_precio.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre12.setText("Nº del producto en stock:");

        txt_id_estoque.setBackground(new java.awt.Color(153, 153, 255));
        txt_id_estoque.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_id_estoque.setForeground(new java.awt.Color(255, 255, 255));
        txt_id_estoque.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_id_estoque.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre13.setText("Precio venta:");

        txt_precio_venta.setBackground(new java.awt.Color(153, 153, 255));
        txt_precio_venta.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_precio_venta.setForeground(new java.awt.Color(255, 255, 255));
        txt_precio_venta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_precio_venta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre14.setText("Filtrar:");

        txt_filtro.setBackground(new java.awt.Color(153, 153, 255));
        txt_filtro.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_filtro.setForeground(new java.awt.Color(255, 255, 255));
        txt_filtro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_filtro.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txt_filtro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_filtroKeyTyped(evt);
            }
        });

        jLabel_Nombre7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre7.setText("Descuento $:");

        txt_descuento.setBackground(new java.awt.Color(153, 153, 255));
        txt_descuento.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_descuento.setForeground(new java.awt.Color(255, 255, 255));
        txt_descuento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_descuento.setText("0");
        txt_descuento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre15.setText("Stock:");

        jLabel_Nombre8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre8.setText("Delivery $:");

        cmb_tipoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cuidado de la piel", "Maquillajes", "Fragrancias", "accesorios", "Muestras" }));

        txt_delivery.setBackground(new java.awt.Color(153, 153, 255));
        txt_delivery.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_delivery.setForeground(new java.awt.Color(255, 255, 255));
        txt_delivery.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_delivery.setText("0");
        txt_delivery.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre9.setText("Tipo de Producto:");

        txt_producto.setBackground(new java.awt.Color(153, 153, 255));
        txt_producto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_producto.setForeground(new java.awt.Color(255, 255, 255));
        txt_producto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_producto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Titulo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel_Titulo.setText("Registro de venta");

        jLabel_Nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre.setText("Nombre del cliente:");

        txt_nombreCliente.setBackground(new java.awt.Color(153, 153, 255));
        txt_nombreCliente.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_nombreCliente.setForeground(new java.awt.Color(255, 255, 255));
        txt_nombreCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_nombreCliente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre2.setText("Producto:");

        jDateVenta.setDateFormatString("dd.MM.yyyy");

        jDateEntrega.setDateFormatString("dd.MM.yyyy");

        jLabel_Nombre11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre11.setText("Fecha de vencimiento:");

        jLabel_Nombre5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre5.setText("Observaciones:");

        jDateVencimiento.setDateFormatString("dd.MM.yyyy");

        jButton_Eliminar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Eliminar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Eliminar.setText("Eliminar venta");
        jButton_Eliminar.setBorder(null);
        jButton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarActionPerformed(evt);
            }
        });

        jDateCompra.setDateFormatString("dd.MM.yyyy");

        jLabel_Nombre1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre1.setText("Fecha de venta:");

        jLabel_Nombre4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre4.setText("Fecha de compra:");

        jLabel_Nombre3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre3.setText("Fecha de entrega:");

        jLabel_Nombre16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre16.setText("ID producto:");

        jButton_Actualizar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Actualizar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Actualizar.setText("Actualizar venta");
        jButton_Actualizar.setBorder(null);
        jButton_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ActualizarActionPerformed(evt);
            }
        });

        jButton_Registrar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Registrar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Registrar.setText("Registrar venta");
        jButton_Registrar.setBorder(null);
        jButton_Registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RegistrarActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jTextPane_observaciones);

        txt_id_producto.setBackground(new java.awt.Color(153, 153, 255));
        txt_id_producto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_id_producto.setForeground(new java.awt.Color(255, 255, 255));
        txt_id_producto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_id_producto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTable_venta.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane_estoque2.setViewportView(jTable_venta);

        jButton_Agregar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Agregar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Agregar.setText("Agregar producto");
        jButton_Agregar.setBorder(null);
        jButton_Agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AgregarActionPerformed(evt);
            }
        });

        txt_totalestoque.setBackground(new java.awt.Color(153, 153, 255));
        txt_totalestoque.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_totalestoque.setForeground(new java.awt.Color(255, 255, 255));
        txt_totalestoque.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_totalestoque.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre18.setText("Total de productos en stock: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel_Nombre18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_totalestoque, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane_estoque, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_footer)
                        .addContainerGap())
                    .addComponent(jScrollPane_estoque2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel_Nombre2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel_Nombre14)
                                .addGap(975, 975, 975))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Nombre)
                                    .addComponent(txt_nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 458, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Nombre5)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel_Titulo)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(txt_delivery, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_id_estoque, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel_Nombre7)
                                                .addGap(55, 55, 55)
                                                .addComponent(jLabel_Nombre9))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txt_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel_Nombre8))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel_Nombre12)
                                                    .addComponent(cmb_tipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel_Nombre13)
                                            .addComponent(txt_precio_venta, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel_Nombre10)
                                            .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel_Nombre16)
                                            .addComponent(txt_id_producto, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(77, 77, 77)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Nombre4)
                                    .addComponent(jDateCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_Nombre1)
                                    .addComponent(jDateVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Nombre3)
                                    .addComponent(jDateEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel_Nombre11)
                                    .addComponent(jDateVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(47, 47, 47))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_Nombre15)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_Titulo)
                        .addGap(23, 23, 23)
                        .addComponent(jLabel_Nombre)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_Nombre16)
                            .addComponent(jLabel_Nombre2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_id_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel_Nombre5)
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel_Nombre3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateEntrega, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_Nombre11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel_Nombre4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_Nombre1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_Nombre9)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel_Nombre7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel_Nombre10)
                                        .addGap(11, 11, 11)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmb_tipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel_Nombre8)
                                    .addComponent(jLabel_Nombre12)
                                    .addComponent(jLabel_Nombre13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_delivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_id_estoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_precio_venta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addComponent(jLabel_Nombre14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Nombre15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane_estoque2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jScrollPane_estoque, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Nombre18)
                    .addComponent(txt_totalestoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_footer, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("REGISTRO DE VENTA", jPanel1);

        jTable_venta1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane_estoque3.setViewportView(jTable_venta1);

        jLabel_footer1.setText("Creado por Vandreh Esmeraldo ®");

        jLabel_Titulo1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel_Titulo1.setText("Carrito de la venta");

        txt_totalproductos.setBackground(new java.awt.Color(153, 153, 255));
        txt_totalproductos.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_totalproductos.setForeground(new java.awt.Color(255, 255, 255));
        txt_totalproductos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_totalproductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre17.setText("TOTAL DE PRODUCTOS: ");

        jLabel_Nombre19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre19.setText("PRECIO DE LA VENTA: $ ");

        txt_preciototal.setBackground(new java.awt.Color(153, 153, 255));
        txt_preciototal.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_preciototal.setForeground(new java.awt.Color(255, 255, 255));
        txt_preciototal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_preciototal.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel_Nombre17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_totalproductos, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel_Nombre19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_preciototal, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(222, 222, 222)
                                .addComponent(jLabel_footer1))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel_Titulo1)))
                        .addGap(0, 98, Short.MAX_VALUE))
                    .addComponent(jScrollPane_estoque3))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel_Titulo1)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane_estoque3, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_Nombre17)
                    .addComponent(txt_totalproductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Nombre19)
                    .addComponent(txt_preciototal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_footer1))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CARRITO DE VENTA:", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_RegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RegistrarActionPerformed

        int validacion = 0;
        String id_producto, cliente, producto, descuento, delivery, precio_compra, precio_venta, observaciones;

        id_producto = txt_id_producto.getText().trim();
        cliente = txt_nombreCliente.getText().trim();
        producto = txt_producto.getText().trim();
        descuento = txt_descuento.getText().trim();
        delivery = txt_delivery.getText().trim();
        precio_compra = txt_precio.getText().trim();
        precio_venta = txt_precio_venta.getText().trim();
        observaciones = jTextPane_observaciones.getText();

        if (Compra() == null) {
            jDateVencimiento.setBackground(Color.red);
            validacion++;
        }
        if (vencimiento() == null) {
            jDateVencimiento.setBackground(Color.red);
            validacion++;
        }
        if (fecha_entrega() == null) {
            jDateEntrega.setBackground(Color.red);
            validacion++;
        }
        if (fecha_venta() == null) {
            jDateVenta.setBackground(Color.red);
            validacion++;
        }
        if (id_producto.equals("")) {
            txt_id_producto.setBackground(Color.red);
            validacion++;
        }
        if (cliente.equals("")) {
            txt_nombreCliente.setBackground(Color.red);
            validacion++;
        }
        if (producto.equals("")) {
            txt_producto.setBackground(Color.red);
            validacion++;
        }
        if (precio_compra.equals("")) {
            txt_precio.setBackground(Color.red);
            validacion++;
        }
        if (precio_venta.equals("")) {
            txt_precio_venta.setBackground(Color.red);
            validacion++;
        }
        if (delivery.equals("")) {
            txt_delivery.setBackground(Color.red);
            validacion++;
        }
        if (descuento.equals("")) {
            txt_descuento.setBackground(Color.red);
            validacion++;
        }
        if (observaciones.equals("")) {
            jTextPane_observaciones.setText("Sin observaciones.");
        }

        if (validacion == 0) {
            try {
                if (jTable_venta.getRowCount() > 0) {
                    for (int i = 0; i < jTable_venta.getRowCount(); i++) {

                        String stock = null;
                        String id_stock = jTable_venta.getValueAt(i, 0).toString();

                        Connection cn = Conexion.conectar();
                        PreparedStatement pst = cn.prepareStatement("select id_estoque from estoque where id_estoque = '" + id_stock + "'");
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            stock = rs.getString("id_estoque");
                        }
                        if (stock == null) {
                            JOptionPane.showMessageDialog(null, "Producto indisponible en el stock. Id del producto = " + stock);
                            validacion++;
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Producto indisponible en el stock");
                    validacion++;
                }
            } catch (HeadlessException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Producto indisponible en el stock");
                validacion++;
            }

            if (validacion == 0) {
                try {
                    Connection cn = Conexion.conectar();
                    PreparedStatement pst = cn.prepareStatement(
                            "insert into venta values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                    for (int i = 0; i < jTable_venta1.getRowCount(); i++) {

                        SimpleDateFormat formato = new SimpleDateFormat("dd.MM.yyyy");
                        String date = (String) jTable_venta1.getValueAt(i, 6);
                        jDateCompra.setDate(formato.parse(date));
                        int diaCompra, mesCompra, anoCompra;
                        String fecha_compra;
                        diaCompra = jDateCompra.getCalendar().get(Calendar.DAY_OF_MONTH);
                        mesCompra = jDateCompra.getCalendar().get(Calendar.MONTH) + 1;
                        anoCompra = jDateCompra.getCalendar().get(Calendar.YEAR);
                        fecha_compra = anoCompra + "." + mesCompra + "." + diaCompra;

                        pst.setInt(1, 0);
                        pst.setString(2, (String) jTable_venta1.getValueAt(i, 0));
                        pst.setString(3, cliente);
                        pst.setString(4, (String) jTable_venta1.getValueAt(i, 2));
                        pst.setString(5, descuento);
                        pst.setString(6, delivery);
                        pst.setString(7, fecha_entrega());
                        pst.setString(8, (String) jTable_venta1.getValueAt(i, 3));
                        pst.setString(9, (String) jTable_venta1.getValueAt(i, 4));
                        pst.setString(10, fecha_venta());
                        pst.setString(11, observaciones);
                        pst.setString(12, (String) jTable_venta1.getValueAt(i, 1));
                        pst.setString(13, vencimiento());
                        pst.setString(14, (String) jTable_venta1.getValueAt(i, 7));
                        pst.setString(15, fecha_compra);

                        pst.executeUpdate();
                    }
                    cn.close();

                } catch (SQLException e) {
                    System.err.println("Error en registrar la venta. " + e);
                    JOptionPane.showMessageDialog(null, "¡¡ERROR al registrar la venta!!, contacte al administrador.");
                    validacion++;
                } catch (ParseException ex) {
                    Logger.getLogger(RegistrarVenta.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (validacion == 0) {
                    try {
                        Connection cn = Conexion.conectar();
                        for (int i = 0; i < jTable_venta1.getRowCount(); i++) {
                            String id_stock = (String) jTable_venta1.getValueAt(i, 0);
                            PreparedStatement pst = cn.prepareStatement("delete from estoque where id_estoque ='" + id_stock + "'");
                            pst.executeUpdate();
                        }
                        cn.close();
                        JOptionPane.showMessageDialog(null, "Registro exitoso.");
                    } catch (SQLException e) {
                        System.err.println("Error al eliminar producto del estoque. " + e);
                        JOptionPane.showMessageDialog(null, "¡¡ERROR al eliminar producto del estoque!! Contacte al administrador.");
                        validacion++;
                    }
                }

                if (validacion == 0) {
                    try {
                        estoque.setRowCount(0);
                        Connection cn = Conexion.conectar();
                        PreparedStatement pst = cn.prepareStatement("select id_estoque, tipo_producto, producto, precio_producto, DATE_FORMAT(vencimiento, '%d-%m-%Y'), DATE_FORMAT(fecha_compra, '%d-%m-%Y'), id_producto from estoque");
                        ResultSet rs = pst.executeQuery();

                        while (rs.next()) {
                            Object[] fila = new Object[7];
                            for (int i = 0; i < 7; i++) {
                                fila[i] = rs.getObject(i + 1);
                            }
                            estoque.addRow(fila);
                        }
                        cn.close();
                        estoque.fireTableDataChanged();
                    } catch (SQLException e) {
                        System.err.println("Error en el llenado de la tabla de estoque.");
                        validacion++;
                    }
                }

                TableColumnModel columnModel = jTable_estoque.getColumnModel();
                columnModel.getColumn(0).setMaxWidth(100);
                columnModel.getColumn(0).setPreferredWidth(100);
                columnModel.getColumn(1).setMaxWidth(300);
                columnModel.getColumn(1).setPreferredWidth(200);
                columnModel.getColumn(2).setPreferredWidth(500);
                columnModel.getColumn(3).setPreferredWidth(100);
                columnModel.getColumn(3).setMaxWidth(200);
                DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                jTable_estoque.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
                jTable_estoque.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

                venta.setRowCount(0);
                venta1.setRowCount(0);

                try {
                    estoque.setRowCount(0);
                    Connection cn = Conexion.conectar();
                    PreparedStatement pst = cn.prepareStatement("select id_estoque, tipo_producto, producto, precio_producto, DATE_FORMAT(vencimiento, '%d-%m-%Y'), DATE_FORMAT(fecha_compra, '%d-%m-%Y'), id_producto from estoque");
                    ResultSet rs = pst.executeQuery();

                    while (rs.next()) {
                        Object[] fila = new Object[7];
                        for (int i = 0; i < 7; i++) {
                            fila[i] = rs.getObject(i + 1);
                        }
                        estoque.addRow(fila);
                    }
                    cn.close();
                    trs1 = new TableRowSorter(estoque);
                    jTable_estoque.setRowSorter(trs1);

                    int contar = (jTable_estoque.getRowCount());
                    String contarstr = String.valueOf(contar);
                    txt_totalestoque.setText(contarstr);

                } catch (SQLException e) {
                    System.err.println("Error en el llenado de la tabla de estoque.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos correctamente.");
        }

    }//GEN-LAST:event_jButton_RegistrarActionPerformed

    private void jButton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActualizarActionPerformed

        int validacion = 0;
        String precio_venta, tipo_producto, cliente, producto, descuento, delivery, precio, observaciones;

        cliente = txt_nombreCliente.getText().trim();
        producto = txt_producto.getText().trim();
        descuento = txt_descuento.getText().trim();
        delivery = txt_delivery.getText().trim();
        precio = txt_precio.getText().trim();
        precio_venta = txt_precio_venta.getText().trim();
        tipo_producto = cmb_tipoProducto.getSelectedItem().toString();
        observaciones = jTextPane_observaciones.getText();

        if (vencimiento() == null) {
            jDateVencimiento.setBackground(Color.red);
            validacion++;
        }
        if (fecha_entrega() == null) {
            jDateEntrega.setBackground(Color.red);
            validacion++;
        }
        if (fecha_venta() == null) {
            jDateVenta.setBackground(Color.red);
            validacion++;
        }
        if (cliente.equals("")) {
            txt_nombreCliente.setBackground(Color.red);
            validacion++;
        }
        if (producto.equals("")) {
            txt_producto.setBackground(Color.red);
            validacion++;
        }
        if (precio.equals("")) {
            txt_precio.setBackground(Color.red);
            validacion++;
        }
        if (precio_venta.equals("")) {
            txt_precio_venta.setBackground(Color.red);
            validacion++;
        }
        if (delivery.equals("")) {
            txt_delivery.setBackground(Color.red);
            validacion++;
        }
        if (descuento.equals("")) {
            txt_descuento.setBackground(Color.red);
            validacion++;
        }
        if (observaciones.equals("")) {
            jTextPane_observaciones.setText("Sin observaciones.");
        }

        if (validacion == 0) {

            try {

                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement(
                        "update venta set cliente_venta=?, producto_venta=?, descuento_venta=?,"
                        + " delivery=?, fecha_entrega=?, precio_compra=?, precio_venta=?, fecha_venta=?,"
                        + " observaciones=?, tipo_producto=?, vencimiento=? where id_venta = '" + IDventa_update + "'");

                pst.setString(1, cliente);
                pst.setString(2, producto);
                pst.setString(3, descuento);
                pst.setString(4, delivery);
                pst.setString(5, fecha_entrega());
                pst.setString(6, precio);
                pst.setString(7, precio_venta);
                pst.setString(8, fecha_venta());
                pst.setString(9, observaciones);
                pst.setString(10, tipo_producto);
                pst.setString(11, vencimiento());

                pst.executeUpdate();
                cn.close();

                Limpiar();

                JOptionPane.showMessageDialog(null, "Actualización correcta.");

            } catch (SQLException e) {
                System.err.println("Error en actualizar la venta." + e);
                JOptionPane.showMessageDialog(null, "¡¡ERROR al actualizar la venta!! Contacte al administrador.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos.");
        }
    }//GEN-LAST:event_jButton_ActualizarActionPerformed
    TableRowSorter trs = null;
    private void txt_filtroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filtroKeyTyped

        txt_filtro.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent ke) {
                trs1.setRowFilter(RowFilter.regexFilter("(?i)" + txt_filtro.getText(), 2));
            }
        });

        trs1 = new TableRowSorter(estoque);
        jTable_estoque.setRowSorter(trs1);

    }//GEN-LAST:event_txt_filtroKeyTyped

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed

        String id_Venta = null;

        int valor = JOptionPane.showConfirmDialog(this, "¿Esta seguro que deseas borrar esta venta?", "Advertencia", JOptionPane.YES_NO_OPTION);
        if (valor == JOptionPane.YES_OPTION) {
            try {

                Connection cn = Conexion.conectar();
                PreparedStatement ppst = cn.prepareStatement(
                        "select * from venta where id_venta = '" + IDventa_update + "'");
                ResultSet rs = ppst.executeQuery();
                if (rs.next()) {
                    id_Venta = rs.getString("id_venta");
                }

                String id_estoque, id_producto, tipo_producto, cliente, producto, descuento, delivery, precio_compra, precio_venta, observaciones;

                id_producto = txt_id_producto.getText().trim();
                cliente = txt_nombreCliente.getText().trim();
                producto = txt_producto.getText().trim();
                descuento = txt_descuento.getText().trim();
                delivery = txt_delivery.getText().trim();
                precio_compra = txt_precio.getText().trim();
                precio_venta = txt_precio_venta.getText().trim();
                tipo_producto = cmb_tipoProducto.getSelectedItem().toString();
                observaciones = jTextPane_observaciones.getText();
                id_estoque = txt_id_estoque.getText().trim();

                PreparedStatement pst = cn.prepareStatement(
                        "insert into venta_eliminada values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                pst.setString(1, id_Venta);
                pst.setString(2, id_estoque);
                pst.setString(3, cliente);
                pst.setString(4, producto);
                pst.setString(5, descuento);
                pst.setString(6, delivery);
                pst.setString(7, fecha_entrega());
                pst.setString(8, precio_compra);
                pst.setString(9, precio_venta);
                pst.setString(10, fecha_venta());
                pst.setString(11, observaciones);
                pst.setString(12, tipo_producto);
                pst.setString(13, vencimiento());
                pst.setString(14, id_producto);
                pst.setString(15, Compra());

                pst.executeUpdate();

                PreparedStatement psmt = cn.prepareStatement(
                        "insert into estoque values (?,?,?,?,?,?,?)");

                psmt.setString(1, id_estoque);
                psmt.setString(2, id_producto);
                psmt.setString(3, tipo_producto);
                psmt.setString(4, producto);
                psmt.setString(5, precio_compra);
                psmt.setString(6, vencimiento());
                psmt.setString(7, Compra());

                psmt.executeUpdate();

                PreparedStatement ppsmt = cn.prepareStatement(
                        "delete from venta where id_venta = '" + IDventa_update + "'");
                ppsmt.executeUpdate();

                cn.close();

            } catch (SQLException e) {
                System.err.println("Error en eliminar la venta. " + e);
                JOptionPane.showMessageDialog(null, "¡¡ERROR al eliminar la venta!!, contacte al administrador.");
            }

            try {
                estoque.setRowCount(0);
                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement("select id_estoque, tipo_producto, producto, precio_producto, DATE_FORMAT(vencimiento, '%d-%m-%Y') from estoque");
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Object[] fila = new Object[5];
                    for (int i = 0; i < 5; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    estoque.addRow(fila);
                }
                cn.close();
                estoque.fireTableDataChanged();
            } catch (SQLException e) {
                System.err.println("Error en el llenado de la tabla de estoque.");
            }

            TableColumnModel columnModel = jTable_estoque.getColumnModel();
            columnModel.getColumn(0).setMaxWidth(100);
            columnModel.getColumn(0).setPreferredWidth(100);
            columnModel.getColumn(1).setMaxWidth(300);
            columnModel.getColumn(1).setPreferredWidth(200);
            columnModel.getColumn(2).setPreferredWidth(500);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(3).setMaxWidth(200);
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            jTable_estoque.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
            jTable_estoque.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

            JOptionPane.showMessageDialog(null, "Venta eliminada correctamente.");

            try {
                estoque.setRowCount(0);
                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement("select id_estoque, tipo_producto, producto, precio_producto, DATE_FORMAT(vencimiento, '%d-%m-%Y'), DATE_FORMAT(fecha_compra, '%d-%m-%Y'), id_producto from estoque");
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Object[] fila = new Object[7];
                    for (int i = 0; i < 7; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    estoque.addRow(fila);
                }
                cn.close();
                trs1 = new TableRowSorter(estoque);
                jTable_estoque.setRowSorter(trs1);

                int contar = (jTable_estoque.getRowCount());
                String contarstr = String.valueOf(contar);
                txt_totalestoque.setText(contarstr);

            } catch (SQLException e) {
                System.err.println("Error en el llenado de la tabla de estoque.");
            }
        }
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void jButton_AgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AgregarActionPerformed

        String fecha_Vencimiento = null;
        try {
            int diaVencimiento, mesVencimiento, anoVencimiento;
            diaVencimiento = jDateVencimiento.getCalendar().get(Calendar.DAY_OF_MONTH);
            mesVencimiento = jDateVencimiento.getCalendar().get(Calendar.MONTH) + 1;
            anoVencimiento = jDateVencimiento.getCalendar().get(Calendar.YEAR);
            fecha_Vencimiento = diaVencimiento + "." + mesVencimiento + "." + anoVencimiento;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Debes llenar la fecha de vencimiento");
        }

        String fecha_Compra = null;
        try {
            int diaCompra, mesCompra, anoCompra;
            diaCompra = jDateCompra.getCalendar().get(Calendar.DAY_OF_MONTH);
            mesCompra = jDateCompra.getCalendar().get(Calendar.MONTH) + 1;
            anoCompra = jDateCompra.getCalendar().get(Calendar.YEAR);
            fecha_Compra = diaCompra + "." + mesCompra + "." + anoCompra;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Debes llenar la fecha de compra");
        }

        String[] Datos = new String[2];
        Datos[0] = txt_id_estoque.getText();
        Datos[1] = txt_producto.getText();
        venta.addRow(Datos);

        String[] Datos1 = new String[8];
        Datos1[0] = txt_id_estoque.getText();
        Datos1[1] = cmb_tipoProducto.getSelectedItem().toString();
        Datos1[2] = txt_producto.getText();
        Datos1[3] = txt_precio.getText();
        Datos1[4] = txt_precio_venta.getText();
        Datos1[5] = fecha_Vencimiento;
        Datos1[6] = fecha_Compra;
        Datos1[7] = txt_id_producto.getText();
        venta1.addRow(Datos1);

        int contar = (jTable_venta1.getRowCount());
        String contarstr = String.valueOf(contar);
        txt_totalproductos.setText(contarstr);

        float suma = 0, sumai;
        for (int i = 0; i < contar; i++) {
            sumai = Float.parseFloat((String) jTable_venta1.getValueAt(i, 4));
            suma = suma + sumai;
        }
        String sumaStr = String.valueOf(suma);
        txt_preciototal.setText(sumaStr);

    }//GEN-LAST:event_jButton_AgregarActionPerformed

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
            java.util.logging.Logger.getLogger(RegistrarVenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistrarVenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrarVenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistrarVenta.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RegistrarVenta().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmb_tipoProducto;
    private javax.swing.JButton jButton_Actualizar;
    private javax.swing.JButton jButton_Agregar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JButton jButton_Registrar;
    private com.toedter.calendar.JDateChooser jDateCompra;
    private com.toedter.calendar.JDateChooser jDateEntrega;
    private com.toedter.calendar.JDateChooser jDateVencimiento;
    private com.toedter.calendar.JDateChooser jDateVenta;
    private com.toedter.calendar.JDayChooser jDayChooser1;
    private javax.swing.JLabel jLabel_Nombre;
    private javax.swing.JLabel jLabel_Nombre1;
    private javax.swing.JLabel jLabel_Nombre10;
    private javax.swing.JLabel jLabel_Nombre11;
    private javax.swing.JLabel jLabel_Nombre12;
    private javax.swing.JLabel jLabel_Nombre13;
    private javax.swing.JLabel jLabel_Nombre14;
    private javax.swing.JLabel jLabel_Nombre15;
    private javax.swing.JLabel jLabel_Nombre16;
    private javax.swing.JLabel jLabel_Nombre17;
    private javax.swing.JLabel jLabel_Nombre18;
    private javax.swing.JLabel jLabel_Nombre19;
    private javax.swing.JLabel jLabel_Nombre2;
    private javax.swing.JLabel jLabel_Nombre3;
    private javax.swing.JLabel jLabel_Nombre4;
    private javax.swing.JLabel jLabel_Nombre5;
    private javax.swing.JLabel jLabel_Nombre7;
    private javax.swing.JLabel jLabel_Nombre8;
    private javax.swing.JLabel jLabel_Nombre9;
    private javax.swing.JLabel jLabel_Titulo;
    private javax.swing.JLabel jLabel_Titulo1;
    private javax.swing.JLabel jLabel_footer;
    private javax.swing.JLabel jLabel_footer1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane_estoque;
    private javax.swing.JScrollPane jScrollPane_estoque2;
    private javax.swing.JScrollPane jScrollPane_estoque3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable_estoque;
    private javax.swing.JTable jTable_venta;
    private javax.swing.JTable jTable_venta1;
    private javax.swing.JTextPane jTextPane_observaciones;
    private javax.swing.JTextField txt_delivery;
    private javax.swing.JTextField txt_descuento;
    private javax.swing.JTextField txt_filtro;
    private javax.swing.JTextField txt_id_estoque;
    private javax.swing.JTextField txt_id_producto;
    private javax.swing.JTextField txt_nombreCliente;
    private javax.swing.JTextField txt_precio;
    private javax.swing.JTextField txt_precio_venta;
    private javax.swing.JTextField txt_preciototal;
    private javax.swing.JTextField txt_producto;
    private javax.swing.JTextField txt_totalestoque;
    private javax.swing.JTextField txt_totalproductos;
    // End of variables declaration//GEN-END:variables

    public void Limpiar() {
        txt_nombreCliente.setText("");
        txt_producto.setText("");
        txt_descuento.setText("");
        txt_delivery.setText("");
        txt_precio.setText("");
        txt_precio_venta.setText("");
    }
}
