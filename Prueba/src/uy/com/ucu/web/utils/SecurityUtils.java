package uy.com.ucu.web.utils;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

public class SecurityUtils {
	
	public String hash(String input){
		String hashedPassword = null;
		try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(input.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            hashedPassword = sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return hashedPassword;
	}
	
	public boolean comparePasswords(String rawPassword, String hashedPassword){
		return hash(rawPassword).equals(hashedPassword);
	}
	
}