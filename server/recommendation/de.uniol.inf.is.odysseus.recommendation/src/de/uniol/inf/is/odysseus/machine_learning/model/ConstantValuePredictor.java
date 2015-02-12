/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.machine_learning.model;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * A model that predicts for always a constant value.
 *
 * @author Cornelius Ludmann
 */
public class ConstantValuePredictor<P> implements
		Model<AbstractStreamObject<IMetaAttribute>, IMetaAttribute, P> {

	private final P value;

	/**
	 * Constructs a new predictor that predicts for all objects
	 * <code>value</code>.
	 */
	public ConstantValuePredictor(final P value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.machine_learning.model.Model#predict(de.uniol
	 * .inf.is.odysseus.core.metadata.AbstractStreamObject)
	 */
	@Override
	public P predict(final AbstractStreamObject<IMetaAttribute> object) {
		return this.value;
	}

}
