package com.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Clase que representa una entidad dentro de la base de datos utilizando JPA
 * @author DavidGSola
 *
 */
@Entity
public class Usuario implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Id del usuario
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * Nombre del usuario
	 */
	private String nombre;
	
	/**
	 * Apellidos del usuario
	 */
	private String apellidos;
	
	/**
	 * Email del usuario
	 */
	private String email;
	
	/**
	 * Constructor por defecto de un usuario
	 */
	public Usuario()
	{
		
	}
	
	/**
	 * Constructor que inicializa un usuario
	 * @param nombre Nombre del usuario
	 * @param apellidos Apellidos del usuario
	 * @param email Email del usuario
	 */
	public Usuario(String nombre, String apellidos, String email)
	{
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
	}
	
	public Long getId()
	{
		return id;
	}
	
	public String getNombre() 
	{
		return nombre;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	public String getApellidos() 
	{
		return apellidos;
	}
	
	public void setApellidos(String apellidos) 
	{
		this.apellidos = apellidos;
	}
	
	public String getEmail() 
	{
		return email;
	}
	
	public void setEmail(String email) 
	{
		this.email = email;
	}
}
