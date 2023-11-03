# multichat Application

A simple multi-chat application implemented in Java using sockets.

## Features

- Allows multiple clients to connect to a server.
- Clients can send messages to the server, and the server will broadcast the messages to all connected clients.
- Clients can running in GUI for suppported OS and for CLI you can use in folder Client_CLI.

## How to Use
  1. Clone the repository to your local machine:
  
     ```bash
     git clone https://github.com/roomgnh/multichat.git
     
  
  2. Compile the server and client code.
     
  - Code for compile server you can use:
    
      ```bash
      javac ChatServer.java
      
  
  - Code for compile client you can use:
    
      ```bash
      javac ChatClient.java
      
  
  3. Run the server and client code. 
  - Run the server:
    
      ```bash
      java ChatServer
      
  
  - Run the client(s) on separate terminals or machines:
    
      ```bash
      java ChatClient
      
  
  4. Connect to the server and start chatting!

## Configuration
You can customize the server port by modifying the port number in the ChatServer.java and ChatClient.java file and customize the server ip by modifying the ip address in the ChatClient.java. 

## Acknowledgements
This project is a simple demonstration of socket programming in Java.
