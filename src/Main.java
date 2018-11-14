import crypto.RSA;

import java.security.KeyPair;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws Exception {
		
		if(args.length >= 5) {
			MAMLite.protocol = args[0];
			MAMLite.host = args[1];
			MAMLite.port = args[2];
			MAMLite.depth = Integer.parseInt(args[3]);
			MAMLite.minWeightMagnitude = Integer.parseInt(args[4]);	
		}
		
		System.out.println("MAM LITE 1.9\n");
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter channel name: ");
		String seed = in.nextLine();
		System.out.print("Enter channel password: ");
		String sideKey = in.nextLine();

		String mamChannel = MAMLite.hash(seed);
		System.out.println("CHANNEL START: "+mamChannel);
		
		KeyPair keys = RSA.generateKeyPair();
		

		
		MAMLite m = new MAMLite(mamChannel, sideKey);
		
		System.out.println("\nEnter your command. Enter 'help' for help.");
		System.out.print("> ");
		
		do {
			
			switch (in.nextLine()) {
			
			case "read": {
				try {
				System.out.println(m.read());
				} catch(Exception e) {
					e.printStackTrace();
				}
				System.out.print("> ");
				break;
			}

			case "write": {
				System.out.print("Enter public data: ");
				String data = in.nextLine();
				System.out.print("Enter private data: ");
				String pData = in.nextLine();
				Message msg = new Message(data, pData, keys.getPublic());
				System.out.println(m.write(msg, keys.getPrivate()));
				System.out.print("> ");
				break;
			}
			
			case "split": {
				System.out.print("Enter new channel password: ");
				String key = in.nextLine();
				m.split(key);
				System.out.println("Password set.");
				System.out.print("> ");
				break;
			}
			
			case "load": {
				System.out.print("Enter address: ");
				String address = in.nextLine();
				
				System.out.print("Enter password: ");
				String k = in.nextLine();
				
				m.setChannelPassword(k);
				m.load(address);
				
				System.out.print("> ");
				break;
			}
			
			case "help": {
				System.out.println("Following commands are supported:\nwrite\nread\nsplit\nload");
				System.out.print("> ");
				break;
			}
			default:
				System.out.print("> ");
				break;
			}

		} while(true);
		

		
		
		

	}

}
