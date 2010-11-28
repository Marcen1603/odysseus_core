package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.user.ActiveUser;
import de.uniol.inf.is.odysseus.rcp.viewer.query.QueryBuildConfigurationRegistry;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class CyclicQueryPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(Map<String, String> variables, String parameter) throws QueryTextParseException {
		try {
			IExecutor executor = ExecutorHandler.getExecutor();
			if( executor == null ) 
				throw new QueryTextParseException("No executor found");
			
			if( executor.getCompiler() == null ) 
				throw new QueryTextParseException("No compiler found");
			
			String parserID = variables.get("PARSER");
			if( parserID == null ) 
				throw new QueryTextParseException("Parser not set");
			if( !executor.getSupportedQueryParsers().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = variables.get("TRANSCFG");
			if( transCfg == null ) 
				throw new QueryTextParseException("TransformationConfiguration not set");
			if( QueryBuildConfigurationRegistry.getInstance().getQueryBuildConfiguration(transCfg) == null ) 
				throw new QueryTextParseException("TransformationConfiguration " + transCfg + " not found");
			
		} catch( Exception ex ) {
			throw new QueryTextParseException("Unknown Exception during validation a cyclic query", ex);
		}
	}

	@Override
	public void execute(Map<String, String> variables, String parameter ) throws QueryTextParseException {

		String queries = parameter;
		String parserID = variables.get("PARSER");
		String transCfgID = variables.get("TRANSCFG");

		IExecutor executor = ExecutorHandler.getExecutor();
		
		List<IQueryBuildSetting<?>> transCfg = QueryBuildConfigurationRegistry.getInstance().getQueryBuildConfiguration(transCfgID);
		User user = ActiveUser.getActiveUser();
		try {
			ICompiler compiler = executor.getCompiler();
			List<IQuery> plans = compiler.translateQuery(queries, parserID, user);
			
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
				compiler.transform(query, cfg.getValue(), ActiveUser.getActiveUser());
	
				int queryID = executor.addQuery(query.getRoots(), user, transCfg.toArray(new IQueryBuildSetting[0]));
				executor.startQuery(queryID, ActiveUser.getActiveUser());
			} 

		} catch (QueryParseException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch( Throwable ex ) {
//			ex.printStackTrace();
		}
	}

}
