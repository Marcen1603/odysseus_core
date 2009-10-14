package de.uniol.inf.is.odysseus.parser.pql.base;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class AccessAOBuilder extends AbstractOperatorBuilder {

	private static final String SOURCE_NAME = "SOURCE";

	@Override
	public ILogicalOperator createOperator(Map<String, Object> parameters,
			List<ILogicalOperator> inputOps) {
		String sourceName = getParameter(parameters, SOURCE_NAME, String.class);
		DataDictionary dataDictionary = DataDictionary.getInstance();
		SDFSource source = dataDictionary.getSource(sourceName);
		SDFEntity entity = dataDictionary.getEntity(sourceName);
		
		AccessAO accessAO = new AccessAO(source);
		accessAO.setOutputSchema(entity.getAttributes());
		
		return accessAO;
	}

}
