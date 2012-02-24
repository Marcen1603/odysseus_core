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

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * 
 * @author Dennis Geesen Created at: 17.08.2011
 */
public class ReloadLog {

	private static Logger logger = LoggerFactory.getLogger(ReloadLog.class);
	public static final String LOG_FILENAME = System.getProperty("user.home") + "/odysseus/reloadlog.store";

	private List<QueryEntry> queries = new ArrayList<QueryEntry>();

	public ReloadLog() {
	}

	public void queryAdded(String query, String buildConfig, String parserID, ISession user) {
		logger.debug("Query added to log: " + query);

		QueryEntry qe = new QueryEntry();
		qe.parserID = parserID;
		qe.query = query;
		qe.transCfgID = buildConfig;
		qe.username = user.getUser().getName();
		queries.add(qe);
		saveState();
	}

	private void saveState() {
		FileWriter writer = null;
		try {
			logger.debug("Save queries in log.");
			writer = new FileWriter(LOG_FILENAME);			
			for (QueryEntry e : this.queries) {
				writer.write(e.toString());
			}
		} catch (Exception ex) {
			logger.error("Error while saving queries for reload", ex);
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception ex) {
				logger.error("Error while closing reload log file", ex);
			}
		}

	}

	public void removeQuery(String queryText) {
		logger.debug("Removing query from log: " + queryText);
		Iterator<QueryEntry> iterator = this.queries.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().query.equals(queryText)) {
				iterator.remove();
			}
		}		
		saveState();
	}
}
