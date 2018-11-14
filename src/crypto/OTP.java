package crypto;

import java.util.Base64;

public class OTP {
	
	public static String encrypt(String s, String k) {
		byte[] bytes = s.getBytes();
		byte[] key = k.getBytes();
	    int size = bytes != null ? bytes.length : 0;
	    final byte[] encoded = new byte[size];
	    int keySize = key != null ? key.length : 0;
	    // loop on input bytes 
	    for (int i = 0; i < size; i++) {
	        // shift key index (assuming key <= bytes)
	        int keyi = i >= keySize ? size % (keySize-1) : i;
	        // pad 
	        encoded[i] = (byte) (bytes[i] ^ key[keyi]);
	    }
	    return Base64.getEncoder().encodeToString(encoded);
	}
	
	public static String decrypt(String s, String k) {	
		byte[] bytes = Base64.getDecoder().decode(s.getBytes());
		byte[] key = k.getBytes();
	    int size = bytes != null ? bytes.length : 0;
	    final byte[] encoded = new byte[size];
	    int keySize = key != null ? key.length : 0;
	    // loop on input bytes 
	    for (int i = 0; i < size; i++) {
	        // shift key index (assuming key <= bytes)
	        int keyi = i >= keySize ? size % (keySize-1) : i;
	        // pad 
	        encoded[i] = (byte) (bytes[i] ^ key[keyi]);
	    }
	    return new String(encoded);
	}

}
