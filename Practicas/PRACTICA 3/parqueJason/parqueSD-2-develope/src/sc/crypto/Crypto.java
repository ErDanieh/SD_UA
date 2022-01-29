package sc.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;
import java.util.Base64;

public class Crypto
{
	private SecretKey key;
	private Cipher cipher;
	private String algoritmo = "AES";
	private int keysize = 16;

	/**
	 * Crea la Llave para encriptar/desencriptar
	 * 
	 * @param String value
	 */
	public void addKey(String value)
	{
		byte[] valuebytes = value.getBytes();
		key = new SecretKeySpec(Arrays.copyOf(valuebytes, keysize), algoritmo);
	}

	/**
	 * Metodo para encriptar un texto
	 * 
	 * @param String texto
	 * @return String texto encriptado
	 */
	public String encriptar(String texto)
	{
		String value = "";
		try
		{
			cipher = Cipher.getInstance(algoritmo);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] textobytes = texto.getBytes();
			byte[] cipherbytes = cipher.doFinal(textobytes);
			value = Base64.getEncoder().encodeToString(cipherbytes);
		} catch (NoSuchAlgorithmException ex)
		{
			System.err.println(ex.getMessage());
		} catch (NoSuchPaddingException ex)
		{
			System.err.println(ex.getMessage());
		} catch (InvalidKeyException ex)
		{
			System.err.println(ex.getMessage());
		} catch (IllegalBlockSizeException ex)
		{
			System.err.println(ex.getMessage());
		} catch (BadPaddingException ex)
		{
			System.err.println(ex.getMessage());
		}
		return value;
	}

	/**
	 * Metodo para desencriptar un texto
	 * 
	 * @param texto Texto encriptado
	 * @return String texto desencriptado
	 */
	public String desencriptar(String texto)
	{
		String str = "";
		try
		{
			byte[] value = Base64.getDecoder().decode(texto);
			cipher = Cipher.getInstance(algoritmo);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cipherbytes = cipher.doFinal(value);
			str = new String(cipherbytes);
		} catch (InvalidKeyException ex)
		{
			System.err.println(ex.getMessage());
		} catch (IllegalBlockSizeException ex)
		{
			System.err.println(ex.getMessage());
		} catch (BadPaddingException ex)
		{
			System.err.println(ex.getMessage());
		} catch (NoSuchAlgorithmException ex)
		{
			System.err.println(ex.getMessage());
		} catch (NoSuchPaddingException ex)
		{
			System.err.println(ex.getMessage());
		}
		return str;
	}
}
