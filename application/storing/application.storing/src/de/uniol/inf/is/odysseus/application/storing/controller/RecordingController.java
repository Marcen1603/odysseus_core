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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.application.storing.RecordingException;
import de.uniol.inf.is.odysseus.application.storing.controller.RecordEntry.PlayingState;
import de.uniol.inf.is.odysseus.application.storing.controller.RecordEntry.State;
import de.uniol.inf.is.odysseus.application.storing.model.RecordingStore;
import de.uniol.inf.is.odysseus.database.connection.DatabaseConnectionDictionary;
import de.uniol.inf.is.odysseus.database.connection.IDatabaseConnection;
import de.uniol.inf.is.odysseus.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.usermanagement.ISession;


/**
 * 
 * @author Dennis Geesen Created at: 09.11.2011
 */
public class RecordingController {

	private Map<String, RecordEntry> recordings = new HashMap<String, RecordEntry>();

	private List<IRecordingListener> listener = new ArrayList<IRecordingListener>();

	private static RecordingController instance = null;

	private RecordingStore recordingStore;

	private RecordingController() {
		try {
			this.recordingStore = new RecordingStore();
			this.recordings.putAll(this.recordingStore.getMap());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized RecordingController getInstance() {
		if (instance == null) {
			instance = new RecordingController();
		}
		return instance;
	}

	public void startRecording(String recordingName) {
		RecordEntry record = recordings.get(recordingName);
		ISession user = OdysseusRCPPlugIn.getActiveSession();
		if (!record.isPaused()) {
			try {
				deployQueries(record);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		for (IQuery q : record.getStreamToQueries()) {
			try {
				OdysseusRCPPlugIn.getExecutor().startQuery(q.getID(), user);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		record.setState(State.Started);
		fireChangedEvent();

	}

	public void pauseRecording(String recordingName) {
		RecordEntry record = this.recordings.get(recordingName);
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		for (IQuery q : record.getStreamToQueries()) {
			try {
				OdysseusRCPPlugIn.getExecutor().stopQuery(q.getID(), caller);
			} catch (PlanManagementException e) {
				e.printStackTrace();
			}
		}
		record.setState(State.Paused);
		fireChangedEvent();
	}

	public void stopRecording(String name) {
		RecordEntry record = recordings.get(name);
		ISession user = OdysseusRCPPlugIn.getActiveSession();
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
		OdysseusRCPPlugIn.getExecutor().removeSink(record.getSinkName(), user);

		record.setState(State.Stopped);
		fireChangedEvent();
	}

	private void deployQueries(RecordEntry record) throws PlanManagementException {
		String sinkName = record.getSinkName();
		String createSink = "CREATE SINK " + sinkName + " AS DATABASE " + record.getDatabaseConnection() + " TABLE " + record.getTableName() + " AND DROP";
		String createStreamTo = "STREAM TO " + sinkName + " SELECT * FROM " + record.getFromStream();
		ISession user = OdysseusRCPPlugIn.getActiveSession();
		Collection<IQuery> sinkQueries = OdysseusRCPPlugIn.getExecutor().addQuery(createSink, "CQL", user, "Standard");
		Collection<IQuery> streamToQueries = OdysseusRCPPlugIn.getExecutor().addQuery(createStreamTo, "CQL", user, "Standard");
		record.clearQueries();
		record.setSinkQueries(sinkQueries);
		record.setStreamToQueries(streamToQueries);
	}
	
	public void dropRecording(String name, boolean dropTable) throws RecordingException{		
		RecordEntry record = recordings.get(name);
		if(!record.isStopped()){
			stopRecording(name);
		}
		if(!record.isPlayingStopped()){
			stopPlaying(name);
		}
		if(dropTable){			
			IDatabaseConnection connection = DatabaseConnectionDictionary.getInstance().getDatabaseConnection(record.getDatabaseConnection());
			if(connection != null){
				if(connection.tableExists(record.getTableName())){
					connection.dropTable(record.getTableName());
				}
			}else{
				throw new RecordingException("There is no connection named \""+record.getDatabaseConnection()+"\"\nCreate the connection with that name before!");
			}
		}
		recordings.remove(name);
		fireChangedEvent();
	}
	
	@SuppressWarnings("unused")
	private void deployPlayingQueries(RecordEntry record) throws PlanManagementException{
		
		
	}

	public void createRecording(String recordingName, String databaseConnection, String tableName, String fromStream) {
		RecordEntry record = new RecordEntry(recordingName, databaseConnection, tableName, fromStream);
		this.recordings.put(record.getName(), record);
		record.setState(State.Initialized);
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

	public void pausePlaying(String name) {
		RecordEntry record = recordings.get(name);

		record.setPlayingState(PlayingState.Paused);
		fireChangedEvent();
	}

	public void stopPlaying(String name) {
		RecordEntry record = recordings.get(name);

		record.setPlayingState(PlayingState.Stopped);
		fireChangedEvent();
	}

	public void startPlaying(String name) {
		RecordEntry record = recordings.get(name);

		record.setPlayingState(PlayingState.Started);
		fireChangedEvent();
	}

	public void dispose() {
		// stop all
		for (Entry<String, RecordEntry> e : this.recordings.entrySet()) {
			if (e.getValue().isStarted() || e.getValue().isPaused()) {
				e.getValue().setState(State.Stopped);
			} else {
				if (e.getValue().isPlayingStarted() || e.getValue().isPlayingPaused()) {
					e.getValue().setPlayingState(PlayingState.Stopped);
				}
			}
			e.getValue().clearQueries();
		}

		// save all
		this.recordingStore.clear();
		for (RecordEntry record : this.recordings.values()) {
			this.recordingStore.storeRecordEntry(record);
		}

	}
}
