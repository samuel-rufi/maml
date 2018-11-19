import crypto.Keys;
import crypto.OTP;

import java.security.PublicKey;
import java.util.Base64;

public class Message {

	private String publicData;
	private String privateData;
	private PublicKey publicKey;
	private String signature;

	public Message(String publicData, String privateData, PublicKey publicKey) {
		this.publicData = publicData;
		this.privateData = privateData;
		this.publicKey = publicKey;
	}

	public String getPublicData() {
		return publicData;
	}
	
	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public String getPrivateData() {
		return privateData;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}
	
	public void finalize(String password) {
		this.privateData = OTP.encrypt(privateData, password);
	}
	
	@Override
	public String toString() {
		return "{\"public\": \""+publicData+"\", \"private\": \""+privateData+"\", \"pk\": \""+Base64.getEncoder().encodeToString(Keys.toPEM(publicKey).getBytes())+"\", \"sig\": \""+signature+"\"}";
	}

}


