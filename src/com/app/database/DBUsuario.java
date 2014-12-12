package com.app.database;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.app.model.Usuario;

/**
 * Clase que representa una base de datos que hace uso de JPA.
 * Permite insertar, eliminar, seleccionar y comprobar la existencia
 * de un email en la base de datos.
 * @author DavidGSola
 *
 */
public class DBUsuario 
{
	private static final String PERSISTENCE_UNIT_NAME = "listaCorreo";
	private static EntityManagerFactory factoria;
	
	/**
	 * Inserta un usuario en la base de datos si no existe ya un usuario con 
	 * dicho correo.
	 * @param usuario Usuario a insertar.
	 */
	public static void insertar(Usuario usuario)
	{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();

		if(!existeEmail(usuario.getEmail()))
		{
			em.getTransaction().begin();
			em.persist(usuario);
			em.getTransaction().commit();
		}
		
		em.close();
	}
	
	/**
	 * Elimina un usuario de la base de datos utilizando su id
	 * @param id Id del usuario a eliminar
	 */
	public static void eliminar(long id)
	{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();
		em.getTransaction().begin();
		
		Query q = em.createQuery("Select p FROM Usuario p WHERE  p.id = :id");
		q.setParameter("id", id);
		
		// Comprobamos que se ha obtenido al menos un usuario
		if(q.getResultList().size() == 1)
		{
			Usuario usuarioEliminar = (Usuario) q.getSingleResult();
			em.remove(usuarioEliminar);
			em.getTransaction().commit();
		}
		
		em.close();
	}
	
	/**
	 * Actualiza el email de un usuario
	 * @param usuario Usuario a actualizar
	 * @param newEmail Nuevo email del usuario
	 * @return Si ha sido existosa la operación
	 */
	public static boolean actualizar(Usuario usuario, String newEmail)
	{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();

		Query q = em.createQuery("Select p FROM Usuario p WHERE  p.nombre = :nombre AND p.apellidos = :apellidos AND p.email = :email");
		q.setParameter("nombre", usuario.getNombre());
		q.setParameter("apellidos", usuario.getApellidos());
		q.setParameter("email", usuario.getEmail());
		
		// Comprobamos que se ha obtenido al menos un usuario
		if(q.getResultList().size() == 1)
		{
			Usuario usuarioActualizar = (Usuario) q.getSingleResult();

			if(!existeEmail(newEmail))
			{
				em.getTransaction().begin();
				usuarioActualizar.setEmail(newEmail);
				em.getTransaction().commit();
				em.close();
				
				return true;
			}
		}
			
		return false;
	}
	
	/**
	 * Devuelve el usuario que coincida con el email
	 * @param email Email del usuario
	 * @return Usuario o null si no existe
	 */
	public static Usuario seleccionarUsuario(String email)
	{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();
		 
		Query q = em.createQuery("select m from Usuario m WHERE m.email = :email");
		q.setParameter("email", email);
		
		// Comprobamos que se ha obtenido al menos un usuario
		Usuario usuario = q.getResultList().size() == 1 ? (Usuario)q.getSingleResult() : null;
		
		em.close();
		
		return usuario;
	}
	
	/**
	 * Comprueba que exista un email en la base de atos
	 * @param email Email a comparar
	 * @return Si existe o no el email
	 */
	public static boolean existeEmail(String email)
	{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();
		 
		Query q = em.createQuery("select m from Usuario m WHERE m.email = :email");
		q.setParameter("email", email);
		
		boolean res =  q.getResultList().size() == 0 ? false : true;
		
		em.close();
		
		return res;
	}
	
	/**
	 * Devuelve una lista con todos los usuarios de la base de datos
	 * @return Lista de los usuarios
	 */
	public static List<Usuario> seleccionarTodosUsuarios()
	{
		factoria = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		EntityManager em = factoria.createEntityManager();
		 
		Query q = em.createQuery("select m from Usuario m");

		List<Usuario> listaUsuario = q.getResultList();
		return listaUsuario;
	}
}
