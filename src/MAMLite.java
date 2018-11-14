import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONObject;

import com.google.common.hash.Hashing;

import crypto.Keys;
import crypto.OTP;
import crypto.RSA;
import crypto.RSAException;
import jota.IotaAPI;
import jota.model.Bundle;
import jota.model.Transaction;
import jota.model.Transfer;
import jota.utils.TrytesConverter;

public class MAMLite {

	public static String protocol = "https";
	public static String host = "nodes.thetangle.org";
	public static String port = "443";
	public static int depth = 3;
	public static int minWeightMagnitude = 14;
	
	private IotaAPI api = new IotaAPI.Builder().protocol(protocol).host(host).port(port).build();

	private String rootAddress;
	private String channelPassword;
	private String currentWriteAddress;
	private String currentReadAddress;

	public MAMLite(String rootAddress) {
		this.rootAddress = rootAddress;
		this.channelPassword = "";
		load(rootAddress);
	}

	public MAMLite(String rootAddress, String channelPassword) {
		this.rootAddress = rootAddress;
		this.channelPassword = channelPassword;
		load(rootAddress);
	}

	public void load(String rootAddress) {
		
		System.out.println("LOADING CHANNEL...");
		
		this.rootAddress = rootAddress;
		currentWriteAddress = null;
		currentReadAddress = rootAddress;
		
		String prevAddress = null;
		List<Transaction> x = null;
		
		do {
		
			do {
				try {
					x = api.findTransactionObjectsByAddresses(new String[] { currentReadAddress });
					break;
				} catch (Exception e) { e.printStackTrace(); }
			} while (true);
			
			if(x.isEmpty()) {
				System.out.println("EMPTY: "+currentReadAddress);
				break;
			}
			
			System.out.println("DATA FOUND: "+currentReadAddress);
			
			prevAddress = currentReadAddress;
			currentReadAddress = hash(currentReadAddress + channelPassword);
			
		} while(true);
		
		if(!currentReadAddress.equals(rootAddress))
			currentWriteAddress = prevAddress;
		
		currentReadAddress = null;
		
		System.out.println("LOADED...");
		
	}

	public String write(Message message, PrivateKey privateKey) throws RSAException {

		if(currentWriteAddress == null)
			currentWriteAddress = rootAddress;
		else
			currentWriteAddress = hash(currentWriteAddress + channelPassword);
		
		message.setAddress(currentWriteAddress);
		message.finalize(currentWriteAddress + channelPassword);
		message.setSignature(RSA.sign(hash(message.getPublicData() + message.getPrivateData()), privateKey));
		
		List<Transfer> transfers = new ArrayList<>();
		Transfer t = new Transfer(currentWriteAddress, 0, TrytesConverter.asciiToTrytes(message.toString()), "");
		transfers.add(t);
		
		boolean loop = true;
		do {
			try {
				api.sendTransfer(rootAddress, 2, depth, minWeightMagnitude, transfers, null, currentWriteAddress, false, false, null);
				loop = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (loop);
		
		return message.toString();

	}

	public Message read() throws IOException, RSAException {
		
		String previousAddress = currentReadAddress;
		
		if(currentReadAddress == null)
			currentReadAddress = rootAddress;
		else currentReadAddress = hash(currentReadAddress + channelPassword);

		boolean loop = true;
		List<Transaction> x = null;
		String data = "";
		
		do {
			try {
				x = api.findTransactionObjectsByAddresses(new String[] { currentReadAddress });
				
				if (x.isEmpty()) {
					currentReadAddress = previousAddress;
					return null;
				}
				
				Bundle b = api.bundlesFromAddresses(new String[] {currentReadAddress}, false)[0];
				for (Transaction t : b.getTransactions())
					data += t.getSignatureFragments();
				
				loop = false;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (loop);

		data = data.substring(0, data.indexOf("999999999"));
		data = TrytesConverter.trytesToAscii(data);
		
		JSONObject o = new JSONObject(data);
		
		String publicData = o.get("public").toString();
		String privateData = o.get("private").toString();
		String decryptedData = OTP.decrypt(privateData, currentReadAddress+channelPassword);
		String signature = o.get("signature").toString();
		String publicKeyPEM = new String(Base64.getDecoder().decode(o.get("publicKey").toString()));
		
		PublicKey publicKey = (PublicKey) Keys.fromPEM(publicKeyPEM);
		
		boolean valid = RSA.verify(hash(publicData + privateData), signature, publicKey);
		System.out.println("TRUSTED SIGNATURE: "+valid);

		
		Message ret = new Message(publicData, decryptedData, publicKey);
		ret.setAddress(currentReadAddress);
		ret.setSignature(signature);

		return ret;

	}
	
	public void split(String channelPassword) {
		this.channelPassword = channelPassword;
		currentReadAddress = currentWriteAddress;
	}
	
	public void setChannelPassword(String channelPassword) {
		this.channelPassword = channelPassword;
	}
	
	public static String hash(String s) {
		String hash = Hashing.sha512().hashString(s, StandardCharsets.UTF_8).toString();
		hash = TrytesConverter.asciiToTrytes(hash).substring(0, 81);
		return hash;
	}
	
	public static String generateSecureRootAddress(String seed) {
		return hash(seed);
	}

}