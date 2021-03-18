package com.tp1.socket;
import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Mini_Client {

	private String hote = "127.0.0.1";
	private int port = 12000;
	private Scanner console_input;
	public Mini_Client() {}
	public void execute() {
	 console_input = new Scanner(System.in);
	 Socket laConnection = null;
	 try {
	 laConnection = new Socket(this.hote, this.port);
	 PrintWriter ma_sortie = new PrintWriter(
	 laConnection.getOutputStream(), true);
	 System.out.format(" Contacting "+hote+" on "+port+" \n",  null);
	 ma_sortie.println("Hello c est l un de vos client");
	 System.out.println("entrer les données");
	 while (true) {
	 String data = console_input.next();
	 ma_sortie.println(data);
	 if (data.equals("end")){
	 System.out.println("termine");
	 laConnection.close();
	 System.exit(0);
	 }
	 }
	 } catch (IOException e) {
	 System.out.format("Probleme de connection avec serveur fontionne : "+ e,null);
	 System.exit(-1);
	 }
	}
	public static void main(String[] args) {
		Mini_Client test = new Mini_Client();
	 test.execute();
	}


}
