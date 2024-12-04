package Server.main;

import Server.main.DB.DB_connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

 class Server {
    private static final int PORT = 5000;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private Connection dbConnection;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            pool = Executors.newFixedThreadPool(10);
            System.out.println("Server started on port " + PORT);
            dbConnection= DB_connection.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket, dbConnection));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
