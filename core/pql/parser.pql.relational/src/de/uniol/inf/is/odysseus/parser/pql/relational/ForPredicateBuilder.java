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
package de.uniol.inf.is.odysseus.parser.pql.relational;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IPredicateBuilder;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.ForPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.ForPredicate.Type;

/**
 * @author Dennis Geesen
 * 
 */
public class ForPredicateBuilder implements IPredicateBuilder {

	
	private Type type;

	public ForPredicateBuilder(Type type){
		this.type = type;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.
	 * IPredicateBuilder
	 * #createPredicate(de.uniol.inf.is.odysseus.core.server.sourcedescription
	 * .sdf.schema.IAttributeResolver, java.lang.String)
	 */	
	@Override
	public IPredicate<?> createPredicate(IAttributeResolver resolver, String predicate) {
		if (predicate.contains(",")) {
			String[] parts = predicate.split(",");
			String attributeName = parts[0].trim();
			predicate = parts[1].trim();
			SDFAttribute attribute = resolver.getAttribute(attributeName);
			if(!attribute.getDatatype().isListValue()){
				throw new IllegalArgumentException("Attribute "+attributeName+" for ForListPredicate must be multi value");
			}
			
			ForPredicate pred = new ForPredicate(type, attribute, predicate);
			return pred;
		}else{
			throw new IllegalArgumentException("ForListPredicate needs an attribute!");
		}
	}

}
