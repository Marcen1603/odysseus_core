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
/* Generated By:JJTree: Do not edit this line. ASTIdentifier.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;

public class ASTIdentifier extends SimpleNode {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ASTIdentifier(int id) {
		super(id);
	}

	public ASTIdentifier(NewSQLParser p, int id) {
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
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		return this.name.equals(((ASTIdentifier) obj).name);
	}
	
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
