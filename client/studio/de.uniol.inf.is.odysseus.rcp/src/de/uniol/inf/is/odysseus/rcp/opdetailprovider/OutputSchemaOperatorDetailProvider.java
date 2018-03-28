package de.uniol.inf.is.odysseus.rcp.opdetailprovider;

import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.rcp.AbstractKeyValueGeneralProvider;

public class OutputSchemaOperatorDetailProvider extends AbstractKeyValueGeneralProvider {

	@Override
	public String getTitle() {
		return "OutputSchema";
	}

	@Override
	protected Map<String, String> getKeyValuePairs(IPhysicalOperator operator) {
		Map<String, String> map = Maps.newHashMap();
		for( SDFAttribute attribute : operator.getOutputSchema() ) {
			String constraints = "[";
			for (SDFConstraint constraint : attribute.getDtConstraints()) {
				constraints = (constraints.length() > 1 ? ", " : "") + constraints + constraint.getURI() + " : " + constraint.getValue();
			}
			constraints = constraints + "]";
			map.put(determineFullName(attribute), attribute.getDatatype().getQualName() + "; constraints: " + constraints);
		}
		return map;
	}

	private static String determineFullName( SDFAttribute attribute ) {
		if( !Strings.isNullOrEmpty(attribute.getSourceName())) {
			return attribute.getSourceName() + "." + attribute.getAttributeName();
		}
		return attribute.getAttributeName();
	}
}
