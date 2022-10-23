package io.eronalves1996.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.eronalves1996.main.Main;

public class MainTest {

	private Socket[] sockets = new Socket[2];
	private InetAddress host;
	private String chatReadyMess = "Chat ready";
	private String waiting2ndConnMess = "Waiting a second connection";
	private ExecutorService executor = null;
	private Future<?> thread;

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
	}

	@Test
	public void testSendMessages() throws IOException, InterruptedException, ExecutionException {
		execute();
		Thread.sleep(100); // Necessary for await the another thread to start the Socket Server
		sockets[0] = new Socket(host.getHostName(), 5000);
		BufferedReader br1 = extractBufferedReader(sockets[0].getInputStream());
		br1.skip(this.waiting2ndConnMess.length());
		sockets[1] = new Socket(host.getHostName(), 5000);
		BufferedReader br2 = extractBufferedReader(sockets[1].getInputStream());
		br2.skip(this.chatReadyMess.length());
		br1.skip(this.chatReadyMess.length());
		PrintWriter pw1 = extractPrintWriter(sockets[0].getOutputStream());
		PrintWriter pw2 = extractPrintWriter(sockets[1].getOutputStream());
		pw1.write("Oi");
		pw1.flush();
		assertEquals("USER 1: Oi", br2.readLine());
	}

	@AfterEach
	public void tearDown() throws IOException, InterruptedException {
		sockets[0].close();
		sockets[1].close();
		this.thread.cancel(true);
	}

	private void execute() {
		if(this.executor == null) this.executor = Executors.newSingleThreadExecutor();
		if(this.executor.isShutdown()) this.executor.notify();
		this.thread = executor.submit(()->{
			try {
				Main.main(null);
			} catch (IOException e) {
			}
			});
	}

	private BufferedReader extractBufferedReader(InputStream in) {
		return new BufferedReader(new InputStreamReader(in));
	}

	private PrintWriter extractPrintWriter(OutputStream out) {
		return new PrintWriter(new OutputStreamWriter(out));
	}
}
