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
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * This class represents an abstract logical operator to be used for classifier
 * learn operators
 * 
 * @author Sven Vorlauf
 */

public abstract class AbstractClassificationLearnerAO extends UnaryLogicalOp {

	/**
	 * the UID to identify this class
	 */
	private static final long serialVersionUID = 5930667720134167936L;

	/**
	 * the attributes to be used to learn the classifier
	 */
	protected List<SDFAttribute> attributes;

	/**
	 * the attribute holding the class
	 */
	protected SDFAttribute labelAttribute;

	/**
	 * create a new logical learner operator
	 */
	protected AbstractClassificationLearnerAO() {

	}

	/**
	 * create a new logical learner operator as a copy of another learner
	 * operator
	 * 
	 * @param copy
	 *            the learner operator to copy
	 */
	public AbstractClassificationLearnerAO(AbstractClassificationLearnerAO copy) {
		super(copy);
		this.attributes = new ArrayList<SDFAttribute>(copy.getAttributes());
		this.labelAttribute = copy.labelAttribute.clone();
	}

	/**
	 * get hte attributes to be used to learn the classifier
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
		return calcRestrictList(this.getInputSchema().getAttributes(), attributes);
	}

	/**
	 * get the position of the class in the tuple
	 * 
	 * @return the position
	 */
	public int getLabelPosition() {
		int i = 0;
		for (SDFAttribute attribute : getInputSchema()) {
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
	public static int[] calcRestrictList(List<SDFAttribute> in,
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
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;

	}

}
