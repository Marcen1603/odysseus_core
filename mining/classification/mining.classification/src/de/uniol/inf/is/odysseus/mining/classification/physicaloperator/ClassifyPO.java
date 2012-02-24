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
package de.uniol.inf.is.odysseus.mining.classification.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.mining.classification.model.IClassifier;
import de.uniol.inf.is.odysseus.mining.classification.model.RelationalClassificationObject;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * This class represents a physical operator used to classify the relational
 * tuples of a data stream with a classifier
 * 
 * @author Sven Vorlauf
 * 
 * @param <T>
 *            the type of the IMetaAttribute
 */
public class ClassifyPO<T extends IMetaAttribute> extends
		AbstractClassificationPO<T> {

	protected IClassifier<T> classifier;

	public ClassifyPO(ClassifyPO<T> classifyPO) {
		super(classifyPO);
	}

	public ClassifyPO() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.classification.physicaloperator.
	 * AbstractClassificationPO
	 * #process_next(de.uniol.inf.is.odysseus.relational.base.RelationalTuple,
	 * int)
	 */
	@Override
	protected void process_next(RelationalTuple<T> object, int port) {
		if (port == 0) {
			// set the new classifier
			classifier = object.getAttribute(0);
		} else {
			// predict the class if the tuple is not already classified
			if (!(labelPosition < object.size())
					|| object.getAttribute(labelPosition) != null) {
				processNext(new RelationalClassificationObject<T>(object,
						restrictList, labelPosition));
			} else {
				transfer(object);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.datamining.classification.physicaloperator.
	 * AbstractClassificationPO
	 * #processNext(de.uniol.inf.is.odysseus.core.server.datamining.classification
	 * .RelationalClassificationObject)
	 */
	@Override
	protected void processNext(RelationalClassificationObject<T> tuple) {
		if (classifier != null) {
			// predict the class if a classifier exists

			tuple.setClassLabel(classifier.getClassLabel(tuple));
		}
		transfer(tuple.getClassifiedTuple());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe#clone()
	 */
	@Override
	public AbstractPipe<RelationalTuple<T>, RelationalTuple<T>> clone() {
		return new ClassifyPO<T>(this);
	}

}