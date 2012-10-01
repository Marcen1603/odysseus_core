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
package de.uniol.inf.is.odysseus.generator.frequentitem;

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

	private int counter = 0;
	
	private int startSupport = 2;
	private int startTransactions = 25;
	private int startSelectivity = 0;
	private int maxSupport = 15;
	private int maxTransactions = 150;
	private int maxSelectivity = 100;

	private int currentSupport;
	private int currentTransaction;
	private int currentSelectivity;
	private String token;
	private WebserviceServer server;
	private boolean started = false;

	public void start() {
		this.started = true;
		this.currentSupport = this.startSupport;
		this.currentTransaction = this.startTransactions;
		this.currentSelectivity = this.startSelectivity;

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
		currentSupport = currentSupport + 1;
		if (currentSupport > maxSupport) {
			currentTransaction = currentTransaction + 25;
			if (currentTransaction > maxTransactions) {
				currentSelectivity = currentSelectivity + 25;
				if (currentSelectivity > maxSelectivity) {
					System.out.println("finished - all done!");
					return;
				}
				this.currentTransaction = this.startTransactions;
			}
			this.currentSupport = startSupport;

		}
		runquery();
	}

	private void runquery() {
		if (!this.started) {
			return;
		}
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		System.out.println("********************************Query "+counter+" *************************************************");
		counter++;				
		System.out.println("Start new with: Support s=" + currentSupport + " \t Transactions w=" + currentTransaction+ " \t Selectivity g=" + currentSelectivity);
		String query = "#PARSER CQL \n" + "#TRANSCFG StandardLatency \n" + "#DEFINE support "
				+ currentSupport
				+ " \n"
				+ "#DEFINE transactions "
				+ currentTransaction
				+ " \n"
				+ "#DEFINE selectivity "
				+ currentSelectivity
				+ " \n"
				+ "#DROPALLQUERIES \n"
				+ "#QUERY \n"
				+ "DROP STREAM frequent IF EXISTS  \n"
				+ "#QUERY \n"
				+ "CREATE STREAM frequent (timestamp STARTTIMESTAMP, transaction INTEGER, item STRING) CHANNEL localhost : 54322; \n"
				+ "#PARSER PQL \n"
				+ "#RUNQUERY \n"
				+ "fis = FREQUENTITEMSET({algorithm='fpgrowth', support=${support}, transactions=${transactions}}, \n"
				+ " RENAME({ALIASES=['item']}, \n"
				+ " MAP({EXPRESSIONS=['toLong(item)']}, \n"
				+ "PROJECT({ATTRIBUTES=['item']}, \n"
				+ "WINDOW({size = 50, advance = 1, type = 'time'}, \n"
				+ "ACCESS({source = 'frequent'}) \n"
				+ ") \n"
				+ ") \n"
				+ ") \n"
				+ ") \n"
				+ ") \n"
				+ "selected = SELECT({PREDICATE=ForAllPredicate('set, item <= (${selectivity}*10)')},fis) \n"
				+ "stats = CALCLATENCY(COUNTSTATS({outputeach=100}, selected)) \n"
				+ "fs3 = FILESINK({file='E:/Results/unoptimized-"+counter+"- w${transactions} - sel ${selectivity}/dump-unoptimized-S${support}-W${transactions}-SEL${selectivity}.csv', filetype='csv', dumpMetaData='true', cachesize=100}, stats)";
		server.addQuery(token, "OdysseusScript", query, "StandardLatency");
		System.out.println("****************************************************************************************");

	}

	public void instanceFinished() {
		if (!this.started) {
			return;
		}
		this.next();
	}
}
