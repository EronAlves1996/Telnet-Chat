package io.eronalves1996.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.jupiter.api.Test;

import io.eronalves1996.main.Main;

public class MainTest {

	Socket[] sockets = new Socket[2];

	@Test
	public void testFirstMessage() throws IOException, InterruptedException, ExecutionException {
		execute();
		InetAddress host = InetAddress.getLocalHost();
		sockets[0] = new Socket(host.getHostAddress(), 5000);
		BufferedReader br1 = new BufferedReader(new InputStreamReader(sockets[0].getInputStream()));
		assertEquals("Waiting a second connection", br1.readLine());
	}

	@After
	public void tearDown() throws IOException {
		sockets[0].close();
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
}
