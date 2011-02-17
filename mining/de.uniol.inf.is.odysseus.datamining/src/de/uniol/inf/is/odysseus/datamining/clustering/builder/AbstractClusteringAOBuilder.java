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
package de.uniol.inf.is.odysseus.datamining.clustering.builder;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.datamining.builder.NonNumericAttributeException;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * This class is a super class to create {@link AbstractOperatorBuilder} for logical
 * clustering operator. It specifies a parameter for a list of attributes to determine which
 * attributes should be used for clustering. It also guarantees that all the
 * selected attributes are of a numerical data type.
 * 
 * @author Kolja Blohm
 * 
 */
public abstract class AbstractClusteringAOBuilder extends
		AbstractOperatorBuilder {

	private static final long serialVersionUID = -4469186115851181332L;

	private static final String ATTRIBUTES = "ATTRIBUTES";

	protected ListParameter<SDFAttribute> attributes;

	/**
	 * Constructor to initialize an AbstractClusteringAOBuilder.
	 */
	public AbstractClusteringAOBuilder() {

		super(1, 1);
		attributes = new ListParameter<SDFAttribute>(ATTRIBUTES,
				REQUIREMENT.MANDATORY, new ResolvedSDFAttributeParameter(
						"data mining attribute", REQUIREMENT.MANDATORY));
		setParameters(attributes);
	}

	/**
	 * Validates that all the attributes are numerical and initiates an error
	 * otherwise.
	 * 
	 * @see de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder#internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		Iterator<SDFAttribute> iter = attributes.getValue().iterator();
		while (iter.hasNext()) {
			SDFAttribute attribute = iter.next();
			if (!SDFDatatypes.isNumerical(attribute.getDatatype())) {
				addError(new NonNumericAttributeException(attribute));
				return false;
			}
		}
		return true;
	}

}
