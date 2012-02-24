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
/* Generated By:JJTree: Do not edit this line. ASTTimedTuples.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class ASTTimedTuples extends SimpleNode {
	public ASTTimedTuples(int id) {
		super(id);
	}

	public ASTTimedTuples(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * 
	 * @throws QueryParseException */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
		return visitor.visit(this, data);
	}

	@SuppressWarnings("unchecked")
	public RelationalTuple<ITimeInterval>[] getTuples(List<SDFAttribute> schema) {
		RelationalTuple<ITimeInterval>[] tuples = new RelationalTuple[jjtGetNumChildren()];
		for (int i = 0; i < jjtGetNumChildren(); ++i) {
			tuples[i] = ((ASTTimedTuple) jjtGetChild(i)).getTuple(schema);
		}
		return tuples;
	}
}
