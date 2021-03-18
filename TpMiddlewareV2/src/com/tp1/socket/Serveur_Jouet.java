package com.tp1.socket;
import java.net.*;
import java.io.*;
import java.util.*;

public class Serveur_Jouet {
	// FINISH string
	private static final String Finish=""+(char) 4;; //crtl d
	private static ServerSocket gestionnaire_de_connexion; // Objet gerant les sockets
	private final static int port = 12000; /* Port d'écoute */

	private static Socket ma_connection; /* file instanciée */

	public static void main(String[] args) {

		try{
			gestionnaire_de_connexion = new ServerSocket(port);
			System.out.println("Serveur Jouet lancé sur " + (port) );
		}
		catch (IOException e) {
			System.out.format(" Cannot create the server on port"+port+" it may be busy\n",null);
			System.exit(-1); }

		try{
			// Attente d'une bloquante connexion
			System.out.println("waiting for connexion...................................") ;
			ma_connection = gestionnaire_de_connexion.accept();

			// Connection recupérée, on determine l'ip et le port
			String c_ip = ma_connection.getInetAddress().toString() ;
			int c_port= ma_connection.getPort();
			System.out.format("client admis IP "+c_ip+" sur le port "+c_port+" \n",null);

			/* On Associe un tampon pour lire sur le flux connection
Input streamreader permet de transformer le flux d'octets en flux de
caracteres
le second argument et le type d'encodage des caractere --utf-8, isoXXXX
etc ... */
			InputStreamReader isr = new InputStreamReader(ma_connection.getInputStream(), "UTF-8");
			// Une seconde encapsulation qui permet d'améliorer les perfomances en lisant par blocs -- pour les gros fichiers
			BufferedReader flux_entrant = new BufferedReader(isr) ;
			System.out.println("  Mon Tampon de lecture est attaché ");

			// Stream de sortie, getOutputStream renvoie un Outputstream sur lequel on peut juste écrire des bit
			// PrintWriter l'encapsule ce qui permet d'érire comme sur Sys
			// le second parametre impose l option autoflush .. ce qui evite de faire de forcer l'envoi des messages partout
			PrintWriter ma_sortie = new PrintWriter(ma_connection.getOutputStream() ,true);
			System.out.println(" \n Mon Tampon pour ecrire attaché ");

			System.out.format("Pret à servir la machine : IP "+c_ip+" sur le port "+c_port+"\n",null);
			ma_sortie.format("Hello "+c_ip+" sur le port "+c_port+", vous etes, pour faire simple,disons Admis\n" , null );

			String message_lu = new String();
			int line_num =0 ;
			/* On lit une ligne dans le flux_entrant La fonction readline est
Bloquante
La condition du while fait diverses choses
elle attend que le client ai ecrit au moins une ligne
si la connection est brisée ou fautive ce message vaudra null et l'on
quitera la boucle while
			 */
			while ( (message_lu = flux_entrant.readLine()) != null ){
				// Si le client demande de terminer
				if (message_lu.contains(Finish) ){
					// on termine proprement
					System.out.format (message_lu+"Serveur : Bien recu, Transmission finie\n",null);
					ma_sortie.println("Fin de connexion");
					terminer();
				}
				System.out.format( line_num +"--> ' "+ message_lu+" ' de la part du client \n",null);
				line_num++;
			}
			// Si on est ici à priori le client à fermé la connection, sans envoyer finish (pex on peut tuer le processus telnet)
			System.out.println( "Mini_Client deconnecté, je termine\n" ) ;
			terminer();

			//telnet 127.0.0.1 12000 cmd 
		}
		catch (IOException e) {
			System.err.println(" Erreur de reception");
			e.printStackTrace(); terminer();}

	}
	private static void terminer(){
		try{
			if (ma_connection != null) ma_connection.close();
			if (gestionnaire_de_connexion != null)
				gestionnaire_de_connexion.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

}
