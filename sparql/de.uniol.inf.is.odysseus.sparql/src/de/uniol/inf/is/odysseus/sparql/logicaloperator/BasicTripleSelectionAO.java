/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Triple;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;

public class BasicTripleSelectionAO extends SelectAO {

	private static final long serialVersionUID = -8187061597335204197L;
	protected Triple triple;

	public BasicTripleSelectionAO(Triple t) {
		this.triple = t;
		calcOutElements();
	}

	public void calcOutElements() {
		List<SDFAttribute> l = new ArrayList<SDFAttribute>();

		if (triple.getSubject().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#"
					+ triple.getSubject().getName(), SDFDatatype.STRING));
		}
		if (triple.getPredicate().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#"
					+ triple.getPredicate().getName(), SDFDatatype.STRING));
		}
		if (triple.getObject().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#"
					+ triple.getObject().getName(), SDFDatatype.STRING));
		}
		if (getInputAO() != null && getInputSchema() != null) {
			l.addAll(getInputSchema().getAttributes());
		}
		SDFSchema schema = new SDFSchema("", l);
		this.setOutputSchema(schema);
	}

	public BasicTripleSelectionAO(BasicTripleSelectionAO selection) {
		super();
		this.triple = selection.triple;
	}

	public Triple getTriple() {
		return triple;
	}

	public void setT(Triple t) {
		this.triple = t;
	}

	@Override
	public BasicTripleSelectionAO clone() {
		return new BasicTripleSelectionAO(this);
	}

	@Override
	public String toString() {
		return "BasicTripleSelection (" + this.triple.toString() + ")";
	}

}
