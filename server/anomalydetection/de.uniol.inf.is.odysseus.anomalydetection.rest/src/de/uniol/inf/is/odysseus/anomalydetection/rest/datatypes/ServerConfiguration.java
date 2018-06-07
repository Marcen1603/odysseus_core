package de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the server side of a configuration. The class holds the
 * information which is necessary to run a configuration on the server (Odysseus
 * side). E.g. the queries which need to be started.
 * 
 * @author Tobias Brandt
 *
 */
public class ServerConfiguration {
	
	// Sources are saved separately, cause they need to be executed first
	private List<QueryInformation> sources;
	private List<QueryInformation> sourcePaths;
	
	private List<QueryInformation> queries;
	private List<QueryInformation> queryPaths;
	
	public ServerConfiguration() {
		this.sources = new ArrayList<QueryInformation>();
		this.sourcePaths = new ArrayList<QueryInformation>();
		this.queries = new ArrayList<QueryInformation>();
		this.queryPaths = new ArrayList<QueryInformation>();
	}

	public List<QueryInformation> getSources() {
		return sources;
	}

	public List<QueryInformation> getSourcePaths() {
		return sourcePaths;
	}

	public List<QueryInformation> getQueries() {
		return queries;
	}

	public List<QueryInformation> getQueryPaths() {
		return queryPaths;
	}
	
	public void addSource(String source, String parser) {
		this.sources.add(new QueryInformation(source, parser));
	}
	
	public void addSourcePath(String path, String parser) {
		this.sourcePaths.add(new QueryInformation(path, parser));
	}
	public void addQuery(String query, String parser) {
		this.queries.add(new QueryInformation(query, parser));
	}
	public void addQueryPath(String path, String parser) {
		this.queryPaths.add(new QueryInformation(path, parser));
	}

}
