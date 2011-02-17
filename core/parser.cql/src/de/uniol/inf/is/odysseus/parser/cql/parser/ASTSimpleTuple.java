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
/* Generated By:JJTree: Do not edit this line. ASTSimpleTuple.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
@SuppressWarnings("unchecked")
public class ASTSimpleTuple extends SimpleNode {
	public ASTSimpleTuple(int id) {
		super(id);
	}

	public ASTSimpleTuple(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public RelationalTuple getTuple(SDFAttributeList schema) {
		if (schema.size() != jjtGetNumChildren()) {
			// TODO exceptionhandling anstaendig machen und bessere
			// fehlermeldungen geben
			throw new RuntimeException("tuple definition does not fit schema");
		}
		Object[] values = new Object[jjtGetNumChildren()];
		Iterator<SDFAttribute> it = schema.iterator();
		for (int i = 0; i < jjtGetNumChildren(); ++i) {
			Node child = jjtGetChild(i);
			String type = it.next().getDatatype().getURI(false);
			if (child.getClass() == ASTString.class && type.equals("String")) {
				values[i] = ((ASTString) child).getValue();
			} else {
				String value = ((ASTNumber) child).getValue();
				if (type.equals("Integer")) {
					values[i] = Integer.parseInt(value);
					continue;
				} else if (type.equals("Double")) {
					values[i] = Double.parseDouble(value);
					continue;
				}
				throw new RuntimeException(
						"type of attribute does not match schema");
			}
		}
		return new RelationalTuple(values);
	}
}
