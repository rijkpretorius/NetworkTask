# Java Server-Client Communication Demo

A simple demonstration of server-client communication implemented in Java. This project was developed as part of a university coursework assignment to explore networking fundamentals and socket programming.

## Overview

This application demonstrates basic network communication between a server and a client using Java. It showcases how data is transmitted, received, and processed across a local network connection.

## Technologies Used

* **Language:** Java (JDK 25)
* **IDE:** IntelliJ IDEA
* **Networking:** Java Sockets (`java.net.Socket`, `java.net.ServerSocket`) 

## Features

* Establishes a connection between a server and one or more clients.
* Sends and receives messages/data over a specified port.
* Handles connection termination gracefully.

## Getting Started

### Prerequisites

* Java Development Kit (JDK) installed on your machine.
* IntelliJ IDEA (or any preferred Java IDE / terminal).

### Running the Project in IntelliJ IDEA

1. **Clone the repository:**
   ```bash
   git clone [Insert your repository URL here]
   ```
2. **Open the project:**
   * Launch IntelliJ IDEA.
   * Select **File > Open** and navigate to the cloned project folder.
3. **Start the Server:**
   * Locate the server main class (e.g., `ServerCosnole.java`).
   * Right-click the file and select **Run 'ServerConsole.main()'**.
   * Ensure the console indicates that the server is listening on the designated port (e.g., `Port 8080`).
4. **Start the Client:**
   * Locate the client main class (e.g., `ClientNode.java`).
   * Right-click the file and select **Run 'ClientNode.main()'**.
   * Follow the console prompts to send messages to the server.

## Project Structure

```text
├── src/
│   ├── BrokerServer.java    # Handles incoming client connections and requests
│   └── ClientNode.java    # Connects to the server and sends/receives data
└── README.md
```

## Academic Context

This project was created solely for educational purposes as part of university coursework. 
