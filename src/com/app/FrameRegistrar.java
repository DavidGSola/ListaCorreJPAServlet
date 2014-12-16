package com.app;

import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.app.model.Usuario;
import com.app.util.ValidadorEmail;

/**
 * Frame registrar de la interfaz de usuario. Permite registrar un usuario
 * en la lista de correo.
 * @author DavidGSola
 *
 */
public class FrameRegistrar extends JFrame implements ActionListener
{
	/**
	 * Textfield donde escribir el nombre
	 */
	private JTextField jtfNombre;
	
	/**
	 * Textfield donde escribir los apellidos
	 */
	private JTextField jtfApellidos;
	
	/**
	 * Textfield donde escribir el email
	 */
	private JTextField jtfEmail;
	
	/**
	 * Botón de registrar
	 */
	private JButton jbRegistrarse;
	
	/**
	 * Botón de cancelar el registro
	 */
	private JButton jbCancelar;
	
	/**
	 * Referencia al frame principal de la interfaz de usuario
	 */
	private FramePrincipal fPrincipal;

	/**
	 * Crea la aplicación
	 */
	public FrameRegistrar(FramePrincipal principal) 
	{
		fPrincipal = principal;
		initialize();
	}

	/**
	 * Inicializa el panel principal
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
			Usuario usuario = null;
			if(jtfNombre.getText().length()!=0 && jtfApellidos.getText().length()!=0 && jtfEmail.getText().length()!=0)
				if(!ValidadorEmail.validar(jtfEmail.getText()))
					JOptionPane.showMessageDialog(this, "Debe utilizar un email valido (example@example.com).", "Advertencia", JOptionPane.WARNING_MESSAGE);
				else
					usuario = registrarUsuario(new Usuario(jtfNombre.getText(), jtfApellidos.getText(), jtfEmail.getText()) );
			else
				JOptionPane.showMessageDialog(this, "Debe rellenar todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
			
			// Si se registra con exito lo añadimos a la tabla del frame principal
			if(usuario != null)
			{
				fPrincipal.addUsuarioToTable(usuario);
				this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
		}
		else if(actionCommand == "cancelar")
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	/**
	 * Registra un usuario en la lista de correo haciendo uso del servlet
	 * @param usuario
	 * @return
	 */
	private Usuario registrarUsuario(Usuario usuario)
	{
		try
		{
			// Seleccionamos la acción a realizar
			String accion = "accion=registrar&";
			// Añadimos los datos del usuario a registrar
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
			
			// Leemos la respuesta
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
