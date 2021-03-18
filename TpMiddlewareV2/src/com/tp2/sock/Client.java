package com.tp2.sock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
public static void main(String[] args) {
	final Socket sockCli;
	final PrintWriter out;
	final BufferedReader in;
	final Scanner sc;
	try {
		sockCli=new Socket("127.0.0.1",5000);
		in=new BufferedReader(new InputStreamReader(sockCli.getInputStream()));
		out=new PrintWriter(sockCli.getOutputStream());
		sc=new Scanner(System.in);
		Thread envoyer=new Thread(new Runnable() {
			String msg;
			public void run() {
				while(true) {
					msg=sc.nextLine();
					out.println(msg);
					out.flush();
				}
				
			}
		});
		envoyer.start();
		Thread receiver=new Thread(new Runnable() {
			String msg;
			
			public void run() {
				// TODO Auto-generated method stub
			try {
				msg=in.readLine();
				while(msg!=null) {
					System.out.println("Serveur: "+msg);
					msg=in.readLine();
				}
				System.out.println("Serveur deconnecte");
				out.close();
				sockCli.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			}
		});
		
		receiver.start();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
