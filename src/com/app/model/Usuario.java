package com.app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String apellidos;
	private String email;
	
	public Usuario()
	{
		
	}
	
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
