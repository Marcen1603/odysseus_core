/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.script.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractPreParserKeyword implements IPreParserKeyword {
	
	public static final String ADD_TRANS_PARAMS = "ADD_TRANS_PARAMS";
	IOdysseusScriptParser parser;
	
	@Override
	public void setParser(IOdysseusScriptParser parser) {
		this.parser = parser;
	}
	
	@Override
	public IOdysseusScriptParser getParser() {
		return parser;
	}		
	
	/**
	 * Get Parameters with blanc as delimmiter
	 * @param parameter
	 * @return
	 */
	protected String[] getSimpleParameters(String parameter) {
		if( parameter.contains(" ")) {
			return parameter.split("\\ ");
		} 
		
		return new String[] { parameter, "" };
	}
	
	protected List<IQueryBuildSetting<?>> getAdditionalTransformationSettings(Map<String, Object> variables){
		@SuppressWarnings("unchecked")
		List<IQueryBuildSetting<?>> addSettings =  (List<IQueryBuildSetting<?>>) variables.get(ADD_TRANS_PARAMS);
		if (addSettings == null){
			addSettings = new ArrayList<IQueryBuildSetting<?>>();
			variables.put(ADD_TRANS_PARAMS, addSettings);
		}
		return addSettings;
	}

	
	@Override
	public Collection<String> getAllowedParameters(ISession caller) {
		return new ArrayList<>();
	}

	@Override
	public final boolean isDeprecated() {
		return (getClass().getAnnotation(Deprecated.class) != null );
	}
	
	@Override
	public String getDeprecationInfo() {
		return null;
	}

}
