/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ventanas;

import clases.Conexion;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
public class RegistrarCompra extends javax.swing.JFrame {

    String user;
    public static int IDproducto_update = 0;
    DefaultTableModel productos = new DefaultTableModel();
    DefaultTableModel compra;
    TableRowSorter trs1;

    /**
     * Creates new form RegistrarProducto
     */
    public RegistrarCompra() {
        initComponents();
        user = Login.user;
        setExtendedState(MAXIMIZED_BOTH);
        setTitle("Registrar nueva compra - Sesión de " + user);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        Calendar c2 = new GregorianCalendar();
        jDate_Compra.setCalendar(c2);
        jDate_Recepcion.setCalendar(c2);
        c2.add(Calendar.YEAR, 1);
        jDateVencimiento.setCalendar(c2);

        compra = new DefaultTableModel();
        compra.addColumn("ID Producto");
        compra.addColumn("Tipo de Producto");
        compra.addColumn("Producto");
        compra.addColumn("Precio");
        compra.addColumn("Vencimiento");
        jTable_compra.setModel(compra);

        try {
            Connection cn = Conexion.conectar();
            PreparedStatement pst = cn.prepareStatement("select id_producto, tipo_producto, producto, precio from productos");
            ResultSet rs = pst.executeQuery();

            jTable_productos = new JTable(productos);
            jScrollPane_equipos.setViewportView(jTable_productos);

            productos.addColumn("ID producto");
            productos.addColumn("Tipo de producto");
            productos.addColumn("Producto");
            productos.addColumn("Precio");

            while (rs.next()) {
                Object[] fila = new Object[4];

                for (int i = 0; i < 4; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                productos.addRow(fila);
            }
            cn.close();
            trs1 = new TableRowSorter(productos);
            jTable_productos.setRowSorter(trs1);
        } catch (SQLException e) {
            System.err.println("Error en el llenado de la tabla de productos");
        }

        TableColumnModel columnModel = jTable_productos.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(100);
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setMaxWidth(300);
        columnModel.getColumn(1).setPreferredWidth(130);
        columnModel.getColumn(3).setMaxWidth(100);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTable_productos.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);

        jTable_productos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = (int) jTable_productos.getValueAt(jTable_productos.getSelectedRow(), 0);

                if (fila_point > -1) {
                    txt_idproducto.setText("");
                    txt_producto.setText("");
                    txt_PrecioProducto.setText("");
                    txt_idproducto.setText(jTable_productos.getValueAt(jTable_productos.getSelectedRow(), 0).toString());
                    cmb_tipoProducto.setSelectedItem(jTable_productos.getValueAt(jTable_productos.getSelectedRow(), 1).toString());
                    txt_producto.setText(jTable_productos.getValueAt(jTable_productos.getSelectedRow(), 2).toString());
                    txt_PrecioProducto.setText(jTable_productos.getValueAt(jTable_productos.getSelectedRow(), 3).toString());

                }
            }
        });

        jTable_compra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila_point = jTable_compra.rowAtPoint(e.getPoint());

                if (fila_point > -1) {
                    compra.removeRow(fila_point);
                    int contar = (jTable_compra.getRowCount());
                    String contarstr = String.valueOf(contar);
                    txt_totalproductos.setText(contarstr);
                }
            }
        });

    }

    public String fecha_Compra() {
        int diaCompra, mesCompra, anoCompra;
        String fecha_compra;
        diaCompra = jDate_Compra.getCalendar().get(Calendar.DAY_OF_MONTH);
        mesCompra = jDate_Compra.getCalendar().get(Calendar.MONTH) + 1;
        anoCompra = jDate_Compra.getCalendar().get(Calendar.YEAR);
        fecha_compra = anoCompra + "." + mesCompra + "." + diaCompra;
        return fecha_compra;
    }

    public String fecha_Recepcion() {
        int diaRecepcion, mesRecepcion, anoRecepcion;
        String fecha_recepcion;
        diaRecepcion = jDate_Recepcion.getCalendar().get(Calendar.DAY_OF_MONTH);
        mesRecepcion = jDate_Recepcion.getCalendar().get(Calendar.MONTH) + 1;
        anoRecepcion = jDate_Recepcion.getCalendar().get(Calendar.YEAR);
        fecha_recepcion = anoRecepcion + "." + mesRecepcion + "." + diaRecepcion;
        return fecha_recepcion;
    }

    public String fecha_Vencimiento() {
        int diaVencimiento, mesVencimiento, anoVencimiento;
        String fecha_Vencimiento;
        diaVencimiento = jDateVencimiento.getCalendar().get(Calendar.DAY_OF_MONTH);
        mesVencimiento = jDateVencimiento.getCalendar().get(Calendar.MONTH) + 1;
        anoVencimiento = jDateVencimiento.getCalendar().get(Calendar.YEAR);
        fecha_Vencimiento = anoVencimiento + "." + mesVencimiento + "." + diaVencimiento;
        return fecha_Vencimiento;
    }

    public String Precio() {
        float suma = 0, precio, sumai, flete;
        for (int i = 0; i < jTable_compra.getRowCount(); i++) {
            sumai = Float.parseFloat((String) jTable_compra.getValueAt(i, 3));
            suma = suma + sumai;
        }
        flete = Float.parseFloat(txt_flete.getText());
        precio = suma + flete;

        DecimalFormat df = new DecimalFormat("0.00");
        String precio1 = df.format(precio);

        return precio1;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane_equipos = new javax.swing.JScrollPane();
        jTable_productos = new javax.swing.JTable();
        jLabel_footer = new javax.swing.JLabel();
        jLabel_Nombre = new javax.swing.JLabel();
        cmb_tipoProducto = new javax.swing.JComboBox<>();
        jLabel_Nombre1 = new javax.swing.JLabel();
        txt_producto = new javax.swing.JTextField();
        jLabel_Titulo = new javax.swing.JLabel();
        jLabel_Nombre2 = new javax.swing.JLabel();
        jButton_Registrar = new javax.swing.JButton();
        jLabel_Nombre3 = new javax.swing.JLabel();
        txt_descuento = new javax.swing.JTextField();
        jLabel_Nombre4 = new javax.swing.JLabel();
        txt_PrecioProducto = new javax.swing.JTextField();
        jScrollPane_compra = new javax.swing.JScrollPane();
        jTable_compra = new javax.swing.JTable();
        jButton_agregar = new javax.swing.JButton();
        jLabel_Nombre7 = new javax.swing.JLabel();
        txt_idproducto = new javax.swing.JTextField();
        jLabel_Nombre8 = new javax.swing.JLabel();
        txt_PrecioCompra = new javax.swing.JTextField();
        jLabel_Nombre9 = new javax.swing.JLabel();
        jLabel_Nombre6 = new javax.swing.JLabel();
        txt_flete = new javax.swing.JTextField();
        jDate_Compra = new com.toedter.calendar.JDateChooser();
        jDate_Recepcion = new com.toedter.calendar.JDateChooser();
        jButton_ImprimirReporte = new javax.swing.JButton();
        jDateVencimiento = new com.toedter.calendar.JDateChooser();
        jLabel_Nombre10 = new javax.swing.JLabel();
        jButton_Descuento = new javax.swing.JButton();
        jLabel_Nombre5 = new javax.swing.JLabel();
        txt_filtro = new javax.swing.JTextField();
        jLabel_Nombre17 = new javax.swing.JLabel();
        txt_totalproductos = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(245, 218, 225));

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

        jLabel_footer.setText("Creado por Vandreh Esmeraldo ®");

        jLabel_Nombre.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre.setText("PRODUCTO:");

        cmb_tipoProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cuidado de la piel", "Maquillajes", "Fragrancias", "Accesorios", "Muestras" }));

        jLabel_Nombre1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre1.setText("TIPO DE PRODUCTO:");

        txt_producto.setBackground(new java.awt.Color(153, 153, 255));
        txt_producto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_producto.setForeground(new java.awt.Color(255, 255, 255));
        txt_producto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_producto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Titulo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel_Titulo.setText("REGISTRO DE CONPRA");

        jLabel_Nombre2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre2.setText("Fecha de compra:");

        jButton_Registrar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Registrar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Registrar.setText("Registrar compra");
        jButton_Registrar.setBorder(null);
        jButton_Registrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RegistrarActionPerformed(evt);
            }
        });

        jLabel_Nombre3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre3.setText("Descuento %:");

        txt_descuento.setBackground(new java.awt.Color(153, 153, 255));
        txt_descuento.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_descuento.setForeground(new java.awt.Color(255, 255, 255));
        txt_descuento.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_descuento.setText("0");
        txt_descuento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre4.setText("Precio de compra:");

        txt_PrecioProducto.setBackground(new java.awt.Color(153, 153, 255));
        txt_PrecioProducto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_PrecioProducto.setForeground(new java.awt.Color(255, 255, 255));
        txt_PrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_PrecioProducto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTable_compra.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane_compra.setViewportView(jTable_compra);

        jButton_agregar.setBackground(new java.awt.Color(153, 153, 255));
        jButton_agregar.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_agregar.setText("Agregar producto");
        jButton_agregar.setBorder(null);
        jButton_agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_agregarActionPerformed(evt);
            }
        });

        jLabel_Nombre7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre7.setText("ID DEL PRODUCTO:");

        txt_idproducto.setBackground(new java.awt.Color(153, 153, 255));
        txt_idproducto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_idproducto.setForeground(new java.awt.Color(255, 255, 255));
        txt_idproducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_idproducto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre8.setText("Precio de la compra:");

        txt_PrecioCompra.setBackground(new java.awt.Color(153, 153, 255));
        txt_PrecioCompra.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_PrecioCompra.setForeground(new java.awt.Color(255, 255, 255));
        txt_PrecioCompra.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_PrecioCompra.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel_Nombre9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre9.setText("Fecha de recepcion:");

        jLabel_Nombre6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre6.setText("Flete:");

        txt_flete.setBackground(new java.awt.Color(153, 153, 255));
        txt_flete.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_flete.setForeground(new java.awt.Color(255, 255, 255));
        txt_flete.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_flete.setText("5");
        txt_flete.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jDate_Compra.setDateFormatString("dd.MM.yyyy");

        jDate_Recepcion.setDateFormatString("dd.MM.yyyy");

        jButton_ImprimirReporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/impresora.png"))); // NOI18N
        jButton_ImprimirReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ImprimirReporteActionPerformed(evt);
            }
        });

        jDateVencimiento.setDateFormatString("dd.MM.yyyy");

        jLabel_Nombre10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre10.setText("Fecha de Vencimiento:");

        jButton_Descuento.setBackground(new java.awt.Color(153, 153, 255));
        jButton_Descuento.setFont(new java.awt.Font("Arial Narrow", 0, 18)); // NOI18N
        jButton_Descuento.setText("Generar descuento");
        jButton_Descuento.setBorder(null);
        jButton_Descuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_DescuentoActionPerformed(evt);
            }
        });

        jLabel_Nombre5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre5.setText("FILTRO");

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

        jLabel_Nombre17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel_Nombre17.setText("Total de productos: ");

        txt_totalproductos.setBackground(new java.awt.Color(153, 153, 255));
        txt_totalproductos.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_totalproductos.setForeground(new java.awt.Color(255, 255, 255));
        txt_totalproductos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_totalproductos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Titulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane_equipos, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel_Nombre)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(cmb_tipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel_Nombre1)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel_Nombre5)
                                                    .addComponent(txt_filtro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(31, 31, 31)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jButton_agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel_Nombre4)
                                                        .addComponent(txt_PrecioProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(txt_idproducto, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel_Nombre7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton_ImprimirReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txt_producto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 700, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel_footer)
                        .addGap(107, 107, 107)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel_Nombre17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_totalproductos, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel_Nombre10)
                                .addComponent(jLabel_Nombre9)
                                .addComponent(jLabel_Nombre2)
                                .addComponent(jDate_Compra, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                .addComponent(jDate_Recepcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateVencimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_Nombre8)
                                .addComponent(txt_PrecioCompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton_Registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_Nombre3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton_Descuento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_descuento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel_Nombre6)
                                .addComponent(txt_flete, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(112, 112, 112))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel_Titulo)
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButton_agregar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton_Registrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_Nombre5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_filtro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 52, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel_Nombre)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txt_producto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel_Nombre1)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmb_tipoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel_Nombre4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txt_PrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(52, 52, 52))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel_Nombre7)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txt_idproducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(124, 124, 124))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jButton_ImprimirReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(62, 62, 62)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_Nombre3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txt_descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_Nombre2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jDate_Compra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel_Nombre9)
                                                .addGap(8, 8, 8)
                                                .addComponent(jDate_Recepcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jButton_Descuento, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel_Nombre10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jDateVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel_Nombre6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_flete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)))
                                .addComponent(jLabel_Nombre8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_PrecioCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane_equipos, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel_footer))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane_compra, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel_Nombre17)
                            .addComponent(txt_totalproductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_RegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RegistrarActionPerformed

        int validacion = 0;
        String producto = txt_producto.getText().trim();
        if (producto.equals("")) {
            txt_descuento.setBackground(Color.red);
            validacion++;
        }

        if (validacion == 0) {

            if (jTable_compra.getRowCount() > 0) {
                for (int i = 0; i < jTable_compra.getRowCount(); i++) {
                    try {

                        Connection cn = Conexion.conectar();
                        PreparedStatement pst = cn.prepareStatement("insert into estoque values (?,?,?,?,?,?,?)");

                        pst.setInt(1, 0);
                        pst.setString(2, (String) jTable_compra.getValueAt(i, 0));
                        pst.setString(3, (String) jTable_compra.getValueAt(i, 1));
                        pst.setString(4, (String) jTable_compra.getValueAt(i, 2));
                        pst.setString(5, (String) jTable_compra.getValueAt(i, 3));
                        pst.setString(6, fecha_Vencimiento());
                        pst.setString(7, fecha_Compra());

                        pst.executeUpdate();
                        cn.close();

                    } catch (SQLException e) {
                        System.out.println("Error en registrar el estoque." + e);
                        JOptionPane.showMessageDialog(null, "¡¡ERROR al registrar el estoque!! Contacte al administrador.");
                    }

                    try {

                        Connection cn = Conexion.conectar();
                        PreparedStatement pst = cn.prepareStatement("insert into productos_comprados values (?,?,?,?,?,?,?)");

                        pst.setInt(1, 0);
                        pst.setString(2, (String) jTable_compra.getValueAt(i, 0));
                        pst.setString(3, (String) jTable_compra.getValueAt(i, 1));
                        pst.setString(4, (String) jTable_compra.getValueAt(i, 2));
                        pst.setString(5, (String) jTable_compra.getValueAt(i, 3));
                        pst.setString(6, fecha_Vencimiento());
                        pst.setString(7, fecha_Compra());

                        pst.executeUpdate();
                        cn.close();

                    } catch (SQLException e) {
                        System.out.println("Error en registrar el estoque." + e);
                        JOptionPane.showMessageDialog(null, "¡¡ERROR al registrar el estoque!! Contacte al administrador.");
                    }

                }

                txt_producto.setBackground(Color.green);

            }

        } else {
            JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos.");
        }

        String precio_de_compra, descuento, flete;

        descuento = txt_descuento.getText().trim();
        flete = txt_flete.getText().trim();
        precio_de_compra = txt_PrecioCompra.getText().trim();

        if (descuento.equals("")) {
            txt_descuento.setBackground(Color.red);
            validacion++;
        }
        if (flete.equals("")) {
            txt_flete.setBackground(Color.red);
            validacion++;
        }

        if (validacion == 0) {

            try {

                Connection cn = Conexion.conectar();
                PreparedStatement pst = cn.prepareStatement("insert into compra values (?,?,?,?,?,?)");

                pst.setInt(1, 0);
                pst.setString(2, fecha_Compra());
                pst.setString(3, fecha_Recepcion());
                pst.setString(4, descuento);
                pst.setString(5, precio_de_compra);
                pst.setString(6, flete);

                pst.executeUpdate();
                cn.close();
                Limpiar();

                txt_descuento.setBackground(Color.green);
                txt_flete.setBackground(Color.green);
                txt_PrecioCompra.setBackground(Color.green);
                txt_flete.setBackground(Color.green);

                JOptionPane.showMessageDialog(null, "Registro exitoso.");
                //this.dispose();

            } catch (SQLException e) {
                System.err.println("Error en registrar la Compra. " + e);
                JOptionPane.showMessageDialog(null, "¡¡ERROR al registrar la venta!! contacte al administrador.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Debes de llenar todos los campos.");
        }


    }//GEN-LAST:event_jButton_RegistrarActionPerformed

    private void jButton_agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_agregarActionPerformed

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

        String[] Datos = new String[5];
        Datos[0] = txt_idproducto.getText();
        Datos[1] = cmb_tipoProducto.getSelectedItem().toString();
        Datos[2] = txt_producto.getText();
        Datos[3] = txt_PrecioProducto.getText();
        Datos[4] = fecha_Vencimiento;
        compra.addRow(Datos);

        txt_PrecioCompra.setText(Precio());

        int contar = (jTable_compra.getRowCount());
        String contarstr = String.valueOf(contar);
        txt_totalproductos.setText(contarstr);

    }//GEN-LAST:event_jButton_agregarActionPerformed

    private void jButton_ImprimirReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ImprimirReporteActionPerformed

        Document documento = new Document(PageSize.A4);

        try {

            String ruta = System.getProperty("user.home");
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/OneDrive/Desktop/" + jLabel_Titulo.getText().trim() + ".pdf"));

            com.itextpdf.text.Image header = com.itextpdf.text.Image.getInstance("src/images/BannerPDF.jpg");
            header.scaleToFit(650, 1000);
            header.setAlignment(Chunk.ALIGN_CENTER);

            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.add("Registro de Compra. \n \n");
            parrafo.setFont(FontFactory.getFont("Tahoma", 14, Font.BOLD, BaseColor.DARK_GRAY));

            documento.open();
            documento.add(header);
            documento.add(parrafo);

            PdfPTable tablaCompra = new PdfPTable(5);
            tablaCompra.addCell("Fecha de Compra");
            tablaCompra.addCell("Fecha de recepcion");
            tablaCompra.addCell("Descuento");
            tablaCompra.addCell("Flete");
            tablaCompra.addCell("Precio");

            String diaCompra, mesCompra, anoCompra, fecha_compra, diaEntrega, mesEntrega, anoEntrega, fecha_recepcion, precio_de_compra, descuento, flete;

            diaCompra = Integer.toString(jDate_Compra.getCalendar().get(Calendar.DAY_OF_MONTH));
            mesCompra = Integer.toString(jDate_Compra.getCalendar().get(Calendar.MONTH) + 1);
            anoCompra = Integer.toString(jDate_Compra.getCalendar().get(Calendar.YEAR));
            fecha_compra = (diaCompra + "." + mesCompra + "." + anoCompra);
            diaEntrega = Integer.toString(jDate_Recepcion.getCalendar().get(Calendar.DAY_OF_MONTH));
            mesEntrega = Integer.toString(jDate_Recepcion.getCalendar().get(Calendar.MONTH) + 1);
            anoEntrega = Integer.toString(jDate_Recepcion.getCalendar().get(Calendar.YEAR));
            fecha_recepcion = (diaEntrega + "." + mesEntrega + "." + anoEntrega);

            descuento = txt_descuento.getText().trim();
            flete = txt_flete.getText().trim();
            precio_de_compra = txt_PrecioCompra.getText().trim();

            tablaCompra.addCell(fecha_compra);
            tablaCompra.addCell(fecha_recepcion);
            tablaCompra.addCell(descuento);
            tablaCompra.addCell(precio_de_compra);
            tablaCompra.addCell(flete);

            documento.add(tablaCompra);

            Paragraph parrafo2 = new Paragraph();
            parrafo2.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo2.add("\n \n Compra registrada. \n \n");
            parrafo2.setFont(FontFactory.getFont("Tahoma", 14, Font.BOLD, BaseColor.DARK_GRAY));

            documento.add(parrafo2);

            PdfPTable tablaProductos = new PdfPTable(4);

            tablaProductos.addCell("ID");
            tablaProductos.addCell("Tipo Producto");
            tablaProductos.addCell("Producto");
            tablaProductos.addCell("Precio");

            for (int i = 0; i < jTable_compra.getRowCount(); i++) {

                tablaProductos.addCell((String) jTable_compra.getValueAt(i, 0));
                tablaProductos.addCell((String) jTable_compra.getValueAt(i, 1));
                tablaProductos.addCell((String) jTable_compra.getValueAt(i, 2));
                tablaProductos.addCell((String) jTable_compra.getValueAt(i, 3));
            }

            documento.add(tablaProductos);

            documento.close();
            JOptionPane.showMessageDialog(null, "Reporte creado correctamente.");

        } catch (DocumentException | IOException e) {
            System.err.println("Error en PDF o ruta de imagen" + e);
            JOptionPane.showMessageDialog(null, "Error al generar PDF, contacte al administrador");
        }
    }//GEN-LAST:event_jButton_ImprimirReporteActionPerformed

    private void jButton_DescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_DescuentoActionPerformed

        float descuento = Float.parseFloat(txt_descuento.getText());
        float precio = Float.parseFloat(txt_PrecioProducto.getText());
        float precioDescuento = (1 - (descuento / 100)) * precio;
        DecimalFormat formato1 = new DecimalFormat("#.00");
        String floatFormat = formato1.format(precioDescuento);
        String DescuentoStr = floatFormat;
        txt_PrecioProducto.setText("");
        txt_PrecioProducto.setText(DescuentoStr);

    }//GEN-LAST:event_jButton_DescuentoActionPerformed
    TableRowSorter trs = null;
    private void txt_filtroKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_filtroKeyTyped
        txt_filtro.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent ke) {
                trs1.setRowFilter(RowFilter.regexFilter("(?i)" + txt_filtro.getText(), 2));
            }
        });

        trs1 = new TableRowSorter(productos);
        jTable_productos.setRowSorter(trs1);
    }//GEN-LAST:event_txt_filtroKeyTyped

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
            java.util.logging.Logger.getLogger(RegistrarCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistrarCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrarCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistrarCompra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RegistrarCompra().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cmb_tipoProducto;
    private javax.swing.JButton jButton_Descuento;
    private javax.swing.JButton jButton_ImprimirReporte;
    private javax.swing.JButton jButton_Registrar;
    private javax.swing.JButton jButton_agregar;
    private com.toedter.calendar.JDateChooser jDateVencimiento;
    private com.toedter.calendar.JDateChooser jDate_Compra;
    private com.toedter.calendar.JDateChooser jDate_Recepcion;
    private javax.swing.JLabel jLabel_Nombre;
    private javax.swing.JLabel jLabel_Nombre1;
    private javax.swing.JLabel jLabel_Nombre10;
    private javax.swing.JLabel jLabel_Nombre17;
    private javax.swing.JLabel jLabel_Nombre2;
    private javax.swing.JLabel jLabel_Nombre3;
    private javax.swing.JLabel jLabel_Nombre4;
    private javax.swing.JLabel jLabel_Nombre5;
    private javax.swing.JLabel jLabel_Nombre6;
    private javax.swing.JLabel jLabel_Nombre7;
    private javax.swing.JLabel jLabel_Nombre8;
    private javax.swing.JLabel jLabel_Nombre9;
    private javax.swing.JLabel jLabel_Titulo;
    private javax.swing.JLabel jLabel_footer;
    private javax.swing.JScrollPane jScrollPane_compra;
    private javax.swing.JScrollPane jScrollPane_equipos;
    private javax.swing.JTable jTable_compra;
    private javax.swing.JTable jTable_productos;
    private javax.swing.JTextField txt_PrecioCompra;
    private javax.swing.JTextField txt_PrecioProducto;
    private javax.swing.JTextField txt_descuento;
    private javax.swing.JTextField txt_filtro;
    private javax.swing.JTextField txt_flete;
    private javax.swing.JTextField txt_idproducto;
    private javax.swing.JTextField txt_producto;
    private javax.swing.JTextField txt_totalproductos;
    // End of variables declaration//GEN-END:variables

    public void Limpiar() {
        txt_producto.setText("");
    }
}
