package de.uniol.inf.is.odysseus.parser.pql.base;

import java.util.List;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.parser.pql.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.pql.DirectParameter;
import de.uniol.inf.is.odysseus.parser.pql.Parameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

/**
 * @author Jonas Jacobi
 */
public class AccessAOBuilder extends AbstractOperatorBuilder {

	private static final String SOURCE_NAME = "SOURCE";
	private final DirectParameter<String> sourceName;

	public AccessAOBuilder() {
		this.sourceName = new DirectParameter<String>(SOURCE_NAME,
				String.class, REQUIREMENT.MANDATORY);
		setParameters(sourceName);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 0);

		String sourceName = this.sourceName.getValue();
		DataDictionary dataDictionary = DataDictionary.getInstance();
		SDFSource source = dataDictionary.getSource(sourceName);
		SDFEntity entity = dataDictionary.getEntity(sourceName);

		AccessAO accessAO = new AccessAO(source);
		accessAO.setOutputSchema(entity.getAttributes());

		return accessAO;
	}
}
