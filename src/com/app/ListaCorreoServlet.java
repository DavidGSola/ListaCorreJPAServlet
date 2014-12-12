package com.app;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.app.database.DBUsuario;
import com.app.model.Usuario;

/**
 * Servlet que maneja una lista de correo haciendo 
 * uso de una base de datos persistente (JPA)
 * @author DavidGSola
 *
 */
@WebServlet("/ListaCorreoServlet")
public class ListaCorreoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaCorreoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head></head>");         
        out.println("<body>");
        out.println("<h1>Hola Mundo</h1>");
        for(int f=1;f<=10;f++) {
            out.println(f);
            out.println(" - ");
        }
        out.println("</body>");
        out.println("</html>");  
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String accion = request.getParameter("accion");
		
		// Comprobamos la acción solicitada
		if (accion.equalsIgnoreCase("registrar"))
		{
			// Obtenemos los parámetros para registrar al usuario
			String nombre = request.getParameter("nombre");
			String apellidos = request.getParameter("apellidos");
			String email = request.getParameter("email");
			
			// Comprobamos la existencia del email en la BD
			if(!DBUsuario.existeEmail(email))
			{
				// Se inserta el usuario y se devuelve. Se devuelve para que 
				// desde el cliente se pueda referenciar al usuario a través
				// de su ID (la ID se genera al insertar el usuario en la base de datos
				DBUsuario.insertar(new Usuario(nombre, apellidos, email));
				Usuario usuario = DBUsuario.seleccionarUsuario(email);

				response.setContentType("Correcto");
				
				ObjectOutputStream objOut = new ObjectOutputStream(
						response.getOutputStream());
				objOut.writeObject(usuario);
				objOut.flush();
				objOut.close();
			}
			else
				response.setContentType("Ya existe el usuario");
			
		}
		else if(accion.equalsIgnoreCase("getUsuarios"))
		{
			// Obtenemos una lista de todos los usuarios y la devolvemos
			List<Usuario> usuarios = DBUsuario.seleccionarTodosUsuarios();
			
			ObjectOutputStream objOut = new ObjectOutputStream(
					response.getOutputStream());
			objOut.writeObject(usuarios);
			objOut.flush();
			objOut.close();
		}else if(accion.equalsIgnoreCase("eliminar"))
		{
			// Obtenemos el parámetro id para eliminarlo de la base de datos
			String id = request.getParameter("id");
			DBUsuario.eliminar(Long.parseLong(id));
			
			response.setContentType("Correcto");
		}
			
	}

}
