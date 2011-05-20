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
/* Generated By:JJTree: Do not edit this line. ASTAllPredicate.java */

package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;

public class ASTAllPredicate extends AbstractQuantificationPredicate {
	public ASTAllPredicate(int id) {
		super(id);
	}

	public ASTAllPredicate(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. * */
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.querytranslation.parser.IExistencePredicate#getTuple()
	 */
	@Override
	public ASTTuple getTuple() {
		return (ASTTuple) jjtGetChild(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.querytranslation.parser.IExistencePredicate#getCompareOperator()
	 */
	@Override
	public String getCompareOperator() {
		return AbstractDefaultVisitor
				.toInverseCompareOperator(jjtGetChild(1).toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.querytranslation.parser.IExistencePredicate#getQuery()
	 */
	@Override
	public ASTComplexSelectStatement getQuery() {
		return (ASTComplexSelectStatement) jjtGetChild(2);
	}
}
