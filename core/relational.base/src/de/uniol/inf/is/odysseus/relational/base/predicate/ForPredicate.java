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
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Dennis Geesen
 * 
 */
public class ForPredicate extends AbstractPredicate<Tuple<?>> implements IRelationalPredicate {

	public enum Type {
		ANY, ALL
	}

	private static final long serialVersionUID = 3711797735975124056L;
	private SDFExpression expression;
	private SDFAttribute listAttribute;
	private int index;
	private RelationalPredicate relationalPredicate;
	private SDFSchema innerSchema;
	private Type type = Type.ALL;
	private String predicate;

	public ForPredicate(Type type, SDFAttribute listAttribute, String predicate) {
		this.type = type;
		this.predicate = predicate;
		this.listAttribute = listAttribute;
	}

	/**
	 * @param forPredicate
	 */
	public ForPredicate(ForPredicate fp) {
		this.expression = fp.expression;
		this.listAttribute = fp.listAttribute;
		this.index = fp.index;
		this.relationalPredicate = fp.relationalPredicate;
		this.innerSchema = fp.innerSchema;
		this.type = fp.type;
		this.predicate = fp.predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang
	 * .Object)
	 */
	@Override
	public boolean evaluate(Tuple<?> input) {
		List<Tuple<?>> tuples = input.getAttribute(index);
		if (type == Type.ALL) {
			for (Tuple<?> tuple : tuples) {
				boolean res = this.relationalPredicate.evaluate(tuple);
				if (!res) {
					// for all : if one is false, all are false
					return false;
				}
			}
			// all true -> result is true
			return true;
		}else{
			// type == ANY
			for (Tuple<?> tuple : tuples) {
				boolean res = this.relationalPredicate.evaluate(tuple);
				if (res) {
					// for any : if one is true, all are true
					return true;
				}
			}
			// all false -> result is false
			return false;
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.predicate.IPredicate#evaluate(java.lang
	 * .Object, java.lang.Object)
	 */
	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		return evaluate(left);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate
	 * #init(de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema)
	 */
	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		this.index = leftSchema.indexOf(listAttribute);
		this.innerSchema = listAttribute.getDatatype().getSchema();
		
		DirectAttributeResolver resolver = new DirectAttributeResolver(innerSchema);
		SDFExpression expression = new SDFExpression("", predicate, resolver, MEP.getInstance());
		
		
		this.relationalPredicate = new RelationalPredicate(expression);
		this.relationalPredicate.init(innerSchema, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate
	 * #replaceAttribute(de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute)
	 */
	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate#clone()
	 */
	@Override
	public AbstractPredicate<Tuple<?>> clone() {
		return new ForPredicate(this);
	}

}
