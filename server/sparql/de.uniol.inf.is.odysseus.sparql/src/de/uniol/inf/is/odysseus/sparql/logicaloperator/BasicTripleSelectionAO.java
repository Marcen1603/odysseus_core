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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.rdf.datamodel.Triple;

@SuppressWarnings("rawtypes")
class BasicTripleSelectionAO extends SelectAO {

	private static final long serialVersionUID = -8187061597335204197L;
	protected Triple triple;

	public BasicTripleSelectionAO(Triple t) {
		this.triple = t;
	}

	public void calcOutElements() {
		List<SDFAttribute> l = new ArrayList<SDFAttribute>();

		if (triple.getSubject().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#" + triple.getSubject().getName(), SDFDatatype.STRING,
					null, null, null));
		}
		if (triple.getPredicate().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#" + triple.getPredicate().getName(), SDFDatatype.STRING,
					null, null, null));
		}
		if (triple.getObject().isVariable()) {
			l.add(new SDFAttribute(null, this.hashCode() + "#" + triple.getObject().getName(), SDFDatatype.STRING, null,
					null, null));
		}
		if (getInputAO() != null && getInputSchema() != null) {
			l.addAll(getInputSchema().getAttributes());
		}
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(l, getInputSchema());
		this.setOutputSchema(schema);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (getOutputSchema() == null || recalcOutputSchemata) {
			calcOutElements();
		}
		return getOutputSchema();
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
