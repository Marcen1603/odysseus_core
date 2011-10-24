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
/* Generated By:JJTree: Do not edit this line. ASTTimeInterval.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;

public class ASTTimeInterval extends SimpleNode {
	private ITimeInterval interval;

	public ASTTimeInterval(int id) {
		super(id);
	}

	public ASTTimeInterval(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * 
	 * @throws QueryParseException */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
		return visitor.visit(this, data);
	}

	public ITimeInterval getInterval() {
		return interval;
	}

	public void setInterval(ITimeInterval interval) {
		this.interval = interval;
	}
}
