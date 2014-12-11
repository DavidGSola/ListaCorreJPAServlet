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
import com.sun.scenario.effect.Blend.Mode;


public class FramePrincipal extends JFrame implements ActionListener
{
	private FrameRegistrar fRegistrar;
	
	private JButton jbRegistrarse;
	private JButton jbEliminar;
	private JButton jbSalir;
	
	private JTable jtUsuarios;
	private	JScrollPane scrollPane;
	
	private ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FramePrincipal frame = new FramePrincipal();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FramePrincipal() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the panel.
	 * @param jtfEmail 
	 */
	private void initialize() {
		this.setBounds(100, 100, 580, 300);
		this.setLayout(null);
		
		DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Nombre", "Apellidos", "Email"}, 0) 
		{
		    @Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		jtUsuarios = new JTable(tableModel);
		jtUsuarios.setFont(new Font("Calibri", Font.PLAIN, 16));
		jtUsuarios.setBounds(20, 10, 540, 180);
		scrollPane = new JScrollPane(jtUsuarios);
		scrollPane.setBounds(20, 10, 540, 180);
		this.add(scrollPane);
	
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
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("registrar"))
		{
			fRegistrar = new FrameRegistrar(this);
			fRegistrar.setVisible(true);
		}else if(actionCommand.equals("eliminar"))
		{
			int index = jtUsuarios.getSelectedRow();
			boolean eliminado = eliminarUsuario(listaUsuarios.get(index));
			
			if(eliminado)
			{
				DefaultTableModel model = (DefaultTableModel) jtUsuarios.getModel();
				
				System.out.println(index);
				
				model.removeRow(index);
				listaUsuarios.remove(index);
			}
		}else if(actionCommand.equals("salir"))
		{
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	private void rellenarTabla()
	{
		try
		{
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
			
			ObjectInputStream objIn = new ObjectInputStream(servletConnection.getInputStream());
			
			List<Usuario> usuarios;

			try 
			{
				usuarios = (List<Usuario>) objIn.readObject();
				listaUsuarios.addAll(usuarios);
				
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
	
	private boolean eliminarUsuario(Usuario usuario)
	{
		try
		{
			String accion = "accion=eliminar&";
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
			
			String answer = servletConnection.getContentType();
			
			if (answer.equalsIgnoreCase("Correcto")) 
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
	
	public void addUsuarioToTable(Usuario usuario)
	{
		listaUsuarios.add(usuario);
		
		DefaultTableModel model = (DefaultTableModel) jtUsuarios.getModel();
		model.addRow(new Object[]{usuario.getNombre(), usuario.getApellidos(),usuario.getEmail()});
	}
}
