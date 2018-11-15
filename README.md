# MAM Lite - an alternative messaging protocol for IOTA

MAM Lite is a  lightweight, flexible and easy to use protocol which ensures privacy and integrity for data communication at another level. Detailed information can be found here: https://medium.com/@samuel.rufinatscha/mam-lite-an-alternative-messaging-protocol-for-iota-562fdd318e1d

Features
- Authentication
- Forward Secrecy
- Spam protection
- Stream access from every address
- Channel splitting
- Multipart messages (Public & Private)
- Different encryption modes (OTP and public key encryption for fine grained access)

Every message author does need a RSA key pair. This can be created easily:

     KeyPair keys = RSA.generateKeyPair();
     PublicKey publicKey = keys.getPublic();
     PrivateKey privateKey = keys.getPrivate();

A channel can be created as follows:

    MAMLite m = new MAMLite(address, password);

Publishing of a message:
    
    Message m = new Message("My public data", "My private data", publicKey);
    m.write(m, privateKey);
    
Reading of a message:

    Message m = m.read();
