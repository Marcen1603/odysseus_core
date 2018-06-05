/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRenamedExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectAll;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;

/**
 * @author Jonas Jacobi
 */
public class CheckAttributes extends AbstractDefaultVisitor {
	// TODO distinct expressions

	private AttributeResolver attributeResolver;

	public CheckAttributes(AttributeResolver attributeResolver) {
		this.attributeResolver = attributeResolver;
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) throws QueryParseException {
		for (int i = 0; i < node.jjtGetNumChildren(); ++i) {
			Node curChild = node.jjtGetChild(i);
			if (!isAttributeValid(curChild)) {
				throw new QueryParseException("invalid Attribute: " + curChild + " in GROUP BY clause");
			}
		}
		return data;
	}

	@Override
	/*
	 * ignore source nodes, because they can contain subqueries and we don't
	 * want to check their attributes
	 */
	public Object visit(ASTSubselect node, Object data) throws QueryParseException {
		return data;
	}

	private boolean isAttributeValid(Node node) {
		ASTIdentifier identifier = (ASTIdentifier) node;		
		return this.attributeResolver.isAttributeValid(identifier.getName());
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) throws QueryParseException {
		return data;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) throws QueryParseException {
		Node childNode = node.jjtGetChild(0);
		if (childNode instanceof ASTIdentifier ) {
			String name = ((ASTIdentifier)childNode).getName();
			// otherwise it could be something like person.* 
			if(!name.endsWith(".*")){
				if(!isAttributeValid(childNode)){
					throw new QueryParseException("invalid Attribute: " + childNode);
				}
			}
		}
		if (childNode instanceof ASTAggregateExpression && !isAttributeValid(childNode.jjtGetChild(1))) {
			throw new QueryParseException("invalid Attribute: " + childNode);
		}
		return data;
	}	

	@Override
	public Object visit(ASTSelectClause node, Object data) throws QueryParseException {
		if (!(node.jjtGetChild(0) instanceof ASTSelectAll)) {
			for (int i = 0; i < node.jjtGetNumChildren(); i++) {
				// selectClause -> renamedExpression ?
				if(!(node.jjtGetChild(i) instanceof ASTRenamedExpression)){
					continue;
				}
				// selectClause -> renamedExpression -> expression -> simpleToken ?
				if(node.jjtGetChild(i).jjtGetChild(0) instanceof ASTSelectAll){
					continue;
				}
				
				Node simpleToken = node.jjtGetChild(i).jjtGetChild(0).jjtGetChild(0);
				if(!(simpleToken instanceof ASTSimpleToken)){
					continue;
				}
			}
		}
		return super.visit(node, data);
	}

}
