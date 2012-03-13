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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.application.storing.Activator;

/**
 * 
 * @author Dennis Geesen Created at: 10.11.2011
 */
public class RecordEntry implements Serializable{

	private static final long serialVersionUID = -5888570174307029553L;
	private String name;
	private String databaseConnection;
	private String tableName;
	private String fromStream;
	private Collection<Integer> streamToQueries = new ArrayList<Integer>();
	private Collection<Integer> sinkQueries = new ArrayList<Integer>();
	private State state = State.Initialized;
	private PlayingState playingState = PlayingState.Stopped;

	public enum State {
		Initialized, Started, Stopped, Paused
	}
	
	public enum PlayingState{
		Started, Paused, Stopped
	}

	public RecordEntry(String name, String databaseConnection, String tableName, String fromStream) {
		super();
		this.name = name;
		this.databaseConnection = databaseConnection;
		this.tableName = tableName;
		this.fromStream = fromStream;
	}

	public String getFromStream() {
		return fromStream;
	}

	public void setFromStream(String fromStream) {
		this.fromStream = fromStream;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(String databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setStreamToQueries(Collection<Integer> streamToQueries) {
		this.streamToQueries = streamToQueries;
	}

	public void setSinkQueries(Collection<Integer> sinkQueries) {
		this.sinkQueries = sinkQueries;

	}

	public Collection<Integer> getSinkQueries() {
		return sinkQueries;
	}

	public Collection<Integer> getStreamToQueries() {
		return streamToQueries;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public String getSinkName() {
		return Activator.SINK_NAME_PREFIX + "_" + getDatabaseConnection() + "_" + getTableName();
	}

	public boolean isInitialized(){
		return (this.state==State.Initialized);
	}
	
	public boolean isStarted() {
		return (this.state==State.Started);
	}

	public boolean isStopped() {
		return (this.state==State.Stopped);
	}

	public boolean isPaused() {
		return (this.state==State.Paused);
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
	
	public void clearQueries(){
		this.sinkQueries.clear();
		this.streamToQueries.clear();
	}
	
	public boolean isPlayingStopped(){
		return (this.playingState == PlayingState.Stopped);
	}
	
	public boolean isPlayingStarted(){
		return (this.playingState == PlayingState.Started);
	}
	
	public boolean isPlayingPaused(){
		return (this.playingState == PlayingState.Paused);
	}

	public PlayingState getPlayingState() {
		return playingState;
	}

	public void setPlayingState(PlayingState playingState) {
		this.playingState = playingState;
	}
	
	
	
}
