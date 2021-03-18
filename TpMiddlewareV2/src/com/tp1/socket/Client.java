package com.tp1.socket;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
public class Client { /* define the client class */
	private final String hote;
	private final int port;
	private final String id;
	private Scanner console_input;
	final String Finish = "" + (char) 4; //Signal de fin de connection aussi nommé EOT ctrl-d
	byte[] datas=null;
	private String fileName;

	public Client(String hote, int port, String mon_id) {
		this.hote = hote;
		this.port = port;
		this.id = mon_id;
	}
	private void getdata() {
		Boolean wait_for_file = true;
		console_input = new Scanner(System.in);
		while (wait_for_file) {
			//System.out.println("Entrez le nom du ficher à envoyer");
			fileName = console_input.next();

			System.out.println("Opening " + fileName);
			try {

				Path file_path = Paths.get(fileName);
				datas = Files.readAllBytes(file_path);
				System.out.println(datas.length + " Bytes Read ");
				return;
			} catch (java.nio.file.InvalidPathException e) {
				System.out.println("path is incorrect");
			} catch (java.nio.file.NoSuchFileException e) {
				System.out.format("Absence du fichier %s\n",
						fileName);
				continue;
			} catch (IOException e) {
				System.out.println("Cannot read data, is the file there ? : " + e);
				continue;
			}
		}
	}
	public void execute() {
		Socket la_connection = null;
		OutputStream os;
		PrintWriter ma_sortie;
		try {
			la_connection = new Socket(this.hote, this.port);
		} catch (IOException e) {
			System.out.format("Probleme de connection avec le serveur %s\n",e);
			System.exit(-1);}
		try {
			os= la_connection.getOutputStream();
			ma_sortie = new PrintWriter(os, true);
			System.out.println(" Contacting " + this.hote + " on " +this.port);
			ma_sortie.println("Hello je suis :" + this.id);
			getdata();
			os.write(datas, 0, datas.length);
			System.out.println("Données envoyées, envoi de la terminaison");
			ma_sortie.println(Finish);
		}
		catch (IOException e) {
			System.out.println("data not fully transmited : " + e);}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Il me faut 3 arguments: hote port identifiant");
			System.exit(1);}
		try {
			new Client(args[0], Integer.parseInt(args[1]),args[2]).execute();}
		catch (NumberFormatException e) {
			System.out.format("Mauvais format du port\n %s\n",e.getMessage());
			System.exit(-1);
		}
	}
}