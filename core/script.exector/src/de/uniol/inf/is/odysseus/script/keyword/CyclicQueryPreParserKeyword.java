/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.script.keyword;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.planmanagement.query.querybuiltparameter.ParameterTransformationConfiguration;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParseException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

public class CyclicQueryPreParserKeyword extends AbstractPreParserExecutorKeyword {

	@Override
	public void validate(Map<String, Object> variables, String parameter, User caller) throws OdysseusScriptParseException {
		try {
			IExecutor executor = getExecutor();
			if( executor == null ) 
				throw new OdysseusScriptParseException("No executor found");
			
			if( executor.getCompiler() == null ) 
				throw new OdysseusScriptParseException("No compiler found");
			
			String parserID = (String)variables.get("PARSER");
			if( parserID == null ) 
				throw new OdysseusScriptParseException("Parser not set");
			if( !executor.getSupportedQueryParsers().contains(parserID))
				throw new OdysseusScriptParseException("Parser " + parserID + " not found");
			String transCfg = (String) variables.get("TRANSCFG");
			if( transCfg == null ) 
				throw new OdysseusScriptParseException("TransformationConfiguration not set");
			if( executor.getQueryBuildConfiguration(transCfg) == null ) 
				throw new OdysseusScriptParseException("TransformationConfiguration " + transCfg + " not bound");
			
		} catch( Exception ex ) {
			throw new OdysseusScriptParseException("Unknown Exception during validation a cyclic query", ex);
		}
	}

	@Override
	public Object execute(Map<String, Object> variables, String parameter, User caller) throws OdysseusScriptParseException {

		String queries = parameter;
		String parserID = (String) variables.get("PARSER");
		String transCfgID = (String) variables.get("TRANSCFG");

		IExecutor executor = getExecutor();
		
		List<IQueryBuildSetting<?>> transCfg = executor.getQueryBuildConfiguration(transCfgID).getConfiguration();
		try {
			IDataDictionary dd = GlobalState.getActiveDatadictionary();
			ICompiler compiler = executor.getCompiler();
			List<IQuery> plans = compiler.translateQuery(queries, parserID, caller, dd);
			
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
				compiler.transform(query, cfg.getValue(), caller, dd);
	
				IQuery addedQuery = executor.addQuery(query.getRoots(), caller, dd, transCfgID);
				executor.startQuery(addedQuery.getID(), caller);
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
