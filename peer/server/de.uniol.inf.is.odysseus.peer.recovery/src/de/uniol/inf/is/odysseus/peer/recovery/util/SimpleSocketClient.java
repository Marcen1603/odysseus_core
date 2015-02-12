package de.uniol.inf.is.odysseus.peer.recovery.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

/**
 *
 * A complete Java class that demonstrates how to use the Socket class, specifically how to open a socket, write to the
 * socket, and read from the socket.
 *
 * @author alvin alexander, devdaily.com. modified by Tobias Brandt
 *
 */
public class SimpleSocketClient {

	public SimpleSocketClient(String ip, int port, String info) {
		String testServerName = ip;
		try {
			// open a socket
			Socket socket = openSocket(testServerName, port);

			// write-to, and read-from the socket.
			// in this case just write a simple command to a web server.
			writeToAndReadFromSocket(socket, info);

			// close the socket, and we're done
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String writeToAndReadFromSocket(Socket socket, String writeTo) throws Exception {
		try {
			// write text to the socket
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			bufferedWriter.write(writeTo);
			bufferedWriter.flush();

			// read text from the socket
			// BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// StringBuilder sb = new StringBuilder();
			// String str;
			// while ((str = bufferedReader.readLine()) != null) {
			// sb.append(str + "\n");
			// }

			// close the reader, and return the results as a String
			// bufferedReader.close();
			// return sb.toString();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Open a socket connection to the given server on the given port. This method currently sets the socket timeout
	 * value to 10 seconds. (A second version of this method could allow the user to specify this timeout.)
	 */
	private Socket openSocket(String server, int port) throws Exception {
		Socket socket;

		// create a socket with a timeout
		try {
			InetAddress inetAddress = InetAddress.getByName(server);
			SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);

			// create a socket
			socket = new Socket();

			// this method will block no more than timeout ms.
			int timeoutInMs = 10 * 1000; // 10 seconds
			socket.connect(socketAddress, timeoutInMs);

			return socket;
		} catch (SocketTimeoutException ste) {
			System.err.println("Timed out waiting for the socket.");
			ste.printStackTrace();
			throw ste;
		}
	}

}