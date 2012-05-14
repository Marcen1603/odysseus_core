/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.detection.stateless;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * 
 * @author Dennis Geesen Created at: 21.06.2011
 */
public class OutOfDomainDetection implements IUnaryDetection<Tuple<?>> {

	private double min;
	private double max;
	private RelationalPredicate predicate;
	private SDFSchema inputschema;
	private String attributeName;

	public OutOfDomainDetection(String attributeName, double min, double max) {
		this.min = min;
		this.max = max;
		this.attributeName = attributeName;
	}

	private void internalInit() {
		try {
			IAttributeResolver attributeResolver = new DirectAttributeResolver(this.inputschema);
			SDFAttribute attribute = attributeResolver.getAttribute(this.attributeName);
			// build the predicate
			String predicateString = attribute.getAttributeName() + " < " + this.min;
			predicateString = predicateString + " || " + attribute.getAttributeName() + " > " + this.max;
			SDFExpression expression = new SDFExpression("", predicateString, attributeResolver, MEP.getInstance());
			this.predicate = new RelationalPredicate(expression);
		} catch (NoSuchAttributeException ex) {
			System.err.println("Could not found the attribute \"" + ex.getAttribute() + "\" in schema ");
			throw ex;
		}
	}

	@Override
	public IPredicate<Tuple<?>> getPredicate() {
		return this.predicate;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMax() {
		return max;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMin() {
		return min;
	}

	@Override
	public void init(SDFSchema inputschema) {
		this.inputschema = inputschema;
		this.internalInit();
		this.predicate.init(this.inputschema, null);
	}

	@Override
	public String getAttribute() {
		return this.attributeName;
	}	
}
