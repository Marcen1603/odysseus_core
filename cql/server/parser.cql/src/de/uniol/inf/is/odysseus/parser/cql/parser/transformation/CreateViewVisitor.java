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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateQueryCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateViewCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateViewStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;

public class CreateViewVisitor extends AbstractDefaultVisitor {

	private ISession caller;
	private IDataDictionary dd;
	private List<IExecutorCommand> commands;

	public CreateViewVisitor(ISession user, IDataDictionary dd,
			List<IExecutorCommand> commands) {
		this.caller = user;
		this.dd = dd;
		this.commands = commands;
	}

	
	@Override
	public Object visit(ASTCreateViewStatement node, Object data)
			throws QueryParseException {
		ASTIdentifier nameNode = (ASTIdentifier) node.jjtGetChild(0);
		String viewName = nameNode.getName();

		CQLParser parser = new CQLParser();
		parser.setUser(caller);
		parser.setDataDictionary(dd);
		IExecutorCommand result = (IExecutorCommand) parser.visit(
				(ASTPriorizedStatement) node.jjtGetChild(1), null);
		if (result instanceof CreateQueryCommand) {

			ILogicalOperator operator = ((CreateQueryCommand) result)
					.getQuery().getLogicalPlan().getRoot();

			// we add an additional rename, so that the view has this names...
			RenameAO rename = new RenameAO();
			rename.subscribeTo(operator, operator.getOutputSchema());
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			for (SDFAttribute old : operator.getOutputSchema()) {
				attributes.add(new SDFAttribute(viewName, old
						.getAttributeName(), old));
			}
			rename.setOutputSchema(SDFSchemaFactory.createNewWithAttributes(viewName, attributes, operator.getOutputSchema()));
			operator = rename;

			CreateViewCommand cmd = new CreateViewCommand(viewName, operator,
					caller);
			commands.add(cmd);
		}
		return null;		
	}
}
