import crypto.RSA;

import java.security.KeyPair;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		
		if(args.length >= 5) {
			MAML.protocol = args[0];
			MAML.host = args[1];
			MAML.port = args[2];
			MAML.depth = Integer.parseInt(args[3]);
			MAML.minWeightMagnitude = Integer.parseInt(args[4]);
		}
		
		System.out.println("MAM Lite 1.1\n");
		
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter a channel id: ");
		String channelID = in.nextLine();
		System.out.print("Enter channel password: ");
		String password = in.nextLine();

		String mamChannel = MAML.hash(channelID+password);
		System.out.println("CHANNEL START: "+mamChannel);
		
		KeyPair keys = RSA.generateKeyPair();
		

		
		MAML m = new MAML(mamChannel, password);
		
		System.out.println("\nEnter your command. Enter 'help' for help.");
		System.out.print("> ");
		
		do {
			
			switch (in.nextLine().trim()) {
			
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

			case "exit": {
			    return;
			}

			default:
				System.out.print("> ");
				break;
			}

		} while(true);

	}

}