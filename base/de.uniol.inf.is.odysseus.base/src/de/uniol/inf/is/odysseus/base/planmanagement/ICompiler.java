package de.uniol.inf.is.odysseus.base.planmanagement;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;

public interface ICompiler extends IInfoProvider {
	public List<ILogicalOperator> translateQuery(String query,
			String parserID) throws QueryParseException;

	public ILogicalOperator restructPlan(ILogicalOperator logicalAlgebraList);

	public IPhysicalOperator transform(ILogicalOperator logicalPlan,
			TransformationConfiguration transformationConfiguration)
			throws TransformationException;

	public Set<String> getSupportedQueryParser();
}