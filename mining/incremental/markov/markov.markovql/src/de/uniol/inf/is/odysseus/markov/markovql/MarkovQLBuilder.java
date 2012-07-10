/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.markov.markovql;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTQuery;
import de.uniol.inf.is.odysseus.markov.markovql.parser.MarkovQLParser;

public class MarkovQLBuilder implements BundleActivator, IQueryParser {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}
	private MarkovQLParser parser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		MarkovQLBuilder.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		MarkovQLBuilder.context = null;
	}

	@Override
	public String getLanguage() {
		return "MarkovQL";
	}

	@Override
	public List<ILogicalQuery> parse(String query, ISession user, IDataDictionary dd) throws QueryParseException {	
		return parse(new StringReader(query),user, dd);
	}

	@Override
	public List<ILogicalQuery> parse(Reader reader, ISession user, IDataDictionary dd) throws QueryParseException {
		try {
			if (this.parser == null) {
				try {
					this.parser = new MarkovQLParser(reader);
				} catch (Error e) {
					parser.ReInit(reader);
				}
			} else {
				parser.ReInit(reader);
			}

			ASTQuery querie = parser.Query();
			
			MarkovQLVisitor mqlv = new MarkovQLVisitor(user, dd);
			mqlv.visit(querie, null);
			List<ILogicalQuery> queries = mqlv.getPlans();
			for (ILogicalQuery query : queries) {
				query.setParserId(getLanguage());
			}
			return queries;
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	

}
