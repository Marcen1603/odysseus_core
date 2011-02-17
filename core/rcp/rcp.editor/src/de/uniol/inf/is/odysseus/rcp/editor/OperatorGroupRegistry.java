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
package de.uniol.inf.is.odysseus.rcp.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class OperatorGroupRegistry {

	private static OperatorGroupRegistry instance = null;

	public static String DEFAULT_OPERATOR_GROUP = "Other";

	private Map<String, List<String>> operators;

	private OperatorGroupRegistry() {

	}

	public static OperatorGroupRegistry getInstance() {
		if (instance == null)
			instance = new OperatorGroupRegistry();
		return instance;
	}

	public void registerOperatorGroup(String groupName) {
		if( groupName == null || groupName.isEmpty() )
			groupName = DEFAULT_OPERATOR_GROUP;
		
		if (getOperators().containsKey(groupName))
			return;
		
		getOperators().put(groupName, new ArrayList<String>());
	}
	
	public void registerOperator( String operatorName, String group ) {
		if( !getOperators().containsKey(group))
			registerOperatorGroup(group);
		
		if( !getOperators().get(group).contains(operatorName)) 
			getOperators().get(group).add(operatorName);
	}
	
	public Set<String> getOperatorGroups() {
		return getOperators().keySet();
	}
	
	public List<String> getOperators( String group ) {
		return getOperators().get(group);
	}
	
	public String getOperatorGroup( String opName ) {
		for( String grp : getOperatorGroups() ) {
			List<String> ops = getOperators(grp);
			if( ops.contains(opName))
				return grp;
		}
		return DEFAULT_OPERATOR_GROUP;
	}

	private Map<String, List<String>> getOperators() {
		if (operators == null) {
			operators = new HashMap<String, List<String>>();

			// Default-Group
			registerOperatorGroup(DEFAULT_OPERATOR_GROUP);
		}
		return operators;
	}
}
