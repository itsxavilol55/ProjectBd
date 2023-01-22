import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class App extends JFrame implements ActionListener, ComponentListener
{
    private JLabel lblserver, lblBD, lbluser, lblpassword, lblinfo;
    private JTextField txtserver, txtBD, txtuser;
    private JPasswordField txtpassword;
    private JFrame panelCaptura, panelConsulta;
    private JButton btnConectar, btnCaptura, btnConsulta;
    public static void main(String[] args) throws Exception
    {
        new App();
    }
    public App()
    {
        super("Inicio de seccion");
        interfaz();
        eventos();
    }
    private void interfaz()
    {
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        lblserver = new JLabel("Ingresa el servidor", SwingConstants.RIGHT);
        lblBD = new JLabel("Ingresa la base de datos", SwingConstants.RIGHT);
        lbluser = new JLabel("Ingresa el usuario", SwingConstants.RIGHT);
        lblpassword = new JLabel("Ingresa la contraseña", SwingConstants.RIGHT);
        lblinfo = new JLabel("", SwingConstants.RIGHT);
        txtserver = new JTextField();
        txtBD = new JTextField();
        txtuser = new JTextField();
        txtpassword = new JPasswordField();
        btnConectar = new JButton("Conectar");
        btnCaptura = new JButton("Captura");
        btnConsulta = new JButton("Consulta");
        btnCaptura.setVisible(false);
        btnConsulta.setVisible(false);
        add(lblserver);
        add(txtserver);
        add(lblBD);
        add(txtBD);
        add(lbluser);
        add(txtuser);
        add(lblpassword);
        add(txtpassword);
        add(btnConectar);
        add(btnCaptura);
        add(btnConsulta);
        add(lblinfo);
        setVisible(true);
    }
    private void eventos()
    {
        this.addComponentListener(this);
        btnConectar.addActionListener(this);
        btnCaptura.addActionListener(this);
        btnConsulta.addActionListener(this);
    }
    @Override public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == btnConectar)
        {
            Conexion conexion = new Conexion(txtserver.getText(), txtBD.getText(), txtuser.getText(), txtpassword.getText());
            if (conexion.getStmt() == null)
            {
                lblinfo.setText("" + conexion.getError());
                return;
            }
            panelCaptura = new Captura(conexion.getStmt());
            panelConsulta = new Consulta(conexion.getStmt());
            panelCaptura.setVisible(false);
            panelConsulta.setVisible(false);
            lblinfo.setText("Conexion Correcta");
            txtserver.setEnabled(false);
            txtBD.setEnabled(false);
            txtuser.setEnabled(false);
            txtpassword.setEnabled(false);
            btnConectar.setEnabled(false);
            btnCaptura.setVisible(true);
            btnConsulta.setVisible(true);
            return;
        }
        if (e.getSource() == btnCaptura)
        {
            panelCaptura.setLocationRelativeTo(null);
            panelCaptura.setVisible(true);
            panelConsulta.setVisible(false);
            return;
        }
        if (e.getSource() == btnConsulta)
        {
            panelConsulta.setLocationRelativeTo(null);
            panelCaptura.setVisible(false);
            panelConsulta.setVisible(true);
            return;
        }
    }
    @Override public void componentResized(ComponentEvent e)
    {
        lblserver.setBounds(getAncho(0.1), getAltoo(0.1), getAncho(0.38), 30);
        txtserver.setBounds(getAncho(0.5), getAltoo(0.1), getAncho(0.4), 30);
        lblBD.setBounds(getAncho(0.1), getAltoo(0.1) + 30, getAncho(0.38), 30);
        txtBD.setBounds(getAncho(0.5), getAltoo(0.1) + 30, getAncho(0.4), 30);
        lbluser.setBounds(getAncho(0.1), getAltoo(0.1) + 60, getAncho(0.38), 30);
        txtuser.setBounds(getAncho(0.5), getAltoo(0.1) + 60, getAncho(0.4), 30);
        lblpassword.setBounds(getAncho(0.1), getAltoo(0.1) + 90, getAncho(0.38), 30);
        txtpassword.setBounds(getAncho(0.5), getAltoo(0.1) + 90, getAncho(0.4), 30);
        btnConectar.setBounds(getAncho(0.35), getAltoo(0.1) + 150, getAncho(0.3), 30);
        btnCaptura.setBounds(getAncho(0.35), getAltoo(0.1) + 195, getAncho(0.3), 30);
        btnConsulta.setBounds(getAncho(0.35), getAltoo(0.1) + 240, getAncho(0.3), 30);
        lblinfo.setBounds(getAncho(0.2), getAltoo(0.1) + 330, getAncho(0.6), 60);
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