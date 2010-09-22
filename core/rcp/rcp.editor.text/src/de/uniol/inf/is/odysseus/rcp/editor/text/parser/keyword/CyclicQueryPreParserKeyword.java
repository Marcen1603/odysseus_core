package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.user.ActiveUser;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

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
			if( !executor.getSupportedQueryParser().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = variables.get("TRANSCFG");
			if( transCfg == null ) 
				throw new QueryTextParseException("TransformationConfiguration not set");
			if( ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfiguration(transCfg) == null ) 
				throw new QueryTextParseException("TransformationConfiguration " + transCfg + " not found");
			
		} catch( Exception ex ) {
			throw new QueryTextParseException("Unknown Exception during validation a cyclic query", ex);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Map<String, String> variables, String parameter ) throws QueryTextParseException {

		String queries = parameter;
		String parserID = variables.get("PARSER");
		String transCfgID = variables.get("TRANSCFG");

		IExecutor executor = ExecutorHandler.getExecutor();
		ICompiler compiler = executor.getCompiler();
		ParameterTransformationConfiguration transCfg = ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfiguration(transCfgID);
		User user = ActiveUser.getActiveUser();
		try {
			List<IQuery> plans = compiler.translateQuery(queries, parserID, user);

			// DEBUG: Print the logical plan.
			PrintGraphVisitor<ILogicalOperator> pv = new PrintGraphVisitor<ILogicalOperator>();
			AbstractGraphWalker walker = new AbstractGraphWalker();
			for (IQuery plan : plans) {
				System.out.println("PRINT PARTIAL PLAN: ");
				walker.prefixWalk(plan.getLogicalPlan(), pv);
				System.out.println(pv.getResult());
				pv.clear();
				walker.clearVisited();
				System.out.println("PRINT END.");
			}

			// DEBUG:
			System.out.println("ExecutorConsole: trafoConfigHelper: " + transCfg.getValue().getTransformationHelper());

			// the last plan is the complete plan
			// so transform this one
			List<IPhysicalOperator> physPlan = compiler.transform(plans.get(plans.size() - 1).getLogicalPlan(), transCfg.getValue());

			int queryID = executor.addQuery(physPlan, user, transCfg);
			executor.startQuery(queryID);

		} catch (QueryParseException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch( Throwable ex ) {
//			ex.printStackTrace();
		}
	}

}
