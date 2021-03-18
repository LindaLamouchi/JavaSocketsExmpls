/**
 * 
 */
package com.Tp.Matrice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author AlDo
 *
 */
public class ServeurObject {

	/**
	 * Methodes de la classe Serveur
	 */
	private static int[][] multiplication (int n,int[][] A , int[][] B){
		int[][] C = new int[n][n];
		for (int i =0 ; i<n ;  i++){
			for ( int j=0; j<n ; j++){
				C[i][j] = 0;
				for (int k=0 ; k<n ; k++){
					C[i][j] += A[i][k]*B[k][j]; 
				}
			}
		}
		return C ;
	}
	public ServeurObject() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	static int n;	
	static int[][] A;
	static int[][] B;
	static int[][] C;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final ServerSocket SocketServ ;
		final Socket SocketServiceCli ;
		final ObjectInputStream in;
		final ObjectOutputStream out;

		System.out.println("Coté Serveur : ");

		try {

			SocketServ = new ServerSocket(5000);
			SocketServiceCli = SocketServ.accept();
			System.out.println(" Requette acceptée ");

			in = new ObjectInputStream(SocketServiceCli.getInputStream());
			out = new ObjectOutputStream(SocketServiceCli.getOutputStream());



			final	 Thread receive=new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub


					try {
						n = in.readInt();
						A = new int[n][n];
						B = new int[n][n];
						A = (int[][])in.readObject();
						B = (int[][])in.readObject();
						C = new int[n][n];

						C = multiplication(n,A,B);
						out.writeObject(C);

					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	





				}
			});

			receive.start();

			Thread envoie=new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

					try {
						receive.join();
						out.writeObject(C);

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}
			});

			envoie.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}






	}

}
