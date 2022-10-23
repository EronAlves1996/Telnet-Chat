package io.eronalves1996.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.eronalves1996.main.Main;

public class MainTest {

	private Socket[] sockets = new Socket[2];
	private InetAddress host;
	private String chatReadyMess = "Chat ready";
	private String waiting2ndConnMess = "Waiting a second connection";
	private ExecutorService executor = null;

	@BeforeEach
	public void before() throws UnknownHostException {
		host = InetAddress.getLocalHost();
	}

	@Test
	public void testMainStart() throws IOException, InterruptedException, ExecutionException {
		execute();
		Thread.sleep(100); // Necessary for await the another thread to start the Socket Server
		sockets[0] = new Socket(host.getHostName(), 5000);
		BufferedReader br1 = extractBufferedReader(sockets[0].getInputStream());
		assertEquals(this.waiting2ndConnMess, br1.readLine());
		sockets[1] = new Socket(host.getHostName(), 5000);
		BufferedReader br2 = extractBufferedReader(sockets[1].getInputStream());
		assertEquals(this.chatReadyMess, br2.readLine());
		assertEquals(this.chatReadyMess, br1.readLine());
		br1.close();
		br2.close();
	}

	@Test
	public void testSendMessages() throws IOException, InterruptedException, ExecutionException {
		execute();
		Thread.sleep(100); // Necessary for await the another thread to start the Socket Server
		sockets[0] = new Socket(host.getHostName(), 5000);
		BufferedReader br1 = extractBufferedReader(sockets[0].getInputStream());
		System.out.println(br1.readLine());
		sockets[1] = new Socket(host.getHostName(), 5000);
		BufferedReader br2 = extractBufferedReader(sockets[1].getInputStream());
		System.out.println(br1.readLine());
		System.out.println(br2.readLine());
		BufferedWriter pw1 = extractBufferedWriter(sockets[0].getOutputStream());
		BufferedWriter pw2 = extractBufferedWriter(sockets[1].getOutputStream());
		pw1.write("Oi\n");
		pw1.flush();
		assertEquals("USER 1: Oi", br2.readLine().replaceAll("\0", ""));
		pw2.write("Oi para ti\n");
		pw2.flush();
		assertEquals("USER 2: Oi para ti", br1.readLine().replaceAll("\0", ""));
		br1.close();
		br2.close();
		pw1.close();
		pw2.close();
	}

	@AfterEach
	public void tearDown() throws IOException, InterruptedException {
		sockets[0].close();
		sockets[1].close();
	}

	private void execute() {
		if(this.executor == null) this.executor = Executors.newSingleThreadExecutor();
		executor.execute( ()-> {
			try {
				Main.main(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private BufferedReader extractBufferedReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}

	private BufferedWriter extractBufferedWriter(OutputStream out) {
		return new BufferedWriter(new OutputStreamWriter(out));
	}
}
