package com.app.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que se permite comprobar que un email mantiene una expresión valida
 * @author DavidGSola
 *
 */
public class ValidadorEmail 
{
	/**
	 * Expresión regular que representa un email valido (example@example.com)
	 */
	private static final String PATRON_EMAIL = 
	    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
	    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Comprueba que el email sigue el patrón valido de un email
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public static boolean validar(final String email) 
	{
		Pattern pattern = Pattern.compile(PATRON_EMAIL);
	    Matcher matcher = pattern.matcher(email);
	    return matcher.matches();
	
	}
}