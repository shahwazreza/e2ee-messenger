#  End-to-End Encrypted Messenger (Java)

A minimal end-to-end encrypted messaging system built in Java.  
Clients exchange public keys, derive a shared secret, and send encrypted messages through a relay server that cannot read message contents.

This project demonstrates real E2EE fundamentals:
- Public key exchange
- Diffie-Hellman shared secret
- AES-GCM authenticated encryption
- Secure client-to-client messaging over a network


##  Features

- End-to-end encryption  
- Server cannot decrypt messages
- Public key registration + retrieval  
- Real network messaging (localhost)  
- Multi-client communication  
- AES-GCM authenticated encryption  
- X25519 key agreement  


## File Structure

```
e2ee-messenger/
│
├── Server.java
├── Client.java
├── Main.java
│
├── crypto/
│   ├── KeyManager.java
│   ├── KeyDerivation.java
│   └── Encryption.java
│
└── README.md
```


## How It Works

### 1. Key Generation
Each client generates:
- X25519 key pair
- Public key shared with server
- Private key stored locally

### 2. Key Exchange
Client requests peer public key from server and derives a shared secret using Diffie-Hellman.

### 3. Encryption
Messages are encrypted using:

AES/GCM/NoPadding

### 4. Message Flow

Sender → encrypt → server relay → receiver → decrypt

Server only forwards ciphertext.


##  How to Run

### Compile
From project root:

```powershell
javac *.java crypto/*.java
```

### Start Server
```powershell
java Server
```

### Start Clients (in separate terminals)

Terminal 1:
```powershell
java Client Bob Alice
```

Terminal 2:
```powershell
java Client Alice Bob
```

Type messages in either terminal.


##  Security Properties

- End-to-end encryption
- Authenticated encryption (AES-GCM)
- Secure random IV generation
- Diffie-Hellman key agreement
- Server has zero access to plaintext



## Planned Improvements

- GUI chat interface
- Persistent key storage
- Group messaging
- Secure session management

---
