package com.Tp.Matrice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	public Client() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws ClassNotFoundException {
		final Socket clientSocket;
		final ObjectInputStream in;
		final ObjectOutputStream out;
		final Scanner sc = new Scanner(System.in);
		
		int n;
		int[][] A;
		int[][] B;
		int[][] C;		
		
		System.out.println(" Entrer la taille des deux matrices carrées SVP! ");
		n = sc.nextInt();
		while ( n <= 0 ) {
			System.out.println(" la Taille entrée devera etre positive superieure a 0");
			n = sc.nextInt();			
		}
		
		A = new int[n][n];
		B = new int[n][n];
		
		
		System.out.println("remplissage de A :");
		for (int i = 0 ;i<n; i++) {
			for (int j=0; j<n ; j++) {				
				System.out.println("A["+i+"]["+j+"] = ");
				A[i][j] = sc.nextInt();
			}
		}
		
		System.out.println("remplir la matrice B :");
		for (int i = 0 ;i<n; i++) {
			for (int j=0; j<n ; j++) {				
				System.out.println("B["+i+"]["+j+"] = ");
				B[i][j] = sc.nextInt();
			}
		}
		
		
		try {
			/*
			* 
			* 
			*/
			clientSocket = new Socket("127.0.0.1",5000);
			out = new ObjectOutputStream(clientSocket.getOutputStream()); 
			in = new ObjectInputStream(clientSocket.getInputStream());
			out.writeInt(n);			
			out.flush();
            //envoi de la matrice A
			out.writeObject(A);			
			out.flush();
			//envoi de la matrice B
			out.writeObject(B);
			out.flush();			
			//Lecture de la matrice C
			C = new int[n][n];
			C = (int[][])in.readObject();
			//Affichage de la matrice C
			System.out.println("resultat est : ");
			for (int i =0 ; i<n ;  i++){
				for ( int j=0; j<n ; j++){
					System.out.print(C[i][j]+" ; ");
				}	
			}
		    
			System.out.println("Fin du traitement .............................................................");
			out.close();
			clientSocket.close();			
			
		 } catch (IOException e) {
			 e.printStackTrace();
		 } 
}
}