package mg.dynaquest.wrapper.access;

/** 
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.8 $
 Log: $Log: HttpAccess.java,v $
 Log: Revision 1.8  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.7  2004/09/07 16:43:58  grawund
 Log: no message
 Log:
 Log: Revision 1.6  2004/07/30 10:46:31  grawund
 Log: no message
 Log:
 Log: Revision 1.5  2004/07/28 11:29:47  grawund
 Log: POManager erste Version integriert
 Log:
 Log: Revision 1.4  2003/08/20 07:41:24  grawund
 Log: Exceptions konkretisiert
 Log: Refactoring: Methoden post und get basieren jetzt auf dem selben Code aus der Methode access
 Log:
 Log: Revision 1.3  2002/02/06 14:02:09  grawund
 Log: Einbindung beliebiger Araneus-Konverter moeglich
 Log:
 */

import java.net.*;
import java.io.*;

public class HttpAccess {

	private static BufferedReader access(boolean get, URL url, String params,
			int maxConnectionTries) throws MalformedURLException,
			FileNotFoundException, IOException, ProtocolException {
		BufferedReader in = null;
		int tries = 0;
		boolean retry = true;
		while (retry) {
			try {
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				//System.out.println(" connection nach "+url+" aufgebaut");
				//connection.setDoOutput(true);
				connection.setRequestProperty("User-Agent", "MarcosWrapper");
				if (get) {
					connection.setRequestMethod("GET");
				} else {
					connection.setDoInput(true);
					connection.setAllowUserInteraction(false);
					connection.setUseCaches(false);
					connection.setRequestMethod("POST");
					connection.setRequestProperty("Content-type",
							"application/x-www-form-urlencoded");

					// Parameter in den Strom schreiben
					DataOutputStream out = new DataOutputStream(connection
							.getOutputStream());
					out.writeBytes(params);
					out.flush();
					out.close();
					connection.disconnect();
				}
				//long start = System.currentTimeMillis();
				//System.out.println(" lese Daten aus 0 "+url+" "+0);
				// Anmerkung: Das hier dauert ewig... Warum wohl ...
				// Weil unter den Run-Options proxy ausgewählt war!!
				connection.connect();
				//System.out.println(" lese Daten aus 1 "+url+"
				// "+(System.currentTimeMillis()-start));
				InputStream stream = connection.getInputStream();
				//System.out.println(" lese Daten aus 2 "+url+"
				// "+(System.currentTimeMillis()-start));
				InputStreamReader reader = new InputStreamReader(stream);
				//System.out.println(" lese Daten aus 3 "+url+"
				// "+(System.currentTimeMillis()-start));
				in = new BufferedReader(reader);
				//System.out.println(" fertig");
				// Wenn ich hier bin, ist alles gut gelaufen
				retry = false;
			} catch (FileNotFoundException e) {
				tries++;
				if (tries > maxConnectionTries) {
					retry = false;
				} else {
					throw e;
				}
			}
		}
		return in;
	}

	public static BufferedReader get(URL url, int maxConnectionTries)
			throws MalformedURLException, FileNotFoundException, IOException,
			ProtocolException {
		return access(true, url, "", maxConnectionTries);
	}

	public static BufferedReader post(URL url, String params,
			int maxConnectionTries) throws MalformedURLException,
			FileNotFoundException, IOException, ProtocolException {
		return access(false, url, params, maxConnectionTries);
	}

}