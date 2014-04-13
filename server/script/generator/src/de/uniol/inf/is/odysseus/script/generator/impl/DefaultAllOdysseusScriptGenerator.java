package de.uniol.inf.is.odysseus.script.generator.impl;

import java.util.List;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorFooter;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorHeader;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorQueryFooter;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorQueryHeader;

public class DefaultAllOdysseusScriptGenerator implements IOdysseusScriptGeneratorFooter, IOdysseusScriptGeneratorHeader, IOdysseusScriptGeneratorQueryFooter, IOdysseusScriptGeneratorQueryHeader {

	@Override
	public String getHeader(List<ILogicalQuery> queries, QueryBuildConfiguration config) {
		return null;
	}

	@Override
	public String getFooter(List<ILogicalQuery> queries, QueryBuildConfiguration config) {
		return null;
	}
	
	@Override
	public String getQueryHeader(ILogicalQuery query, QueryBuildConfiguration config) {
		if( !Strings.isNullOrEmpty(query.getName())) {
			return "#QNAME " + query.getName();
		}
		return null;
	}

	@Override
	public String getQueryFooter(ILogicalQuery query, QueryBuildConfiguration config) {
		return null;
	}

}
