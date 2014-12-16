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
 * Frame actualizar de la interfaz de usuario. Permite actualizar el 
 * email de un usuario en la lista de correo.
 * @author DavidGSola
 *
 */
public class FrameActualizar extends JFrame implements ActionListener
{	
	/**
	 * Textfield donde escribir el email
	 */
	private JTextField jtfEmail;
	
	/**
	 * Botón de Actualizar
	 */
	private JButton jbActualizar;
	
	/**
	 * Botón de cancelar el registro
	 */
	private JButton jbCancelar;
	
	/**
	 * Referencia al frame principal de la interfaz de usuario
	 */
	private FramePrincipal fPrincipal;
	
	/**
	 * Usuario que se va a actualizar
	 */
	private Usuario usuarioActualizar;

	/**
	 * Crea la aplicación
	 */
	public FrameActualizar(FramePrincipal principal, Usuario usuario) 
	{
		this.usuarioActualizar = usuario;
		fPrincipal = principal;
		initialize();
	}

	/**
	 * Inicializa el panel principal
	 */
	private void initialize() {
		this.setBounds(100, 100, 580, 200);
		this.setLayout(null);
		
		JLabel jlNombre = new JLabel("Nuevo email:");
		jlNombre.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jlNombre.setBounds(20, 10, 500, 90);
		this.add(jlNombre);
		
		jtfEmail = new JTextField();
		jtfEmail.setForeground(Color.GRAY);
		jtfEmail.setBounds(160, 30, 400, 45);
		jtfEmail.setMargin(new Insets((5),(10),(5),(5)));
		jtfEmail.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jtfEmail.setColumns(10);
		this.add(jtfEmail);
		
		jbActualizar = new JButton("Aceptar");
		jbActualizar.addActionListener(this);
		jbActualizar.setActionCommand("actualizar");
		jbActualizar.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbActualizar.setBounds(380, 100, 160, 45);
		this.add(jbActualizar);	
		
		jbCancelar = new JButton("Cancelar");
		jbCancelar.addActionListener(this);
		jbCancelar.setActionCommand("cancelar");
		jbCancelar.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbCancelar.setBounds(200, 100, 160, 45);
		this.add(jbCancelar);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand == "actualizar")
		{
			if(jtfEmail.getText().length()!=0)
			{
				if(!ValidadorEmail.validar(jtfEmail.getText()))
					JOptionPane.showMessageDialog(this, "Debe utilizar un email valido (example@example.com).", "Advertencia", JOptionPane.WARNING_MESSAGE);
				else
				{
					actualizarUsuario(usuarioActualizar, jtfEmail.getText() );

					this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
				}
			}
			else
				JOptionPane.showMessageDialog(this, "Debe rellenar todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
		}
		else if(actionCommand == "cancelar")
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	/**
	 * Actualiza un usuario en la lista de correo haciendo uso del servlet con un nuevo correo
	 * @param usuario
	 * @return si se ha llevado a cabo con exito la operación
	 */
	private boolean actualizarUsuario(Usuario usuario, String newEmail)
	{
		try
		{
			// Seleccionamos la acción a realizar
			String accion = "accion=actualizar&";
			// Añadimos los datos del usuario a registrar
			String id = "id=" + usuario.getId() + "&";
			String email2 = "newEmail=" + newEmail + "&";
			URL gwtServlet = new URL("http://localhost:8080/DSBCS_Practica2/ListaCorreoServlet");
			
			HttpURLConnection servletConnection = (HttpURLConnection) gwtServlet
					.openConnection();
			servletConnection.setUseCaches(false);
			servletConnection.setRequestMethod("POST");
			servletConnection.setDoOutput(true);
			OutputStream output = servletConnection.getOutputStream();
			
			output.write(accion.getBytes());
			output.write(id.getBytes());
			output.write(email2.getBytes());
			
			output.flush();
			output.close();
			
			// Leemos la respuesta
			String answer = servletConnection.getContentType();
			
			if (answer.equalsIgnoreCase("Correcto")) 
			{
				JOptionPane.showMessageDialog(this, "Actualizado el email del usuario " + usuario.getNombre() + " correctamente.");
				fPrincipal.actualizarUsuarioEmailTable(usuario, newEmail);
				
				return true;

			} else if (answer.equalsIgnoreCase("Ya existe el email")) 
			{
				JOptionPane.showMessageDialog(this,
					    "No se ha podido actualizar el usuario " + usuario.getNombre() + " debido a que ya existe el nuevo email.",
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
		
		return false;
	}
}
