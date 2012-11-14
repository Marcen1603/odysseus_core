/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.generator.classification;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.generator.StreamClientHandler;
import de.uniol.inf.is.odysseus.webservice.client.StringResponse;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServer;
import de.uniol.inf.is.odysseus.webservice.client.WebserviceServerService;

/**
 * @author Dennis Geesen
 * 
 */
public class BenchmarkController {

	private static BenchmarkController instance = null;

	public static synchronized BenchmarkController getInstance() {
		if (instance == null) {
			instance = new BenchmarkController();
		}
		return instance;
	}

	private int counter = -2;

	private int times = 5;
	private int timesCounter = 0;

	private String token;
	private WebserviceServer server;
	private boolean started = false;

	private Map<Integer, StreamClientHandler> handlers = new HashMap<>();
	private int pointer = 0;

	private int breakAfterTransactions = 20000000;

	private static String CLOSE_QUERY = "#PARSER CQL\n" + "#TRANSCFG StandardLatency\n" + "#DROPALLQUERIES\n" + "#QUERY\n" + "DROP STREAM frequent IF EXISTS\n";

	private List<Double> throughputs = new ArrayList<>();

	public void addHandler(int order, StreamClientHandler sch) {
		this.handlers.put(order, sch);
	}

	public void removeHandler(int order, StreamClientHandler sch) {
		this.handlers.remove(order);
	}

	public void start() {
		this.started = true;
		this.pointer = 0;

		System.out.println("Connecting to Odysseus...");
		WebserviceServerService wss = new WebserviceServerService();
		server = wss.getWebserviceServerPort();
		StringResponse resp = server.login("System", "manager");
		if (resp.isSuccessful()) {
			System.out.println("connected!");
			token = resp.getResponseValue();
			this.runquery();
		} else {
			System.out.println("connection failed!");
		}
	}

	private void next() {
		if (counter > 0) {
			timesCounter++;
			if (timesCounter > times) {
				close();
				return;
			}
		}
		runquery();
	}

	/**
	 * 
	 */
	private void close() {
		System.out.println("finished - all " + counter + " runs done!");
		System.out.print("Installing close-query...");
		server.addQuery(token, "OdysseusScript", CLOSE_QUERY, "StandardLatency");
		System.out.println(" done!");
		System.out.println("Following throughputs were measured: ");
		NumberFormat nf = NumberFormat.getNumberInstance();
		for (Double d : throughputs) {
			System.out.println(nf.format(d));
		}
		System.out.println("----------------");

	}

	private void runquery() {
		if (!this.started) {
			return;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		counter++;
		if (counter > 0) {
			// prefix = "";
		}

		System.out.println("******************************** Query " + counter + " *************************************************");
		System.out.println("Start new query");
		String query = loadQuery("C:" + File.separator + "Users" + File.separator + "dgeesen" + File.separator + "Dropbox" + File.separator + "Odysseus Projects" + File.separator
				+ "Mining" + File.separator + "Classification.qry");
		server.addQuery(token, "OdysseusScript", query, "StandardLatency");
		System.out.println("****************************************************************************************");

	}

	/**
	 * @param string
	 * @return
	 */
	private String loadQuery(String filename) {
		Path path = Paths.get(filename);
		String content = "";
		try {
			List<String> str = Files.readAllLines(path, StandardCharsets.UTF_8);
			for (String line : str) {
				content = content + System.lineSeparator() + line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public void instanceFinished(StreamClientHandler handler, double throughput) {
		if (!this.started) {
			return;
		}
		this.next();
		// add to current values:
		throughputs.add(throughput);

	}

	public int breakAfter() {
		return this.breakAfterTransactions;
	}

}
