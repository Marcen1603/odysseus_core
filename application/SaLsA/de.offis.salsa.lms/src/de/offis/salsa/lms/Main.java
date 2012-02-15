package de.offis.salsa.lms;

import java.io.FileNotFoundException;
import java.io.IOException;
import de.offis.salsa.lms.impl.SickConnectionImpl;
/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length >= 2) {
			final String host = args[0];
			final int port = Integer.parseInt(args[1]);
			final SickConnection connection;

			connection = new SickConnectionImpl(host, port);

			try {
				connection.open();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (connection.isConnected()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("Invalid arguments: <Host/IP> <Port>");
		}
	}
}
