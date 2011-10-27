package de.offis.salsa.lms;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.offis.salsa.lms.impl.SickConnectionImpl;

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
			Date date = new Date();
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			Calendar calendar2 = Calendar.getInstance();
			System.out.println(date.getTime()+" "+System.currentTimeMillis()+" "+calendar.getTimeInMillis()+" "+calendar2.getTimeInMillis());
		System.out.println("Time:"+ calendar.get(Calendar.HOUR_OF_DAY)+" "+calendar2.get(Calendar.HOUR_OF_DAY));
		}
	}
}
