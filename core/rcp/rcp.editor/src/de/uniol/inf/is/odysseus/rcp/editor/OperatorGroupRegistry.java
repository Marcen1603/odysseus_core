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
