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

package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.reloadlog;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * 
 * @author Dennis Geesen Created at: 17.08.2011
 */
public class ReloadLog {

	private static Logger logger = LoggerFactory.getLogger(ReloadLog.class);
	
	private List<QueryEntry> queries = new ArrayList<QueryEntry>();

	public ReloadLog() {		
	}

	public void queryAdded(String query, String buildConfig, String parserID, User user) {
		logger.debug("Query added to log: " + query);

		QueryEntry qe = new QueryEntry();
		qe.parserID =parserID;
		qe.query = query;
		// TODO: korrekte holen!
		qe.transCfgID = buildConfig;
		qe.username = user.getUsername();
		queries.add(qe);
	}

	public void reload() {
		logger.debug("Starting reloading from log...");
		// get clean copies
		List<QueryEntry> copiedQueries = getCopyForQueries(this.queries);
		clear();
		reloadQueries(copiedQueries);
		logger.debug("Reloading done.");
	}

	private List<QueryEntry> getCopyForQueries(List<QueryEntry> queries) {
		List<QueryEntry> copied = new ArrayList<QueryEntry>();
		for (QueryEntry c : queries) {
			copied.add(c.getCopy());
		}
		return copied;
	}

	private void clear() {
		logger.debug("Clearing old log file");

	}

	private void reloadQueries(List<QueryEntry> queries) {
		logger.debug("Reloading queries...");
		for(QueryEntry e : queries){
			logger.debug("Reload query with parser " + e.parserID + ": " + e.query);
			// executor.addQuery(query, user, dd, parameters);
		}
		logger.debug("Reloading queries done.");
	}

	class QueryEntry {
		public String parserID;
		public String transCfgID;
		public String query;
		public String username;

		public QueryEntry getCopy() {
			QueryEntry qe = new QueryEntry();
			qe.parserID = this.parserID;
			qe.transCfgID = this.transCfgID;
			qe.query = this.query;
			qe.username = this.username;
			return qe;
		}
	}

}
