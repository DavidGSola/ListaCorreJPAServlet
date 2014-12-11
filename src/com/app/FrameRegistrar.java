package com.app;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import sun.text.normalizer.CharTrie.FriendAgent;

import com.app.model.Usuario;


public class FrameRegistrar extends JFrame implements ActionListener
{
	private JTextField jtfNombre;
	private JTextField jtfApellidos;
	private JTextField jtfEmail;
	private JButton jbRegistrarse;
	private JButton jbCancelar;
	
	private FramePrincipal fPrincipal;

	/**
	 * Create the application.
	 */
	public FrameRegistrar(FramePrincipal principal) 
	{
		fPrincipal = principal;
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 * @param jtfEmail 
	 */
	private void initialize() {
		this.setBounds(100, 100, 580, 300);
		this.setLayout(null);
		
		JLabel jlNombre = new JLabel("Nombre:");
		jlNombre.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jlNombre.setBounds(20, 10, 500, 90);
		this.add(jlNombre);
		
		jtfNombre = new JTextField();
		jtfNombre.setForeground(Color.GRAY);
		jtfNombre.setBounds(140, 30, 400, 45);
		jtfNombre.setMargin(new Insets((5),(10),(5),(5)));
		jtfNombre.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jtfNombre.setColumns(10);
		this.add(jtfNombre);
		
		JLabel jlApellidos = new JLabel("Apellidos:");
		jlApellidos.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jlApellidos.setBounds(20, 60, 500, 90);
		this.add(jlApellidos);
		
		jtfApellidos = new JTextField();
		jtfApellidos.setForeground(Color.GRAY);
		jtfApellidos.setBounds(140, 80, 400, 45);
		jtfApellidos.setMargin(new Insets((5),(10),(5),(5)));
		jtfApellidos.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jtfApellidos.setColumns(10);
		this.add(jtfApellidos);
		
		JLabel jlEmail = new JLabel("Email:");
		jlEmail.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jlEmail.setBounds(20, 110, 500, 90);
		this.add(jlEmail);
		
		jtfEmail = new JTextField();
		jtfEmail.setForeground(Color.GRAY);
		jtfEmail.setBounds(140, 130, 400, 45);
		jtfEmail.setMargin(new Insets((5),(10),(5),(5)));
		jtfEmail.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jtfEmail.setColumns(10);
		this.add(jtfEmail);
		
		jbRegistrarse = new JButton("Aceptar");
		jbRegistrarse.addActionListener(this);
		jbRegistrarse.setActionCommand("registrar");
		jbRegistrarse.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbRegistrarse.setBounds(380, 200, 160, 45);
		this.add(jbRegistrarse);	
		
		jbCancelar = new JButton("Cancelar");
		jbCancelar.addActionListener(this);
		jbCancelar.setActionCommand("cancelar");
		jbCancelar.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbCancelar.setBounds(200, 200, 160, 45);
		this.add(jbCancelar);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand == "registrar")
		{
			Usuario usuario = registrarUsuario(new Usuario(jtfNombre.getText(), jtfApellidos.getText(), jtfEmail.getText()) );
			
			if(usuario != null)
				fPrincipal.addUsuarioToTable(usuario);
		}
		else if(actionCommand == "cancelar")
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	private Usuario registrarUsuario(Usuario usuario)
	{
		try
		{
			String accion = "accion=registrar&";
			String nombre = "nombre=" + usuario.getNombre() + "&";
			String apellidos = "apellidos=" + usuario.getApellidos() + "&";
			String email = "email=" + usuario.getEmail() + "&";
			URL gwtServlet = new URL("http://localhost:8080/DSBCS_Practica2/ListaCorreoServlet");
			
			HttpURLConnection servletConnection = (HttpURLConnection) gwtServlet
					.openConnection();
			servletConnection.setUseCaches(false);
			servletConnection.setRequestMethod("POST");
			servletConnection.setDoOutput(true);
			OutputStream output = servletConnection.getOutputStream();
			
			output.write(accion.getBytes());
			output.write(nombre.getBytes());
			output.write(apellidos.getBytes());
			output.write(email.getBytes());
			
			output.flush();
			output.close();
			
			String answer = servletConnection.getContentType();
			
			if (answer.equalsIgnoreCase("Correcto")) 
			{
				JOptionPane.showMessageDialog(this, 
						"Usuario " + usuario.getNombre() + " registrado correctamente.");
				
				ObjectInputStream objIn = new ObjectInputStream(servletConnection.getInputStream());
			
				try 
				{
					return (Usuario)objIn.readObject();
					
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if (answer.equalsIgnoreCase("Ya existe el usuario")) 
			{
				JOptionPane.showMessageDialog(this,
					    "No se ha podido registrar el usuario " + usuario.getNombre() + " debido a que ya existe el email.",
					    "Advertencia",
					    JOptionPane.WARNING_MESSAGE);
			}
			
		} catch(MalformedURLException e) 
		{
			e.printStackTrace();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
