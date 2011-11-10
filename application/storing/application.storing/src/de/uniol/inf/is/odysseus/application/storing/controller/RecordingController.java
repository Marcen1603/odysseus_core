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

package de.uniol.inf.is.odysseus.application.storing.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.application.storing.Activator;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * 
 * @author Dennis Geesen Created at: 09.11.2011
 */
public class RecordingController {

	private Map<String, RecordEntry> recordings = new HashMap<String, RecordEntry>();

	private List<IRecordingListener> listener = new ArrayList<IRecordingListener>();

	private static RecordingController instance = null;

	private RecordingController() {

	}

	public static synchronized RecordingController getInstance() {
		if (instance == null) {
			instance = new RecordingController();
		}
		return instance;
	}

	public void startRecording(String recordingName) {
		RecordEntry record = recordings.get(recordingName);
		User user = GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN);
		for (IQuery q : record.getStreamToQueries()) {
			try {
				OdysseusRCPPlugIn.getExecutor().startQuery(q.getID(), user);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		fireChangedEvent();

	}
	
	public void pauseRecording(String recordingName) {
		RecordEntry record = this.recordings.get(recordingName);
		User caller = GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN);
		for (IQuery q : record.getStreamToQueries()) {
			try {
				OdysseusRCPPlugIn.getExecutor().stopQuery(q.getID(), caller);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		fireChangedEvent();
	}

	

	public void stopRecording(String name) {
		RecordEntry record = recordings.get(name);
		User user = GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN);
		for (IQuery q : record.getStreamToQueries()) {
			try {
				OdysseusRCPPlugIn.getExecutor().removeQuery(q.getID(), user);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		for (IQuery q : record.getSinkQueries()) {
			try {
				OdysseusRCPPlugIn.getExecutor().removeQuery(q.getID(), user);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		IDataDictionary dd = GlobalState.getActiveDatadictionary();
		dd.removeSink(record.getSinkName());
		fireChangedEvent();
	}


	public void createRecording(String recordingName, String databaseConnection, String tableName, String fromStream) {
		RecordEntry record = new RecordEntry(recordingName, databaseConnection, tableName, fromStream);
		String sinkName = record.getSinkName();		
		String createSink = "CREATE SINK " + sinkName + " TO DATABASE " + record.getDatabaseConnection() + " TABLE " + record.getTableName() + " AND DROP";
		String createStreamTo = "STREAM TO " + sinkName + " SELECT * FROM " + record.getFromStream();
		try {
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			User user = GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN);
			Collection<IQuery> sinkQueries = OdysseusRCPPlugIn.getExecutor().addQuery(createSink, "CQL", user, dd, "Standard");
			Collection<IQuery> streamToQueries = OdysseusRCPPlugIn.getExecutor().addQuery(createStreamTo, "CQL", user, dd, "Standard");
			this.recordings.put(record.getName(), record);
			record.setSinkQueries(sinkQueries);
			record.setStreamToQueries(streamToQueries);
		} catch (PlanManagementException e) {
			e.printStackTrace();
		}
		fireChangedEvent();
	}
	
	
	private void fireChangedEvent() {
		for (IRecordingListener listener : this.listener) {
			listener.recordingChanged();
		}
	}

	

	public void addListener(IRecordingListener listener) {
		this.listener.add(listener);
	}

	public void removeListener(IRecordingListener listener) {
		this.listener.remove(listener);
	}
	

	public Map<String, RecordEntry> getRecords() {
		return this.recordings;
	}
}
