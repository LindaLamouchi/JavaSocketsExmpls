package com.tp2.sock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur {

	public static void main(String[] args) {
	
	final	ServerSocket serverSocket;
	final	Socket servieCli;
	final BufferedReader in;
	final PrintWriter out;
	final Scanner sc=new Scanner(System.in);
	try {
		serverSocket=new ServerSocket(5000);
		servieCli=serverSocket.accept();
		in=new BufferedReader(new InputStreamReader(servieCli.getInputStream()));
		out=new PrintWriter(servieCli.getOutputStream());
		Thread envoie=new Thread(new Runnable() {
			String msg;
			
		public void run() {
				while(true) {
					msg=sc.nextLine();
					out.println(msg);
					out.flush();
				}
			}
		});
		
		envoie.start();
		
		Thread receiver=new Thread(new Runnable() {
			String msg;
			public void run() {
				try {
					msg=in.readLine();
					while(msg!=null) {
						System.out.println("Client:"+msg);
						msg=in.readLine();
					} //toufa si le client se deconnecte
					System.out.println("Client deconnecte");
					//fermer le flux
					out.close();
					servieCli.close();
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		receiver.start();
	}catch(Exception e) {
		System.out.println(" Erreur "+e);
	}
	

	}

}
