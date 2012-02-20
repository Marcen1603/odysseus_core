package de.uniol.inf.is.odysseus.mining.smql;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.mining.smql.parser.SMQLParser;
import de.uniol.inf.is.odysseus.mining.smql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.mining.smql.visitor.StandardSMQLParserVisitor;
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public class SMQLParserService implements IQueryParser {

	private SMQLParser parser;
	private Map<String, ISMQLFeature> languageFeatures = new HashMap<String, ISMQLFeature>();

	@Override
	public String getLanguage() {
		return "StreamMiningQL";
	}

	@Override
	public List<ILogicalQuery> parse(String query, ISession user, IDataDictionary dd) throws QueryParseException {
		return parse(new StringReader(query), user, dd);
	}

	@Override
	public List<ILogicalQuery> parse(Reader reader, ISession user, IDataDictionary dd) throws QueryParseException {
		try {
			if (parser == null) {
				parser = new SMQLParser(reader);
			} else {
				parser.ReInit(reader);
			}
			SimpleNode statement = parser.Start();
			StandardSMQLParserVisitor walker = new StandardSMQLParserVisitor(user, dd, languageFeatures);
			walker.visit(statement, null);
			walker.print();
			
			List<ILogicalOperator> topOperators = walker.getTopOperators();
			List<ILogicalQuery> queries = new ArrayList<ILogicalQuery>();
			for(ILogicalOperator op: topOperators){
				Query query = new Query();
				query.setParserId(getLanguage());		
				query.setLogicalPlan(op, true);
				queries.add(query);
			}			
			return queries;
		} catch (NoClassDefFoundError e) {
			throw new QueryParseException("Parser error: missing plugin for language feature", e.getCause());
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}
	
	public void bindLanguageFeature(ISMQLFeature feature){
		if(!this.languageFeatures.containsKey(feature.getName())){
			this.languageFeatures.put(feature.getName(), feature);
		}else{
			System.err.println("WARN: you are trying to load the language feature "+feature.getName()+" twice!");
		}
	}
	
	public void unbindLanguageFeature(ISMQLFeature feature){
		this.languageFeatures.remove(feature.getName());
	}

}
