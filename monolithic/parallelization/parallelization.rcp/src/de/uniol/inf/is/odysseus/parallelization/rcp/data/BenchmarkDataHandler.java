/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

/**
 * base data handler for parallelization benchmarker, this class provides
 * multiple instances which can be identified via UUID
 * 
 * @author ChrisToenjesDeye
 *
 */
public class BenchmarkDataHandler {

	private static Map<UUID, BenchmarkDataHandler> instances = new HashMap<UUID, BenchmarkDataHandler>();
	private UUID uniqueIdentifier;

	private IEditorPart part;
	private ISelection selection;

	// query data
	private IFile queryFile;
	private List<String> queryStringArray = new ArrayList<String>();
	private List<ILogicalQuery> logicalQueries = new ArrayList<ILogicalQuery>();

	// Initialization data
	private BenchmarkInitializationResult benchmarkInitializationResult;

	// Analysis data
	private BenchmarkerConfiguration configuration;

	public BenchmarkDataHandler() {
		uniqueIdentifier = UUID.randomUUID();
	}

	/**
	 * creates a new instance of the benchmarker data handler
	 * 
	 * @return
	 */
	public static BenchmarkDataHandler getNewInstance() {
		BenchmarkDataHandler instance = new BenchmarkDataHandler();
		instances.put(instance.getUniqueIdentifier(), instance);
		return instance;
	}

	/**
	 * returns an existing instance of this data handler for a given uuid as
	 * string
	 * 
	 * @param uniqueIdentifier
	 * @return
	 */
	public static BenchmarkDataHandler getExistingInstance(
			String uniqueIdentifier) {
		return instances.get(UUID.fromString(uniqueIdentifier));
	}

	/**
	 * returns an existing instance of this data handler for a given uuid
	 * 
	 * @param uniqueIdentifier
	 * @return
	 */
	public static BenchmarkDataHandler getExistingInstance(UUID uniqueIdentifier) {
		return instances.get(uniqueIdentifier);
	}

	/**
	 * removes an existing instance of this data handler for a given uuid
	 * 
	 * @param processUid
	 */
	public static void removeInstance(UUID processUid) {
		if (instances.containsKey(processUid)) {
			instances.remove(processUid);
		}
	}

	/**
	 * removes an existing instance of this data handler for a given uuid as
	 * string
	 * 
	 * @param processUid
	 */
	public static void removeInstance(String processUid) {
		removeInstance(UUID.fromString(processUid));
	}

	public UUID getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public IFile getQueryFile() {
		return queryFile;
	}

	public void setQueryFile(IFile queryFile) {
		this.queryFile = queryFile;
	}

	public ISelection getSelection() {
		return selection;
	}

	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	public IEditorPart getPart() {
		return part;
	}

	public void setPart(IEditorPart part) {
		this.part = part;
	}

	public void addQueryString(String queryString) {
		this.queryStringArray.add(queryString);
	}

	public List<String> getQueryStringArray() {
		return queryStringArray;
	}

	public BenchmarkInitializationResult getBenchmarkInitializationResult() {
		return benchmarkInitializationResult;
	}

	public void setBenchmarkInitializationResult(
			BenchmarkInitializationResult benchmarkInitializationResult) {
		this.benchmarkInitializationResult = benchmarkInitializationResult;
	}

	public BenchmarkerConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(BenchmarkerConfiguration configuration) {
		this.configuration = configuration;
	}

	public List<ILogicalQuery> getLogicalQueries() {
		return logicalQueries;
	}

	public void setLogicalQueries(List<ILogicalQuery> logicalQueries) {
		this.logicalQueries = logicalQueries;
	}

	public void addLogicalQuery(ILogicalQuery logicalQuery) {
		this.logicalQueries.add(logicalQuery);
	}
}
