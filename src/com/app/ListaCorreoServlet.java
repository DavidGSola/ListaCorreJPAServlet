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
 * Servlet implementation class ListaCorreo
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
		// TODO Auto-generated method stub
	 
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
		
		if (accion.equalsIgnoreCase("registrar"))
		{
			String nombre = request.getParameter("nombre");
			String apellidos = request.getParameter("apellidos");
			String email = request.getParameter("email");
			
			System.out.println("email: " + 	email);
			
			if(!DBUsuario.existeEmail(email))
			{
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
			List<Usuario> usuarios = DBUsuario.seleccionarTodosUsuarios();
			
			ObjectOutputStream objOut = new ObjectOutputStream(
					response.getOutputStream());
			objOut.writeObject(usuarios);
			objOut.flush();
			objOut.close();
		}else if(accion.equalsIgnoreCase("eliminar"))
		{
			String id = request.getParameter("id");
			DBUsuario.eliminar(Long.parseLong(id));
			
			response.setContentType("Correcto");
		}
			
	}

}
