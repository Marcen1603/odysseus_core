package de.uniol.inf.is.odysseus.parallelization.rcp.data;

import java.util.HashMap;
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
	private String queryString; // contains no parallelization keywords
	private ILogicalQuery logicalQuery;
	
	// Initialization data
	private BenchmarkInitializationResult benchmarkInitializationResult;
	
	
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


	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	
	public String getQueryString(){
		return queryString;
	}


	public BenchmarkInitializationResult getBenchmarkInitializationResult() {
		return benchmarkInitializationResult;
	}


	public void setBenchmarkInitializationResult(
			BenchmarkInitializationResult benchmarkInitializationResult) {
		this.benchmarkInitializationResult = benchmarkInitializationResult;
	}
}
