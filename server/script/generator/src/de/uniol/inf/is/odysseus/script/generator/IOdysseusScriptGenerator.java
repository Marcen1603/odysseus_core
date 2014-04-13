package de.uniol.inf.is.odysseus.script.generator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public interface IOdysseusScriptGenerator {

	public String generate( List<ILogicalQuery> queries );
	
}
