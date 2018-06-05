/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.generator.smarthome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamServer;

/**
 * 
 * @author Dennis Geesen Created at: 12.01.2012
 */
public class DataReader {

	private BufferedReader in;
	private long delayMS = 1000;
	private int startPort = 50000;
	private HashMap<String, StreamServer> serverList = new HashMap<String, StreamServer>();
	private List<DataProvider> clients = new ArrayList<DataProvider>();
	private int lineNr = 0;
	private boolean rewindAfterEnd = true;
	private List<String> adls = new ArrayList<String>();

	public DataReader(int startPort, long delayInMS, String[] identifiers) throws Exception {
		this.delayMS = delayInMS;
		this.startPort = startPort;
		createServer(identifiers);
	}

	private void createServer(String[] identifiers) throws Exception {
		for (String identifier : identifiers) {
			createServer(identifier);
		}
	}

	private void createServer(String identifier) throws Exception {
		StreamServer s = new StreamServer(startPort, new DataProvider(identifier, this));
		this.serverList.put(identifier, s);
		startPort++;
		s.start();
	}

	public void start() {
		init();
		while (rewindAfterEnd) {
			System.out.println("Starting new run....");
			processLines();
		}

		close();

	}

	public void addListener(DataProvider provider) {
		synchronized (this.clients) {
			this.clients.add(provider);
		}
	}

	public void removeListener(DataProvider provider) {
		synchronized (this.clients) {
			this.clients.remove(provider);
		}
	}

	public synchronized void processLines() {
		long startTime = System.currentTimeMillis();
		String line;
		try {

			lineNr = 0;
			line = in.readLine();
			while (line != null) {
				lineNr++;
				List<String> parts = new ArrayList<String>();
				int position = 0;
				String token = "";
				while (position < line.length()) {

					char c = line.charAt(position);
					if (Character.isWhitespace(c)) {
						if (!token.isEmpty()) {
							parts.add(token);
						}
						token = "";
					} else {
						token = token + c;
					}
					position++;
				}
				parts.add(token);

				if (parts.size() > 4) {
					String adl = parts.get(4).trim();
					if (!adls.contains(adl)) {
						adls.add(adl);
					}
				}

				String timeString = parts.get(1);
				// ein fehler im datensatz, wenn nur sekunden angegeben sind...
				if (timeString.length() <= 8) {
					timeString = timeString + ".000000";
				}
				String dateTime = parts.get(0) + " " + timeString;
				dateTime = dateTime.substring(0, dateTime.length() - 3);
				// System.out.println(dateTime);
				SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
				sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
				Date d = sdf.parse(dateTime);
				long timeInMillis = d.getTime();

				String type = parts.get(2).substring(0, 1);
				String id = parts.get(2).substring(1, parts.get(2).length());
				String value = parts.get(3);
				Thread.sleep(delayMS);
				line = in.readLine();

				if (!this.serverList.containsKey(type)) {
					createServer(type);
				}
				DataTuple tuple = new DataTuple();
				tuple.addString(id);
				tuple.addString(value);
				tuple.addLong(timeInMillis);

				synchronized (this.clients) {
					for (DataProvider provider : this.clients) {
						if (provider.getStreamName().equals(type)) {
							provider.addTuple(tuple);
						}
					}
				}
				if (lineNr % 10000 == 0) {
					System.out.println("Line: " + lineNr + " Time in ms: " + (System.currentTimeMillis() - startTime));
				}

			}
			init();
		} catch (Exception e) {
			System.err.println("Fehler bei Zeile " + lineNr);
			e.printStackTrace();
		}
		// for (String a : adls) {
		// System.out.println(a);
		// }
		// System.exit(0);
	}

	public void init() {
		// URL fileURL =
		// Activator.getContext().getBundle().getEntry("/data/data_3days.csv");
		URL fileURL = Activator.getContext().getBundle().getEntry("/data/data");
		try {
			InputStream inputStream = fileURL.openConnection().getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
