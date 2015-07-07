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

public class BenchmarkDataHandler {

	
	private static Map<UUID, BenchmarkDataHandler> instances = new HashMap<UUID, BenchmarkDataHandler>();
	private UUID uniqueIdentifier;
	
	private IEditorPart part;
	private ISelection selection;

	// query data
	private IFile queryFile;
	private List<String> queryStringArray = new ArrayList<String>();
	private ILogicalQuery logicalQuery;
	
	// Initialization data
	private BenchmarkInitializationResult benchmarkInitializationResult;
	
	// Analysis data
	private BenchmarkerConfiguration configuration;
	
	
	public BenchmarkDataHandler(){
		uniqueIdentifier = UUID.randomUUID();
	}
	
	
	public static BenchmarkDataHandler getNewInstance(){
		BenchmarkDataHandler instance = new BenchmarkDataHandler();
		instances.put(instance.getUniqueIdentifier(), instance);
		return instance;
	}

	public static BenchmarkDataHandler getExistingInstance(String uniqueIdentifier){
		return instances.get(UUID.fromString(uniqueIdentifier));
	}
	
	public static BenchmarkDataHandler getExistingInstance(UUID uniqueIdentifier){
		return instances.get(uniqueIdentifier);
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


	public ILogicalQuery getLogicalQuery() {
		return logicalQuery;
	}


	public void setLogicalQuery(ILogicalQuery logicalQuery) {
		this.logicalQuery = logicalQuery;
	}


	public void addQueryString(String queryString) {
		this.queryStringArray.add(queryString);
	}
	
	public List<String> getQueryStringArray(){
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


	public static void removeInstance(UUID processUid) {
		if (instances.containsKey(processUid)){
			instances.remove(processUid);
		}
	}
	
	public static void removeInstance(String processUid) {
		removeInstance(UUID.fromString(processUid));
	}
}