package crypto;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 * Reads encryption keys from the file system.
 */
public final class Keys {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Reads an crypto.RSA private key from the given file.
     *
     * @param file the file containing the crypto.RSA key
     * @return a PrivateKey instance
     * @throws IOException if an exception occurred while reading the key file
     * @throws IllegalStateException if an error occurred in the java security api
     */
    public static PrivateKey readPrivateKey(File file) throws IOException, IllegalStateException {
        try (PemReader reader = new PemReader(new FileReader(file))) {
            PemObject pemObject = reader.readPemObject();
            return createPrivateKey(new PKCS8EncodedKeySpec(pemObject.getContent()));
        }
    }

    /**
     * Reads an crypto.RSA public key from the given file.
     *
     * @param file the file containing the crypto.RSA key
     * @return a PublicKey instance
     * @throws IOException if an exception occurred while reading the key file
     * @throws IllegalStateException if an error occurred in the java security api
     */
    public static PublicKey readPublicKey(File file) throws IOException, IllegalStateException {
        try (PemReader reader = new PemReader(new FileReader(file))) {
            PemObject pemObject = reader.readPemObject();
            return createPublicKey(new X509EncodedKeySpec(pemObject.getContent()));
        }
    }

    public static void writePrivateKey(PrivateKey privateKey, File file) throws IOException, IllegalStateException {
        try (PemWriter writer = new PemWriter(new FileWriter(file))) {
        	PemObject o = new PemObject("PRIVATE KEY", privateKey.getEncoded());
        	writer.writeObject(o);
        }
    }

    public static void writePublicKey(PublicKey publicKey, File file) throws IOException, IllegalStateException {
        try (PemWriter writer = new PemWriter(new FileWriter(file))) {
        	PemObject o = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        	writer.writeObject(o);
        }
    }

    private static PrivateKey createPrivateKey(KeySpec spec) {
        try {
            return getRsaKeyFactory().generatePrivate(spec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Error creating private key", e);
        }
    }

    private static PublicKey createPublicKey(KeySpec spec) {
        try {
            return getRsaKeyFactory().generatePublic(spec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException("Error creating private key", e);
        }
    }

    private static KeyFactory getRsaKeyFactory() throws IllegalStateException {
        try {
            return KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalStateException("Error creating crypto.RSA key factory with bouncycastle", e);
        }
    }

	public static String toPEM(Key key) {
		try {
			StringWriter writer = new StringWriter();
			PemWriter pemWriter = new PemWriter(writer);
			pemWriter.writeObject(new PemObject(key instanceof PublicKey ? "PUBLIC KEY" : "PRIVATE KEY", key.getEncoded()));
			pemWriter.flush();
			pemWriter.close();
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Key fromPEM(String pem) throws IOException {
		try (PemReader pemReader = new PemReader(new StringReader(pem))) {
            PemObject pemObject = pemReader.readPemObject();
            if (pem.contains("PUBLIC"))
                return createPublicKey(new X509EncodedKeySpec(pemObject.getContent()));
            return createPrivateKey(new X509EncodedKeySpec(pemObject.getContent()));
        }
	}

}
