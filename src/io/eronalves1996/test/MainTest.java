package io.eronalves1996.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.eronalves1996.main.Main;

public class MainTest {

	private Socket[] sockets = new Socket[2];
	private InetAddress host;
	
	@BeforeEach
	public void before() throws UnknownHostException {
		host = InetAddress.getLocalHost();
	}

	@Test
	public void testMain() throws IOException, InterruptedException, ExecutionException {
		execute();
		Thread.sleep(100); // Necessary for await the another thread to start the Socket Server
		sockets[0] = new Socket(host.getHostName(), 5000);
		BufferedReader br1 = extractBufferedReader(sockets[0].getInputStream());
		assertEquals("Waiting a second connection", br1.readLine());
		sockets[1] = new Socket(host.getHostName(), 5000);
		BufferedReader br2 = extractBufferedReader(sockets[1].getInputStream());
		assertEquals("Chat ready", br2.readLine());
		assertEquals("Chat ready", br1.readLine());
	}

	@AfterEach
	public void tearDown() throws IOException {
		sockets[0].close();
		sockets[1].close();
	}
	
	private void execute() {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.submit(()->{
			try {
				Main.main(null);
			} catch (IOException e) {
			}
		});
	}
	
	private BufferedReader extractBufferedReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}
}
