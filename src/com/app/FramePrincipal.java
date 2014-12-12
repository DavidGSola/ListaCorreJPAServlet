package com.app;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.app.model.Usuario;

/**
 * Frame principal de la interfaz de usuario. Mantiene una lista con todos los
 * usuarios de la lista de correo. Permite eliminar, registrar y actualizar 
 * los usuarios de la lista de correo.
 * @author DavidGSola
 *
 */
public class FramePrincipal extends JFrame implements ActionListener
{
	/**
	 * Referencia al frame que permite registrar un usuario
	 */
	private FrameRegistrar fRegistrar;
	
	/**
	 * Botón de registrar un usuario
	 */
	private JButton jbRegistrarse;
	
	/**
	 * Botón de eliminar un usuario
	 */
	private JButton jbEliminar;
	
	/**
	 * Botón de salir
	 */
	private JButton jbSalir;
	
	/**
	 * Tabla para mostrar la lista de usuarios
	 */
	private JTable jtUsuarios;
	
	/**
	 * Scrollpane que permite hacer scrollable la tabla {@linkplain jtUsuarios}
	 */
	private	JScrollPane scrollPane;
	
	/**
	 * Lista de usuarios de la lista de correos
	 */
	private ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
	
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try 
				{
					// Inicia el frame principal
					FramePrincipal frame = new FramePrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Crea la aplicación
	 */
	public FramePrincipal() 
	{
		initialize();
	}

	/**
	 * Inicializa el panel principal
	 */
	private void initialize() {
		this.setBounds(100, 100, 580, 300);
		this.setLayout(null);
		
		// Modelo de la tabla de los usuarios
		DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre", "Apellidos", "Email"}, 0) 
		{
		    @Override
		    public boolean isCellEditable(int row, int column) 
		    {
		       return false;
		    }
		};
		
		jtUsuarios = new JTable(tableModel);
		jtUsuarios.setFont(new Font("Calibri", Font.PLAIN, 16));
		jtUsuarios.setBounds(20, 10, 540, 180);
		
		scrollPane = new JScrollPane(jtUsuarios);
		scrollPane.setBounds(20, 10, 540, 180);
		this.add(scrollPane);
	
		// Rellenamos la tabla con los usuarios de la base de datos
		rellenarTabla();
		
		jbRegistrarse = new JButton("Registrar");
		jbRegistrarse.addActionListener(this);
		jbRegistrarse.setActionCommand("registrar");
		jbRegistrarse.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbRegistrarse.setBounds(380, 200, 160, 45);
		this.add(jbRegistrarse);	
		
		jbEliminar = new JButton("Eliminar");
		jbEliminar.addActionListener(this);
		jbEliminar.setActionCommand("eliminar");
		jbEliminar.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbEliminar.setBounds(200, 200, 160, 45);
		this.add(jbEliminar);
		
		jbSalir = new JButton("Salir");
		jbSalir.addActionListener(this);
		jbSalir.setActionCommand("salir");
		jbSalir.setFont(new Font("Calibri", Font.PLAIN, (24)));
		jbSalir.setBounds(20, 200, 160, 45);
		this.add(jbSalir);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("registrar"))
		{
			// Mostramos el frame para registrar usuarios
			fRegistrar = new FrameRegistrar(this);
			fRegistrar.setVisible(true);
		}else if(actionCommand.equals("eliminar"))
		{
			// Obtenemos el indice de la fila seleccionada en la tabla
			int index = jtUsuarios.getSelectedRow();
			
			// Eliminamos el usuario llamado al servlet
			boolean eliminado = eliminarUsuario(listaUsuarios.get(index));
			
			if(eliminado)
			{
				DefaultTableModel model = (DefaultTableModel) jtUsuarios.getModel();
				
				// Eliminamos el usuario del modelo de la tabla y de la lista interta
				model.removeRow(index);
				listaUsuarios.remove(index);
			}
		}else if(actionCommand.equals("salir"))
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	
	/**
	 * Obtiene y rellena la tabla con la lista de los usuarios haciendo una llamada al servlet
	 */
	private void rellenarTabla()
	{
		try
		{
			// Seleccionamos la acción a realizar
			String accion = "accion=getUsuarios&";
			URL gwtServlet = new URL("http://localhost:8080/DSBCS_Practica2/ListaCorreoServlet");
			
			HttpURLConnection servletConnection = (HttpURLConnection) gwtServlet
					.openConnection();
			servletConnection.setUseCaches(false);
			servletConnection.setRequestMethod("POST");
			servletConnection.setDoOutput(true);
			OutputStream output = servletConnection.getOutputStream();

			output.write(accion.getBytes());
			
			output.flush();
			output.close();
			
			// Leemos al respuesta del servlet
			ObjectInputStream objIn = new ObjectInputStream(servletConnection.getInputStream());
			
			try 
			{
				// Obtenemos la lista que nos envía el servlet
				List<Usuario> usuarios = (List<Usuario>) objIn.readObject();
				
				// Añadimos la lista a la lista interna de los usurios
				listaUsuarios.addAll(usuarios);
				
				// Añadimos a la tabla cada usuario para que se muestre
				DefaultTableModel model = (DefaultTableModel) jtUsuarios.getModel();
				for(Usuario usuario : usuarios)
				{
					model.addRow(new Object[]{usuario.getNombre(), usuario.getApellidos(),usuario.getEmail()});
				}
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Elimina un usuario dado de la lista de correo
	 * @param usuario Usuario a eliminar
	 * @return Exito de la operación
	 */
	private boolean eliminarUsuario(Usuario usuario)
	{
		try
		{
			// Seleccionamos la acción a realizar
			String accion = "accion=eliminar&";
			// Añadimos el id del usuario para que lo sepa el Servlet
			String id = "id=" + usuario.getId() + "&";
			
			URL gwtServlet = new URL("http://localhost:8080/DSBCS_Practica2/ListaCorreoServlet");
			
			HttpURLConnection servletConnection = (HttpURLConnection) gwtServlet
					.openConnection();
			servletConnection.setUseCaches(false);
			servletConnection.setRequestMethod("POST");
			servletConnection.setDoOutput(true);
			OutputStream output = servletConnection.getOutputStream();
			
			output.write(accion.getBytes());
			output.write(id.getBytes());
			
			output.flush();
			output.close();
			
			// Leemos la respuesta
			String answer = servletConnection.getContentType();
			
			if(answer.equalsIgnoreCase("Correcto")) 
			{
				JOptionPane.showMessageDialog(this, 
						"Usuario " + usuario.getNombre() + " eliminado correctamente.");
				
				return true;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Añade un usuario a la tabla que muestra la lista de los usuarios de la lista de correo
	 * @param usuario
	 */
	public void addUsuarioToTable(Usuario usuario)
	{
		// Añadimos el usuario a la lista interna
		listaUsuarios.add(usuario);
		
		// Añadimos el usuario al modelo de la tabla para que se muestre
		DefaultTableModel model = (DefaultTableModel) jtUsuarios.getModel();
		model.addRow(new Object[]{usuario.getNombre(), usuario.getApellidos(),usuario.getEmail()});
	}
}
