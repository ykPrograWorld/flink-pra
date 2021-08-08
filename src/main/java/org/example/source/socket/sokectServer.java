package org.example.source.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class sokectServer {
    public static void main(String[] args) throws Exception{
        ServerSocket socketServer = new ServerSocket(9000);
        Socket socket = socketServer.accept();

        BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter os = new PrintWriter(socket.getOutputStream());

        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("client say:"+ is.readLine());

        String line = sin.readLine();

        while(!line.equals("bye")){
            os.println(line);
            os.flush();
            System.out.println("Server+"+line);
            System.out.println("Client:"+is.readLine());
            line = sin.readLine();
        }

        os.close();
        is.close();
        socket.close();
        socketServer.close();
    }
}
