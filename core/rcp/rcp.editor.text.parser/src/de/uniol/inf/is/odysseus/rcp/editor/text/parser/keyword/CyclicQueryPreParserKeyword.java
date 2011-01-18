package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class CyclicQueryPreParserKeyword extends AbstractPreParserKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter) throws QueryTextParseException {
		try {
			IExecutor executor = ExecutorHandler.getExecutor();
			if( executor == null ) 
				throw new QueryTextParseException("No executor found");
			
			if( executor.getCompiler() == null ) 
				throw new QueryTextParseException("No compiler found");
			
			String parserID = (String)variables.get("PARSER");
			if( parserID == null ) 
				throw new QueryTextParseException("Parser not set");
			if( !executor.getSupportedQueryParsers().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = (String) variables.get("TRANSCFG");
			if( transCfg == null ) 
				throw new QueryTextParseException("TransformationConfiguration not set");
			if( executor.getQueryBuildConfiguration(transCfg) == null ) 
				throw new QueryTextParseException("TransformationConfiguration " + transCfg + " not bound");
			
		} catch( Exception ex ) {
			throw new QueryTextParseException("Unknown Exception during validation a cyclic query", ex);
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter ) throws QueryTextParseException {

		String queries = parameter;
		String parserID = (String) variables.get("PARSER");
		String transCfgID = (String) variables.get("TRANSCFG");

		IExecutor executor = ExecutorHandler.getExecutor();
		
		List<IQueryBuildSetting<?>> transCfg = executor.getQueryBuildConfiguration(transCfgID);
		User user = getCurrentUser(variables);
		try {
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			ICompiler compiler = executor.getCompiler();
			List<IQuery> plans = compiler.translateQuery(queries, parserID, user, dd);
			
			// HACK
			ParameterTransformationConfiguration cfg = null;
			for( IQueryBuildSetting<?> s : transCfg ) {
				if( s instanceof ParameterTransformationConfiguration ) {
					cfg = (ParameterTransformationConfiguration)s;
					break;
				}
			}
			
			if( cfg != null ) {
				// the last plan is the complete plan
				// so transform this one
				IQuery query = plans.get(plans.size() - 1);
				compiler.transform(query, cfg.getValue(), GlobalState.getActiveUser(), dd);
	
				IQuery addedQuery = executor.addQuery(query.getRoots(), user, transCfg.toArray(new IQueryBuildSetting[0]));
				executor.startQuery(addedQuery.getID(), GlobalState.getActiveUser());
			} 

		} catch (QueryParseException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch( Throwable ex ) {
//			ex.printStackTrace();
		}
		return null;
	}

}
