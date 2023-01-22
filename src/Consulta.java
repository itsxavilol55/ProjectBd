import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class Consulta extends JFrame implements ActionListener, ComponentListener
{
    private JLabel lblID, lblnombre, lbldescripcion, lblprecio, lblfamilia, lblinfo;
    private JTextField txtID, txtnombre, txtdescripcion, txtprecio;
    private JComboBox combofamilia;
    private JButton btnlimpiar, btnbuscar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JPanel paneltabla;
    private Statement stmt;
    public Consulta(
        Statement statement)
    {
        super("Consulta");
        stmt = statement;
        interfaz();
        eventos();
    }
    private void interfaz()
    {
        setSize(800, 600);
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
        btnbuscar = new JButton("Buscar");
        modelo = new DefaultTableModel(0, 5);
        modelo.setColumnIdentifiers(new Object[]
        { "ID", "Nombre", "Descripcion", "Precio", "FamiliaID", "Nombre Familia" });
        tabla = new JTable(modelo);
        tabla.setEnabled(false);
        tabla.setRowHeight(30);
        paneltabla.add(new JScrollPane(tabla));
        paneltabla.setLayout(new BoxLayout(paneltabla, 1));
        llenacombo();
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
        add(btnbuscar);
        add(lblinfo);
        add(paneltabla);
        setVisible(true);
    }
    private void eventos()
    {
        this.addComponentListener(this);
        btnlimpiar.addActionListener(this);
        btnbuscar.addActionListener(this);;
    }
    @Override public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnlimpiar)
        {
            lblinfo.setText("");
            txtnombre.setText("");
            txtdescripcion.setText("");
            txtprecio.setText("");
            txtID.setText("");
            txtID.requestFocus();
            combofamilia.setSelectedIndex(0);
            return;
        }
        if (e.getSource() == btnbuscar)
        {
            if (txtID.getText().equals("") && txtnombre.getText().equals("") && txtdescripcion.getText().equals("") && txtprecio.getText().equals("") && combofamilia.getSelectedItem().equals("Seleccione"))
            {
                llenatabla("select * from vw_artfam");
                return;
            }
            if (!txtID.getText().equals("") && txtnombre.getText().equals("") && txtdescripcion.getText().equals("") && txtprecio.getText().equals(""))
            {
                llenatabla("select * from vw_artfam where artid = " + txtID.getText());
                return;
            }
            if (txtID.getText().equals("") && !txtnombre.getText().equals("") && txtdescripcion.getText().equals("") && txtprecio.getText().equals(""))
            {
                llenatabla("select * from vw_artfam where artnombre like '%" + txtnombre.getText() + "%'");
                return;
            }
            if (txtID.getText().equals("") && txtnombre.getText().equals("") && txtdescripcion.getText().equals("") && txtprecio.getText().equals("") && !combofamilia.getSelectedItem().equals("Seleccione"))
            {
                llenatabla("select * from vw_artfam where famid = " + combofamilia.getSelectedItem());
                return;
            }
            if (txtID.getText().equals("") && txtnombre.getText().equals("") && !txtdescripcion.getText().equals("") && txtprecio.getText().equals(""))
            {
                llenatabla("select * from vw_artfam where artdescripcion like '%" + txtdescripcion.getText() + "%'");
                return;
            }
            if (txtID.getText().equals("") && txtnombre.getText().equals("") && txtdescripcion.getText().equals("") && !txtprecio.getText().equals(""))
            {
                llenatabla("select * from vw_artfam where artprecio = " + txtprecio.getText());
                return;
            }
        }
    }
    private void llenatabla(String sql)
    {
        modelo.setNumRows(0);
        try
        {
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next())
            {
                modelo.addRow(new Object[]
                { resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6) });
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
        combofamilia.addItem("Seleccione");
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
        btnbuscar.setBounds(getAncho(0.4), getAltoo(0.1) + 150, getAncho(0.2), 30);
        lblinfo.setBounds(getAncho(0.1), getAltoo(0.1) + 180, getAncho(0.8), 30);
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
    @Override public void componentMoved(ComponentEvent e)
    {
    }
    @Override public void componentShown(ComponentEvent e)
    {
    }
    @Override public void componentHidden(ComponentEvent e)
    {
    }
}