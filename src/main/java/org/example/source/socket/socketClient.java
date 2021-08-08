package org.example.source.socket;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;

public class socketClient {
    public static void main(String[] args) throws Exception{
//        向本机8888端口发出请求
        Socket socket = new Socket("127.0.0.1", 9000);
//        系统标准输入设备构造BufferReader对象
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
//        由socket对象得到输出流，并构造PrintWriter对象
        PrintWriter os = new PrintWriter(socket.getOutputStream());
//        由socket对象得到输入流，并构造相应的BufferedReader对象
        BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        由系统标注输入读入数据
        String readLine = sin.readLine();
        while (!readLine.equals("bye")){
//        将系统标注输入读入的字符串输出到server
            os.println(readLine);
//        刷新输出流，使Server上收到该字符串
            os.flush();
//        在系统标准输出上打印输入的字符串
            System.out.println("Client say："+ readLine);
//        从server读入一字符串，并打印到输出上
            System.out.println("Server say:"+ is.readLine());
//        从系统标准读入一字符串
            readLine = sin.readLine();
        }
//        关闭socket输出流
        os.close();
//        关闭socket输入流
        is.close();
//        关闭socket
        socket.close();
    }
}
