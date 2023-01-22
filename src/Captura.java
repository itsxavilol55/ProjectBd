import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Captura extends JFrame implements ActionListener, ComponentListener, FocusListener
{
    private JLabel lblID, lblnombre, lbldescripcion, lblprecio, lblfamilia, lblinfo;
    private JTextField txtID, txtnombre, txtdescripcion, txtprecio;
    private JComboBox combofamilia;
    private JButton btnlimpiar, btnguardar, btnborrar;
    private JRadioButton radio1, radio2, radio3;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel paneltabla;
    private Statement stmt;
    public Captura(
        Statement statement)
    {
        super("Captura");
        stmt = statement;
        interfaz();
        eventos();
    }
    private void interfaz()
    {
        setSize(700, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        paneltabla = new JPanel();
        lblID = new JLabel("ID", SwingConstants.RIGHT);
        lblnombre = new JLabel("Ingresa el nombre", SwingConstants.RIGHT);
        lbldescripcion = new JLabel("Ingresa la descripcion", SwingConstants.RIGHT);
        lblprecio = new JLabel("Ingresa el precio", SwingConstants.RIGHT);
        lblfamilia = new JLabel("Ingresa la familia", SwingConstants.RIGHT);
        lblinfo = new JLabel("", SwingConstants.RIGHT);
        txtID = new JTextField();
        txtnombre = new JTextField();
        txtdescripcion = new JTextField();
        txtprecio = new JTextField();
        combofamilia = new JComboBox();
        btnlimpiar = new JButton("Limpiar");
        btnguardar = new JButton("Guardar");
        btnborrar = new JButton("Borrar");
        radio1 = new JRadioButton("Nuevo", true);
        radio2 = new JRadioButton("Modificar", false);
        radio3 = new JRadioButton("Borrar", false);
        modelo = new DefaultTableModel(0, 5);
        modelo.setColumnIdentifiers(new Object[]
        { "ID", "Nombre", "Descripcion", "Precio", "Familia" });
        tabla = new JTable(modelo);
        tabla.setRowHeight(30);
        txtID.setEnabled(false);
        txtID.setText("-");
        llenatabla();
        llenacombo();
        tabla.setEnabled(false);
        btnborrar.setVisible(false);
        paneltabla.add(new JScrollPane(tabla));
        paneltabla.setLayout(new BoxLayout(paneltabla, 1));
        add(lblID);
        add(txtID);
        add(lblnombre);
        add(txtnombre);
        add(lbldescripcion);
        add(txtdescripcion);
        add(lblprecio);
        add(txtprecio);
        add(lblfamilia);
        add(combofamilia);
        add(btnlimpiar);
        add(btnguardar);
        add(lblinfo);
        add(radio1);
        add(radio2);
        add(radio3);
        add(btnborrar);
        add(paneltabla);
        setVisible(true);
    }
    private void llenatabla()
    {
        modelo.setNumRows(0);
        try
        {
            ResultSet resultSet = stmt.executeQuery("select * from articulos");
            while (resultSet.next())
            {
                modelo.addRow(new Object[]
                { resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5) });
            }
            revalidate();
        }
        catch (SQLException e)
        {
            lblinfo.setText("" + e.getMessage());
        }
    }
    private void llenacombo()
    {
        try
        {
            ResultSet resultSet = stmt.executeQuery("select distinct(famid) from familias");
            while (resultSet.next())
            {
                combofamilia.addItem("" + resultSet.getString(1));
            }
        }
        catch (SQLException e)
        {
            lblinfo.setText("" + e.getMessage());
        }
    }
    private void eventos()
    {
        this.addComponentListener(this);
        txtID.addFocusListener(this);
        btnlimpiar.addActionListener(this);
        btnguardar.addActionListener(this);
        btnborrar.addActionListener(this);
        radio1.addActionListener(this);
        radio2.addActionListener(this);
        radio3.addActionListener(this);
    }
    @Override public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnlimpiar)
        {
            limpiar();
            btnborrar.setVisible(false);
            txtnombre.requestFocus();
            nuevo();
            return;
        }
        if (e.getSource() == btnguardar)
        {
            if (txtID.getText().equals("") || txtnombre.getText().equals("") || txtdescripcion.getText().equals("") || txtprecio.getText().equals(""))
            {
                lblinfo.setText("no se permiten datos vacios");
                return;
            }
            try
            {
                String dato = "";
                if (radio2.isSelected()) dato = "SELECT @artid = " + txtID.getText() + ";";
                int update = stmt.executeUpdate("declare @artid int; " + dato + " exec Sp_articulosventas @artid output, '" + txtnombre.getText() + "' , '" + txtdescripcion.getText() + "' , " + txtprecio.getText() + " , " + combofamilia.getSelectedItem());
                ResultSet result = stmt.executeQuery("select max(artid) from articulos");
                result.next();
                if (radio1.isSelected()) txtID.setText(result.getString(1));
                lblinfo.setText("Se inserto correctamente");
                llenatabla();
            }
            catch (SQLException e1)
            {
                lblinfo.setText("" + e1.getMessage());
            }
            return;
        }
        if (e.getSource() == btnborrar)
        {
            if (txtID.getText().equals("")) lblinfo.setText("la ID no puede estar vacia");
            try
            {
                ResultSet result = stmt.executeQuery("delete from articulos where artid = " + txtID.getText());
                enfocatxtID();
                llenatabla();
                lblinfo.setText("Se borro correctamente");
            }
            catch (SQLException e1)
            {
                lblinfo.setText(e1.getMessage());
            }
            return;
        }
        if (e.getSource() == radio1)
        {
            limpiar();
            nuevo();
            habilitatxts(true);
            return;
        }
        if (e.getSource() == radio2)
        {
            limpiar();
            enfocatxtID();
            habilitatxts(true);
            radio3.setSelected(false);
            radio2.setSelected(true);
            radio1.setSelected(false);
            return;
        }
        if (e.getSource() == radio3)
        {
            enfocatxtID();
            habilitatxts(false);
            radio3.setSelected(true);
            radio2.setSelected(false);
            radio1.setSelected(false);
            return;
        }
    }
    @Override public void focusLost(FocusEvent e)
    {
        if (e.getSource() == txtID && radio2.isSelected())
        {
            txtID.setEnabled(false);
            try
            {
                ResultSet resultado = stmt.executeQuery("Select * from articulos where artID = " + txtID.getText());
                resultado.next();
                lblinfo.setText("se encontro el registro");
                txtnombre.setText(resultado.getString(2));
                txtdescripcion.setText(resultado.getString(3));
                txtprecio.setText(resultado.getString(4));
                combofamilia.setSelectedItem(resultado.getString(5));
            }
            catch (SQLException e1)
            {
                txtID.setText("");
                txtID.setEnabled(true);
                txtID.requestFocus();
                lblinfo.setText("no existe este registro");
            }
        }
    }
    @Override public void componentResized(ComponentEvent e)
    {
        lblID.setBounds(getAncho(0.1), getAltoo(0.1) - 30, getAncho(0.18), 30);
        txtID.setBounds(getAncho(0.3), getAltoo(0.1) - 30, getAncho(0.15), 30);
        lblnombre.setBounds(getAncho(0.1), getAltoo(0.1), getAncho(0.18), 30);
        txtnombre.setBounds(getAncho(0.3), getAltoo(0.1), getAncho(0.3), 30);
        lbldescripcion.setBounds(getAncho(0.1), getAltoo(0.1) + 30, getAncho(0.18), 30);
        txtdescripcion.setBounds(getAncho(0.3), getAltoo(0.1) + 30, getAncho(0.3), 30);
        lblprecio.setBounds(getAncho(0.1), getAltoo(0.1) + 60, getAncho(0.18), 30);
        txtprecio.setBounds(getAncho(0.3), getAltoo(0.1) + 60, getAncho(0.3), 30);
        lblfamilia.setBounds(getAncho(0.1), getAltoo(0.1) + 90, getAncho(0.18), 30);
        combofamilia.setBounds(getAncho(0.3), getAltoo(0.1) + 90, getAncho(0.3), 30);
        btnlimpiar.setBounds(getAncho(0.2), getAltoo(0.1) + 150, getAncho(0.2), 30);
        btnguardar.setBounds(getAncho(0.4), getAltoo(0.1) + 150, getAncho(0.2), 30);
        btnborrar.setBounds(getAncho(0.6), getAltoo(0.1) + 150, getAncho(0.2), 30);
        lblinfo.setBounds(getAncho(0.1), getAltoo(0.1) + 180, getAncho(0.8), 30);
        radio1.setBounds(getAncho(0.65), getAltoo(0.1), getAncho(0.2), 30);
        radio2.setBounds(getAncho(0.65), getAltoo(0.1) + 30, getAncho(0.2), 30);
        radio3.setBounds(getAncho(0.65), getAltoo(0.1) + 60, getAncho(0.2), 30);
        paneltabla.setBounds(getAncho(0.1), getAltoo(0.1) + 210, getAncho(0.8), getAltoo(0.8) - 240);
        revalidate();
    }
    private int getAltoo(double d)
    {
        return (int) (getHeight() * d);
    }
    private int getAncho(double d)
    {
        return (int) (getWidth() * d);
    }
    private void limpiar()
    {
        lblinfo.setText("");
        txtnombre.setText("");
        txtdescripcion.setText("");
        txtprecio.setText("");
        habilitatxts(true);
        combofamilia.removeAll();
    }
    private void nuevo()
    {
        txtID.setEnabled(false);
        txtID.setText("-");
        txtnombre.requestFocus();
        radio1.setSelected(true);
        radio2.setSelected(false);
        radio3.setSelected(false);
    }
    private void enfocatxtID()
    {
        txtID.setEnabled(true);
        txtID.setText("");
        txtID.requestFocus();
    }
    private void habilitatxts(boolean b)
    {
        txtnombre.setEnabled(b);
        txtdescripcion.setEnabled(b);
        txtprecio.setEnabled(b);
        combofamilia.setEnabled(b);
        btnguardar.setVisible(b);
        btnborrar.setVisible(!b);
    }
    @Override public void componentMoved(ComponentEvent e)
    {
    }
    @Override public void componentShown(ComponentEvent e)
    {
    }
    @Override public void componentHidden(ComponentEvent e)
    {
    }
    @Override public void focusGained(FocusEvent e)
    {
    }
}