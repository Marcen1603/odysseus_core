package de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;
import de.uniol.inf.is.odysseus.rcp.editor.text.activator.ExecutorHandler;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.IPreParserKeyword;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParseException;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.QueryTextParser;
import de.uniol.inf.is.odysseus.rcp.viewer.query.ParameterTransformationConfigurationRegistry;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

public class CyclicQueryPreParserKeyword implements IPreParserKeyword {

	@Override
	public void validate(QueryTextParser parser, String parameter) throws QueryTextParseException {
		try {
			IAdvancedExecutor executor = ExecutorHandler.getExecutor();
			if( executor == null ) 
				throw new QueryTextParseException("No executor found");
			
			if( executor.getCompiler() == null ) 
				throw new QueryTextParseException("No compiler found");
			
			String parserID = parser.getVariable("PARSER");
			if( parserID == null ) 
				throw new QueryTextParseException("Parser not set");
			if( !executor.getSupportedQueryParser().contains(parserID))
				throw new QueryTextParseException("Parser " + parserID + " not found");
			String transCfg = parser.getVariable("TRANSCFG");
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
	public void execute(QueryTextParser parser, String parameter, User user) throws QueryTextParseException {

		String queries = parameter;
		String parserID = parser.getVariable("PARSER");
		String transCfgID = parser.getVariable("TRANSCFG");

		IAdvancedExecutor executor = ExecutorHandler.getExecutor();
		ICompiler compiler = executor.getCompiler();
		ParameterTransformationConfiguration transCfg = ParameterTransformationConfigurationRegistry.getInstance().getTransformationConfiguration(transCfgID);
		try {
			List<IQuery> plans = compiler.translateQuery(queries, parserID);

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
		}
	}

}
