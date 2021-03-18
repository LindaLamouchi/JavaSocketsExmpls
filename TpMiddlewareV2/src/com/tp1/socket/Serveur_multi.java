package com.tp1.socket;

import java.net.*;
import java.io.*;
public class Serveur_multi {

	ServerSocket ServerSock; // socket du serveur
	/* Port d'écoute */

	private int port; //8500 pour le mini client
	final String Finish = "" + (char) 4; //Signal de fin de connection aussi nommé EOT ctrl-d

	public Serveur_multi(int cport) throws IOException {
		port = cport;
		this.ServerSock = new ServerSocket(port); //Creation du gestionnaire de socket
		System.out.format("Serveur Amélioré lancé sur le port "+port+"\n", null);
	}
	private void terminer(Socket ma_connection){
		if (ma_connection != null)
		{
			try {
				ma_connection.close();
				System.out.format("Socket fermee \n",null);
			}
			catch ( IOException e ) { System.out.println("Could not close the socket .... \n");} // do nothing 

		}}

	public void run() {
		Socket ma_connection = null; // socket Service-cli
		while (true) {
			// // /* Attente bloquante connexion */
			try {
				ma_connection = this.ServerSock.accept();
			} catch (IOException e) {
				System.out.println("Impossible de détacher une socket: " + e);
				System.exit(-1);
			}
			int c_port = ma_connection.getPort(); 
			String c_ip = ma_connection.getInetAddress().toString();
			System.out.format("Un client est arrivé avec IP : "+c_ip+" sur le port "+c_port+"\n", null);

			/* On traite le client que l'on a associé */
			try {
				Service_Client(ma_connection);
			} catch (IOException e) {
				System.out.println("Erreur de Service Client : " + e);
				System.exit(-1); }
		}
	}
	private boolean Service_Client(Socket la_connection) throws IOException {
		
		InputStreamReader isr = new InputStreamReader(la_connection.getInputStream());
		
		BufferedReader flux_entrant = new BufferedReader(isr);
		System.out.println("Tampon entree attache ");
		// On récupère la file de sortie
		PrintWriter ma_sortie = new PrintWriter(la_connection.getOutputStream(), true);
		System.out.println("Sortie attachée");

		String clientName = la_connection.getRemoteSocketAddress().toString(); //get the Cli address
		System.out.format("Prêt à servir le Mini_Client "+clientName+"\n", null);
		String message_lu = new String();
		int line_num = 0;
		 
		
		ma_sortie.format("Bonjour "+clientName+" j attends tes données \n",null);
		while ((message_lu = flux_entrant.readLine()) != null) {
			System.out.format(line_num+" : -> "+message_lu+"\n", null);
			line_num++;
			/* si on recoit Finish on clot et annonce cette terminaison */
			if (message_lu.contains(Finish)) {
				System.out.println("Reception de " + Finish  + " -> Transmission finie");
				// On ferme la connection
				System.out.format("Je ferme la connection "+clientName+" :\n",null);
				terminer(la_connection);
				return (true);
			}
		}
		return false;
	}
	public static void main(String[] args) {
		/* On crée puis lance le serveur */
		Serveur_multi Mon_serveur = null;
		if (args.length != 1) {
			System.err.println("usage: java "+ Serveur_multi.class.getCanonicalName() + " serverPort");
			System.exit(-1);
		}
		try {
			Mon_serveur = new Serveur_multi(Integer.parseInt(args[0]));
		} catch (NumberFormatException e) {
			System.out.println("Format du port incorrect \n: format exception for " + e.getMessage());
			System.exit(-1);

		} catch (IOException e) {
			System.out.println("Impossible de créer le socket server : " + e);
			System.exit(-1);
		}
		Mon_serveur.run();
	}
}
