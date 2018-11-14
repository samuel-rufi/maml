package crypto;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAExample {

	public static void main(String[] args) throws Exception {

		KeyPair k = RSA.generateKeyPair();

		PublicKey publicKey = k.getPublic();
		PrivateKey privateKey = k.getPrivate();
		
		System.out.println(privateKey.getFormat());
		
		Keys.writePrivateKey(privateKey, new File("private.key"));
		Keys.writePublicKey(publicKey, new File("public.key"));
		
		PrivateKey newPK = Keys.readPrivateKey(new File("private.key"));
		PublicKey newPU = Keys.readPublicKey(new File("public.key"));
		
		System.out.println("loo");
		System.out.println(newPK);
		
		String enc = RSA.encrypt(newPU, "HALLO");
		System.out.println(enc);
		String dec = RSA.decrypt(newPK, enc);
		System.out.println(dec);
		
		System.out.println(RSA.verify(dec, RSA.sign(dec, privateKey), publicKey));

	}

}
