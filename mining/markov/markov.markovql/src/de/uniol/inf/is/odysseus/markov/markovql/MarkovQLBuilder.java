package de.uniol.inf.is.odysseus.markov.markovql;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.markov.markovql.parser.ASTQuery;
import de.uniol.inf.is.odysseus.markov.markovql.parser.MarkovQLParser;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.usermanagement.User;

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
	public void start(BundleContext bundleContext) throws Exception {
		MarkovQLBuilder.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		MarkovQLBuilder.context = null;
	}

	@Override
	public String getLanguage() {
		return "MarkovQL";
	}

	@Override
	public List<IQuery> parse(String query, User user, IDataDictionary dd) throws QueryParseException {	
		return parse(new StringReader(query),user, dd);
	}

	@Override
	public List<IQuery> parse(Reader reader, User user, IDataDictionary dd) throws QueryParseException {
		try {
			if (this.parser == null) {
				try {
					this.parser = new MarkovQLParser(reader);
				} catch (Error e) {
					MarkovQLParser.ReInit(reader);
				}
			} else {
				MarkovQLParser.ReInit(reader);
			}

			ASTQuery querie = MarkovQLParser.Query();
			
			MarkovQLVisitor mqlv = new MarkovQLVisitor(user, dd);
			mqlv.visit(querie, null);
			List<IQuery> queries = mqlv.getPlans();
			for (IQuery query : queries) {
				query.setParserId(getLanguage());
			}
			return queries;
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	

}
