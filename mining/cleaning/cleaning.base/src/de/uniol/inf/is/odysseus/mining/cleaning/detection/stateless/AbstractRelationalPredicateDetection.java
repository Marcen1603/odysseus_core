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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * 
 * @author Dennis Geesen
 * Created at: 23.06.2011
 */
public abstract class AbstractRelationalPredicateDetection implements IUnaryDetection<RelationalTuple<?>> {

	private RelationalPredicate predicate;
	private String attributeName;
	
	public AbstractRelationalPredicateDetection(String attributeName){
		this.attributeName = attributeName;
	}
	
	protected RelationalPredicate buildPredicate(String predicateString, SDFSchema schema) {
		try {
			IAttributeResolver attributeResolver = new DirectAttributeResolver(schema);			
			// build the predicate			
			SDFExpression expression = new SDFExpression("", predicateString, attributeResolver, MEP.getInstance());
			predicate = new RelationalPredicate(expression);
			return predicate;
		} catch (NoSuchAttributeException ex) {
			System.err.println("Could not found the attribute \"" + ex.getAttribute() + "\" in schema ");
			throw ex;
		}
	}	
	
	public void init(SDFSchema schema){		
		String predicateString = createPredicate();
		buildPredicate(predicateString, schema);
		this.predicate.init(schema, null);
	}
	
	public abstract String createPredicate();
	
	@Override
	public IPredicate<RelationalTuple<?>> getPredicate() {		
		return predicate;
	}
	
	@Override
	public String getAttribute() {		
		return this.attributeName;
	}

}
