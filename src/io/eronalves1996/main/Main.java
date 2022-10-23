package io.eronalves1996.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(5000);
		System.out.println("Waiting a connection");
		Socket s1 = ss.accept();
		System.out.println("First connection ready");
		BufferedReader br1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream()));
		bw1.write("Waiting a second connection");
		bw1.newLine();
		bw1.flush();
		Socket s2 = ss.accept();
		System.out.println("Second connection ready");
		BufferedReader br2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
		BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(s2.getOutputStream()));
		String message = "Chat ready";
		bw1.write(message);
		bw2.write(message);
		bw1.newLine();
		bw2.newLine();
		bw1.flush();
		bw2.flush();
		while(true) {
			if(br1.ready()) {
				bw2.write("USER 1: " + br1.readLine() + "\n");
				bw2.flush();
			}
			if(br2.ready()) {
				bw1.write("USER 2: " + br2.readLine() + "\n");
				bw1.flush();
			}
		}
	}

}
