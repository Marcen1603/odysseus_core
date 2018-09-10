package de.uniol.inf.is.odysseus.script.parser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public interface IOdysseusScriptConfigSetting {

	public String getName();
	
	public boolean isValidValue( String value, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller);
	public void set( String newValue, Map<String, Object> variables, List<IQueryBuildSetting<?>> queryBuildSettings, ISession caller );
	
	public Collection<String> getAllowedValues(ISession caller);
}
