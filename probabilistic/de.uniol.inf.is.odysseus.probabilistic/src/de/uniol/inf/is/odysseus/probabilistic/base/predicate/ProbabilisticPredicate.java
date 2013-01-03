/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class ProbabilisticPredicate implements
		IRelationalProbabilisticPredicate {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5008657267475886955L;

	protected SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	protected int[] attributePositions;
	final List<SDFAttribute> neededAttributes;

	public ProbabilisticPredicate(SDFExpression expression) {
		this.expression = expression;
		this.neededAttributes = expression.getAllAttributes();
	}

	public ProbabilisticPredicate(ProbabilisticPredicate predicate) {
		this.attributePositions = predicate.attributePositions == null ? null
				: (int[]) predicate.attributePositions.clone();
		this.expression = predicate.expression == null ? null
				: predicate.expression.clone();
		this.neededAttributes = new ArrayList<SDFAttribute>(
				predicate.neededAttributes);
	}

	public ProbabilisticPredicate(IPredicate<?> predicate) {
		// TODO Auto-generated constructor stub

		this.neededAttributes = predicate.getAttributes();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public double evaluate(Tuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Double) this.expression.getValue();
	}

	@Override
	public double evaluate(Tuple<?> left, Tuple<?> right) {
		return 0.0;
	}

	@Override
	public boolean isContainedIn(IProbabilisticPredicate<?> o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(this.expression.getAllAttributes());
	}

	@Override
	public boolean equals(IProbabilisticPredicate<Tuple<?>> pred) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		// TODO Auto-generated method stub

	}

	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		// TODO Auto-generated method stub

	}

	@Override
	public ProbabilisticPredicate clone() {
		return new ProbabilisticPredicate(this);
	}
}
