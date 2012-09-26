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

import de.uniol.inf.is.odysseus.generator.frequentitem.webservice.StringResponse;
import de.uniol.inf.is.odysseus.generator.frequentitem.webservice.WebserviceServer;
import de.uniol.inf.is.odysseus.generator.frequentitem.webservice.WebserviceServerService;

/**
 * @author Dennis Geesen
 *
 */
public class BenchmarkController {

	private static BenchmarkController instance = null;
	
	public static synchronized BenchmarkController getInstance(){
		if(instance == null){
			instance = new BenchmarkController(); 
		}
		return instance;
	}
	
	private int startSupport = 9;
    private int startTransactions = 5;		
	private int maxSupport = 25;
	private int maxTransactions = 150;
	
	private int currentSupport;
	private int currentTransaction;
	private String token;
	private WebserviceServer server;
	private boolean started = false;
	
	public void start(){
		this.started  = true;
		this.currentSupport = this.startSupport;
		this.currentTransaction = this.startTransactions;
		System.out.println("Connecting to Odysseus...");
		WebserviceServerService wss = new WebserviceServerService();
		server = wss.getWebserviceServerPort();
		StringResponse resp = server.login("System", "manager");
		if(resp.isSuccessful()){
			System.out.println("connected!");
			token = resp.getResponseValue();
			this.runquery();
		}else{
			System.out.println("connection failed!");
		}		
	}
	
	
	private void next(){
		currentSupport = currentSupport +1;
		
		
		if(currentSupport>maxSupport){
			currentTransaction = currentTransaction+25;
			if(currentTransaction>maxTransactions){
				System.out.println("finished - all done!");
				return;
			}
			this.currentSupport = startSupport;
			
		}
		runquery();		
	}
	
	private void runquery(){
		if(!this.started){
			return;
		}
		System.out.println("****************************************************************************************");
		System.out.println("Start new with: Support s="+currentSupport+" \t Transactions w="+currentTransaction);
		String query = "#PARSER CQL \n" +
				"#TRANSCFG StandardLatency \n" +
				"#DEFINE support "+currentSupport+" \n"+
				"#DEFINE transactions "+currentTransaction+" \n"+						
				"#DROPALLQUERIES \n" +
				"#QUERY \n" +
				"DROP STREAM frequent IF EXISTS  \n" +
				"#QUERY \n" +
				"CREATE STREAM frequent (timestamp STARTTIMESTAMP, transaction INTEGER, item STRING) CHANNEL localhost : 54322; \n" +
				"#PARSER PQL \n" +
				"#RUNQUERY \n" +
				"fis = FREQUENTITEMSET({algorithm='fpgrowth', support=${support}, transactions=${transactions}}, \n" + 
				" RENAME({ALIASES=['item']}, \n" +
				" MAP({EXPRESSIONS=['toLong(item)']}, \n" +
				"PROJECT({ATTRIBUTES=['item']}, \n" +
				"WINDOW({size = 50, advance = 1, type = 'time'}, \n" + 
				"ACCESS({source = 'frequent'}) \n" +
				") \n" +
				") \n" +
				") \n" +
				") \n" +
				") \n" +
				"stats = CALCLATENCY(1:fis)";
		server.addQuery(token, "OdysseusScript", query, "StandardLatency");
		
	}
	
	
	public void instanceFinished(){		
		if(!this.started){
			return;
		}
		this.next();
	}
}
