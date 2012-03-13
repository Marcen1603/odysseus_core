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
package de.uniol.inf.is.odysseus.mining.classification.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * This class represents a logical classify operator used to classify tuples of
 * a data stream
 * 
 * @author Sven Vorlauf
 * 
 */
@LogicalOperator(name = "CLASSIFY", minInputPorts = 2, maxInputPorts = 2)
public class ClassifyAO extends BinaryLogicalOp {

	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 4257639023636590526L;

	/**
	 * the attributes that have been used to create the classifier
	 */
	protected List<SDFAttribute> attributes;

	/**
	 * the attribute holding the class
	 */
	protected SDFAttribute labelAttribute;

	/**
	 * create a new logical classification operator
	 */
	public ClassifyAO() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.ILogicalOperator#getOutputSchema
	 * ()
	 */
	@Override
	public SDFSchema getOutputSchema() {
		if (getLabelPosition() < getInputSchema(1).size()) {
			return getInputSchema(1);
		}
        // append the class to the schema if not already in
        List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
        attrs.addAll(getInputSchema(1).clone().getAttributes());
        SDFAttribute classLabel = new SDFAttribute(null,"class_label", SDFDatatype.OBJECT);
        attrs.add(classLabel);
        SDFSchema outputSchema = new SDFSchema("Classify", attrs);
        return outputSchema;
	}

	/**
	 * create a new classification operator as a copy of another classification
	 * operator
	 * 
	 * @param copy
	 *            the operator to copy
	 */
	public ClassifyAO(ClassifyAO copy) {
		super(copy);
		this.attributes = new ArrayList<SDFAttribute>(copy.getAttributes());
		if (copy.labelAttribute != null) {
			this.labelAttribute = copy.labelAttribute.clone();
		}
	}

	/**
	 * get the attributes used to learn the classifier
	 * 
	 * @return the attributes
	 */
	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * determine the positions of the classification attributes in the tuple
	 * 
	 * @return the positions
	 */
	public int[] determineRestrictList() {
		return calcRestrictList(this.getInputSchema(1).getAttributes(), attributes);
	}

	/**
	 * get the position of the class in the tuple
	 * 
	 * @return the position
	 */
	public int getLabelPosition() {
		int i = 0;
		for (SDFAttribute attribute : getInputSchema(1)) {
			if (attribute.equals(labelAttribute)) {
				return i;
			}
			i++;
		}
		return i;
	}

	/**
	 * calculate the positions of the a list of attributes in another list of
	 * attributes
	 * 
	 * @param in
	 *            the schema holding all attributes
	 * @param out
	 *            the schema holding the attributes to determine the positions
	 * @return the positions of the attributes
	 */
	public static int[] calcRestrictList(Collection<SDFAttribute> in,
			List<SDFAttribute> out) {
		int[] ret = new int[out.size()];
		int i = 0;
		for (SDFAttribute a : out) {
			int j = 0;
			int k = i;
			for (SDFAttribute b : in) {
				if (b.equals(a)) {
					ret[i++] = j;
				}
				++j;
			}
			if (k == i) {
				throw new IllegalArgumentException("no such attribute: " + a);
			}
		}
		return ret;
	}

	/**
	 * set the attributes to be used to learn tha classifier
	 * 
	 * @param attributes
	 *            the attributes to set
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, isList = true)
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new ClassifyAO(this);
	}

	/**
	 * set the attribute that is holding the class
	 * 
	 * @param labelAttribute
	 *            the attribute to set
	 */
	@Parameter(type = ResolvedSDFAttributeParameter.class, optional = true)
	public void setLabelAttribute(SDFAttribute labelAttribute) {
		this.labelAttribute = labelAttribute;
	}

}
