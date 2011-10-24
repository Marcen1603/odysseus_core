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
package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWhereClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class CheckGroupBy extends AbstractDefaultVisitor {
	private boolean hasAggregates;

	private Set<SDFAttribute> checkGroupingAttributes;

	private AttributeResolver attributeResolver;

	private boolean hasGrouping;

	@Override
	public Object visit(ASTSelectClause node, Object data) throws QueryParseException {
		hasAggregates = false;
		hasGrouping = false;
		this.checkGroupingAttributes = new HashSet<SDFAttribute>();
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return data;
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) throws QueryParseException {
		hasAggregates = true;
		return data;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) throws QueryParseException {
		Node child = node.jjtGetChild(0);
		if (child instanceof ASTIdentifier) {
			checkGroupingAttributes.add(this.attributeResolver
					.getAttribute(child.toString()));
		} else {
			child.jjtAccept(this, data);
		}
		return data;
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) throws QueryParseException {
		this.hasGrouping = true;
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			String curIdentifier = ((ASTIdentifier) node.jjtGetChild(i))
					.getName();
			SDFAttribute attribute = this.attributeResolver
					.getAttribute(curIdentifier);

			checkGroupingAttributes.remove(attribute);
		}
		return data;
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) throws QueryParseException {
		return data;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) throws QueryParseException {
		return data;
	}

	public boolean checkOkay() {
		return checkGroupingAttributes.isEmpty()
				|| (!hasAggregates && !hasGrouping);
	}

	public void init(AttributeResolver attributeResolver) {
		this.attributeResolver = attributeResolver;
	}
}
