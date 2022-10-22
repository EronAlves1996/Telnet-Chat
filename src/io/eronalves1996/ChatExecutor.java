package io.eronalves1996;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatExecutor {

	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(5000);
		System.out.println("Waiting a connection");
		Socket s = ss.accept();
		System.out.println("Connected with: " + s.getRemoteSocketAddress());
		BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		while(true) {
			if(br.ready()) {
				bw.write(br.readLine());
				bw.newLine();
				bw.flush();
			}
		}
	}

}
