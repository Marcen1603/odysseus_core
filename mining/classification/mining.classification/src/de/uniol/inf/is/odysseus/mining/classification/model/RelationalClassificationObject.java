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
package de.uniol.inf.is.odysseus.mining.classification.model;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class implements a classification object for relational tuples
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class RelationalClassificationObject<T extends IMetaAttribute>
		implements IClassificationObject<T> {

	/**
	 * the tuple wrapped by this classification object
	 */
	private RelationalTuple<T> tuple;

	/**
	 * a version of teh tuple restricted to the classification attributes
	 */
	private RelationalTuple<T> restrictedTuple;

	/**
	 * the class belonging to the tuple
	 */
	private Object classLabel;

	/**
	 * the position of the class attribute in the tuple
	 */
	private int labelAttributePosition;

	/**
	 * create a new classification object to wrap agiven tuple
	 * 
	 * @param tuple
	 *            the tuple to wrap
	 * @param restrictList
	 *            the positions of the classification attributes in th etuple
	 * @param labelAttributePosition
	 *            the position of the class attribute in the tuple
	 */
	public RelationalClassificationObject(RelationalTuple<T> tuple,
			int[] restrictList, int labelAttributePosition) {
		this.labelAttributePosition = labelAttributePosition;
		this.tuple = tuple;
		this.restrictedTuple = tuple.restrict(restrictList, true);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.classification.IClassificationObject#getAttributes()
	 */
	@Override
	public Object[] getAttributes() {
		return tuple.getAttributes();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.classification.IClassificationObject#getClassificationAttributeCount()
	 */
	@Override
	public int getClassificationAttributeCount() {
		return restrictedTuple.size();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.classification.IClassificationObject#getClassificationAttributes()
	 */
	@Override
	public Object[] getClassificationAttributes() {
		return restrictedTuple.getAttributes();
	}

	/** get a version of the wrapped tuple containing the class
	 * @return a tuple with the belonging class
	 */
	public RelationalTuple<T> getClassifiedTuple() {
		if (labelAttributePosition < tuple.size()) {
			RelationalTuple<T> newTuple = tuple.clone();
			newTuple.setAttribute(labelAttributePosition, getClassLabel());
			return newTuple;
		} else {
			return tuple.append(getClassLabel(), true);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.classification.IClassificationObject#getClassLabel()
	 */
	@Override
	public Object getClassLabel() {
		return classLabel;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.datamining.classification.IClassificationObject#setClassLabel(java.lang.Object)
	 */
	@Override
	public void setClassLabel(Object classLabel) {
		this.classLabel = classLabel;
	}

}
