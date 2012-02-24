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
/* Generated By:JJTree: Do not edit this line. ASTTuple.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class ASTTuple extends SimpleNode {
	public ASTTuple(int id) {
		super(id);
	}

	public ASTTuple(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * 
	 * @throws QueryParseException */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
		return visitor.visit(this, data);
	}

	@Override
	public String toString() {
		if (jjtGetNumChildren() == 1) {
			return jjtGetChild(0).toString();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		buffer.append(jjtGetChild(0).toString());
		for (int i = 1; i < jjtGetNumChildren(); ++i) {
			buffer.append(',');
			buffer.append(jjtGetChild(i).toString());
		}
		buffer.append(']');
		return buffer.toString();
	}
}
