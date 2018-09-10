/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.turjumaan.server.rdf;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelChangedListener;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * The listener interface for receiving rdf events. The class that is interested
 * in processing a rdf event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addRdfListener<code> method. When the rdf event occurs, that object's
 * appropriate method is invoked.
 *
 * @see RdfEvent
 */
public class RdfListener implements ModelChangedListener {

	/** The Constant maxLogNr. */
	private static final int maxLogNr = 3;

	/** The log. */
	private final Logger log;

	/**
	 * Instantiates a new RDF listener.
	 *
	 * @param log
	 *            the log
	 */
	public RdfListener(Logger log) {
		this.log = log;
	}

	/**
	 * Instantiates a new RDF listener.
	 */
	public RdfListener() {
		this(LoggerFactory.getLogger(RdfListener.class));
	}

	@Override
	public void addedStatement(Statement s) {
		log.info("Added statement => {}", s);
	}

	@Override
	public void addedStatements(List<Statement> s) {
		for (int i = 0; i < s.size() && i < maxLogNr; i++)
			addedStatement(s.get(i));
		if (s.size() > maxLogNr)
			log.info("Added {} more statements...", s.size() - maxLogNr);
	}

	@Override
	public void addedStatements(Statement[] s) {
		addedStatements(Arrays.asList(s));
	}

	@Override
	public void addedStatements(StmtIterator s) {
		addedStatements(s.toList());
	}

	@Override
	public void addedStatements(Model s) {
		addedStatements(s.listStatements());
	}

	@Override
	public void removedStatement(Statement s) {
		log.info("Removed statement => {}", s);
	}

	@Override
	public void removedStatements(List<Statement> s) {
		for (int i = 0; i < s.size() && i < maxLogNr; i++)
			removedStatement(s.get(i));
		if (s.size() > maxLogNr)
			log.info("Removed {} more statements...", s.size() - maxLogNr);
	}

	@Override
	public void removedStatements(Statement[] s) {
		removedStatements(Arrays.asList(s));
	}

	@Override
	public void removedStatements(StmtIterator s) {
		removedStatements(s.toList());
	}

	@Override
	public void removedStatements(Model s) {
		removedStatements(s.listStatements());
	}

	@Override
	public void notifyEvent(Model m, Object e) {
		log.info("notifyEvent => {} {}", e, m);
	}
}