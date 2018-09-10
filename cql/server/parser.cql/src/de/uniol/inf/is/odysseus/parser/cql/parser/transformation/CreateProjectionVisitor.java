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
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IMepConstant;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMatrixExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTNumber;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProjectionMatrix;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProjectionVector;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRenamedExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectAll;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTString;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;

/**
 * @author Jonas Jacobi
 */
public class CreateProjectionVisitor extends AbstractDefaultVisitor {
	private AttributeResolver attributeResolver;

	private ILogicalOperator _top;

	private List<SDFExpression> expressions = new ArrayList<SDFExpression>();
	private SDFSchema _outputSchema = null;
	private List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();

	private SDFSchema _aliasSchema = null;
	private List<SDFAttribute> aliasAttributes = new ArrayList<SDFAttribute>();

	private boolean mathematicalExpressionNeeded = false;

	double[][] projectionMatrix = null;

	double[] projectionVector = null;

	public CreateProjectionVisitor(ILogicalOperator top, AttributeResolver attributeResolver) {
		this._top = top;
		this.attributeResolver = attributeResolver;
	}

	// TODO: This is the only valid Entry point ... how can the other elements
	// be set to private??
	@Override
	public Object visit(ASTSelectStatement statement, Object data) throws QueryParseException {

		SimpleNode node = (SimpleNode) statement.jjtGetChild(0);

		// create output schema (with different visit-Methods)
		node.childrenAccept(this, null);

		// validate output schema
		checkAttributes(aliasAttributes);

		SDFSchema inputSchema = _top.getOutputSchema();
		_outputSchema = SDFSchemaFactory.createNewWithAttributes(outputAttributes, inputSchema);
		// create operator: if there is a need for a project/map operator,
		// create one and set top operator to it
		if (!_outputSchema.equals(inputSchema) || projectionMatrix != null) {
			// if there are no mathematical expressions, a simple ProjectAO will
			// do, otherwise a Map operator is needed
			if (!mathematicalExpressionNeeded) {
				// createrestrictlist

				if (projectionMatrix == null) {
					ProjectAO project = new ProjectAO();
					project.subscribeTo(_top, inputSchema);
					project.setOutputSchema(_outputSchema);
					// project.updateRestrictList();
					_top = project;
				} else {

					try {
						Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateMVProjectionVisitor");
					} catch (ClassNotFoundException e) {
						throw new QueryParseException("Invalid use of multivariate projection -- Missing plug-in!!!.");
					}
					IVisitor v = VisitorFactory.getInstance().getVisitor("ProbabilityPredicate");
					_top = (ILogicalOperator) v.visit(null, null, this);
				}
				_top.setOutputSchema(_outputSchema);
			} else {
				MapAO map = new MapAO();
				map.subscribeTo(_top, inputSchema);
				// all real expressions
				List<NamedExpression> outputExpressions = new ArrayList<NamedExpression>();
				for (SDFExpression expression : expressions) {
					outputExpressions.add(new NamedExpression("", expression,null));
					// outputExpressions.add(new NamedExpressionItem("", new
					// SDFExpression("", expression.getExpressionString(), new
					// DirectAttributeResolver(_outputSchema),
					// MEP.getInstance())));
				}
				map.setExpressions(outputExpressions);
				_top = map;
			}

		}
		RenameAO rename = new RenameAO();
		rename.subscribeTo(_top, _top.getOutputSchema());
		
		rename.setOutputSchema(SDFSchemaFactory.createNewWithAttributes(aliasAttributes, _top.getOutputSchema()));

		_top = rename;
		return _top;
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) throws QueryParseException {
		// select all is simply a * and means all input
		for (SDFAttribute attribute : _top.getOutputSchema().getAttributes()) {
			aliasAttributes.add(attribute);
			outputAttributes.add(attribute);
			SDFExpression expr = new SDFExpression(null, attribute.getURI(), this.attributeResolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			expressions.add(expr);
		}
		return null;
	}

	@Override
	public Object visit(ASTRenamedExpression aliasExpression, Object data) throws QueryParseException {
		Node child = aliasExpression.jjtGetChild(0);
		return child.jjtAccept(this, null);
	}

	@Override
	public Object visit(ASTNumber node, Object data) throws QueryParseException {
		// ASTString should always be a constant number and must have an alias
		// it is either a double (with dot) or a long
		ASTExpression expression = (ASTExpression) data;
		ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
		if (!aliasExpression.hasAlias()) {
			throw new IllegalArgumentException("Missing alias identifier in SELECT-clause for expression " + node.toString());
		} else {
			mathematicalExpressionNeeded = true;
			SDFDatatype datatype = SDFDatatype.LONG;
			IMepConstant<? extends Number> constant = null;
			if (node.getValue().contains(".")) {
				datatype = SDFDatatype.DOUBLE;
				constant = MEP.createConstant(Double.parseDouble(node.getValue()), datatype);
			} else {
				constant = MEP.createConstant(Long.parseLong(node.getValue()), datatype);
			}
			SDFExpression exp = new SDFExpression(constant, this.attributeResolver, MEP.getInstance(), aliasExpression.getAlias());
			expressions.add(exp);
			SDFAttribute attribute = new SDFAttribute(null, aliasExpression.getAlias(), datatype, null, null, null);
			outputAttributes.add(attribute);
			aliasAttributes.add(attribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTExpression expression, Object data) throws QueryParseException {
		if (expression.getOperator() != null) {
			// we have something like a + b, so we need an alias name
			if (!(expression.jjtGetParent() instanceof ASTRenamedExpression)) {
				throw new IllegalArgumentException("Missing alias identifier in SELECT-clause for expression " + expression.toString());
			}
			ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
			if (!aliasExpression.hasAlias()) {
				throw new IllegalArgumentException("Missing alias identifier in SELECT-clause for expression " + expression.toString());
			} else {
				mathematicalExpressionNeeded = true;
				SDFExpression expr = new SDFExpression(null, expression.toString(), this.attributeResolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
				expressions.add(expr);
				SDFDatatype type = expr.getMEPExpression().getReturnType();
				SDFAttribute attribute = new SDFAttribute(null, aliasExpression.getAlias(), type, null, null, null);
				outputAttributes.add(attribute);
				aliasAttributes.add(attribute);
			}
		} else {
			expression.childrenAccept(this, expression);
		}
		return null;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) throws QueryParseException {
		// ASTFunctionExpression is a math-function that could be completely
		// passed to MEP
		ASTExpression expression = (ASTExpression) data;
		ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
		if (!aliasExpression.hasAlias()) {
			throw new IllegalArgumentException("Missing alias identifier in SELECT-clause for expression " + node.toString());
		} else {
			mathematicalExpressionNeeded = true;
			SDFExpression expr = new SDFExpression(null, expression.toString(), this.attributeResolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			expressions.add(expr);
			SDFDatatype type = expr.getMEPExpression().getReturnType();
			SDFAttribute attribute = new SDFAttribute(null, aliasExpression.getAlias(), type, null, null, null);
			outputAttributes.add(attribute);
			aliasAttributes.add(attribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) throws QueryParseException {
		// ASTIdentifier is either "sourcename.*" or just an attribute name
		// (sometimes with alias)
		ASTExpression expression = (ASTExpression) data;
		String name = node.getName();
		if (name.endsWith(".*")) {
			String[] splits = name.split("\\.");
			String sourceName = splits[0];
			ILogicalOperator source = this.attributeResolver.getSource(sourceName);
			if (source == null) {
				// try, if it is the name of the original source
				source = this.attributeResolver.getOriginalSource(sourceName);
				if (source == null) {
					throw new IllegalArgumentException("No such source: " + sourceName);
				}
			}
			List<SDFAttribute> liste = source.getOutputSchema().getAttributes();
			for (SDFAttribute attribute : liste) {
				outputAttributes.add(attribute);
				aliasAttributes.add(attribute);
				SDFExpression expr = new SDFExpression(null, attribute.getURI(), this.attributeResolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
				expressions.add(expr);
			}
		} else {
			SDFAttribute attribute = this.attributeResolver.getAttribute(node.getName());
			ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
			outputAttributes.add(attribute);
			// if it has an alias, we use a clone with new alias name
			if (aliasExpression.hasAlias()) {
				SDFAttribute aliasAttribute = attribute.clone(null, aliasExpression.getAlias());
				aliasAttributes.add(aliasAttribute);
			} else {
				// otherwise we use the normal attribute
				aliasAttributes.add(attribute);
			}
			SDFExpression expr = new SDFExpression(null, attribute.getURI(), this.attributeResolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			expressions.add(expr);
		}
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) throws QueryParseException {
		// ASTString should always be a constant string and must have an alias
		ASTExpression expression = (ASTExpression) data;
		ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
		if (!aliasExpression.hasAlias()) {
			throw new IllegalArgumentException("Missing alias identifier in SELECT-clause for expression " + node.toString());
		} else {
			mathematicalExpressionNeeded = true;
			SDFDatatype datatype = SDFDatatype.STRING;
			SDFExpression exp = new SDFExpression(MEP.createConstant(node.getValue(), datatype), this.attributeResolver, MEP.getInstance(), aliasExpression.getAlias());
			expressions.add(exp);
			SDFAttribute attribute = new SDFAttribute(null, aliasExpression.getAlias(), datatype, null, null, null);
			outputAttributes.add(attribute);
			aliasAttributes.add(attribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) throws QueryParseException {
		ASTExpression expression = (ASTExpression) data;
		ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
		String name = node.jjtGetChild(1).toString();
		String aggregateName = node.jjtGetChild(0).toString();
		SDFAttribute attribute = this.attributeResolver.getAggregateAttribute(name, aggregateName);
		outputAttributes.add(attribute);
		SDFExpression exp = new SDFExpression(null, attribute.getURI(), this.attributeResolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		expressions.add(exp);
		// if it has an alias, we use a clone with new alias name
		if (aliasExpression.hasAlias()) {
			SDFAttribute aliasAttribute = attribute.clone(null, aliasExpression.getAlias());
			aliasAttributes.add(aliasAttribute);
		} else {
			// otherwise we use the normal attribute
			aliasAttributes.add(attribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTProjectionMatrix node, Object data) throws QueryParseException {
		this.projectionMatrix = (double[][]) node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) throws QueryParseException {
		this.projectionVector = ((double[][]) node.childrenAccept(this, null))[0];
		return null;
	}

	@Override
	public Object visit(ASTMatrixExpression matrixExpr, Object data) throws QueryParseException {
		ArrayList<?> rows = matrixExpr.getMatrix();
		projectionMatrix = new double[rows.size()][((ArrayList<?>) rows.get(0)).size()];
		for (int u = 0; u < rows.size(); u++) {
			for (int v = 0; v < ((ArrayList<?>) rows.get(u)).size(); v++) {
				projectionMatrix[u][v] = (Double) ((ArrayList<?>) rows.get(u)).get(v);
			}
		}
		return projectionMatrix;
	}

	/**
	 * checks wether outputschema contains ambigious identifiers
	 */
	private static void checkAttributes(List<SDFAttribute> aliasSchema) {
		for (SDFAttribute attribute : aliasSchema) {
			if (Collections.frequency(aliasSchema, attribute) != 1) {
				throw new IllegalArgumentException("ambiguous attribute: " + attribute);
			}
		}
	}

	public ILogicalOperator getTop() {
		return _top;
	}

	public SDFSchema getOutputSchema() {
		return _outputSchema;
	}

	public SDFSchema getAliasSchema() {
		return _aliasSchema;
	}

	public double[][] getProjectionMatrix() {
		return projectionMatrix;
	}

	public double[] getProjectionVector() {
		return projectionVector;
	}
}
