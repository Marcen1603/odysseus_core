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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
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
	List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();

	private SDFSchema _aliasSchema = null;
	List<SDFAttribute> aliasAttributes = new ArrayList<SDFAttribute>();

	double[][] projectionMatrix = null;

	double[] projectionVector = null;


	public CreateProjectionVisitor(ILogicalOperator top, AttributeResolver attributeResolver) {
		this._top = top;
		this.attributeResolver = attributeResolver;
	}

	// TODO: This is the only valid Entry point ... how can the other elements be set to private?? 
	public Object visit(ASTSelectStatement statement, Object data) throws QueryParseException {

		SimpleNode node = (SimpleNode) statement.jjtGetChild(0);

		// create output schema (with different visit-Methods)
		node.childrenAccept(this, null);
		
		// validate output schema
		checkAttributes(aliasAttributes);

		SDFSchema inputSchema = _top.getOutputSchema();
		_outputSchema = new SDFSchema(inputSchema.getURI(), outputAttributes);
		// create operator: if there is a need for a project/map operator,
		// create one and set top operator to it
		if (!_outputSchema.equals(inputSchema) || projectionMatrix != null) {
			// if there are no mathematical expressions, a simple ProjectAO will
			// do, otherwise a Map operator is needed
			if (expressions.isEmpty()) {
				// createrestrictlist

				if (projectionMatrix == null) {
					ProjectAO project = new ProjectAO();
					project.subscribeTo(_top, inputSchema);
					project.setOutputSchema(_outputSchema);
					// project.updateRestrictList();
					_top = project;
				} else {

					// TODO: Behandlung, wenn kein Visitor gefunden wird
					try {
						Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateMVProjectionVisitor");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						throw new QueryParseException("Invalid use of multivariate projection -- Missing plug-in!!!.");
					}
					IVisitor v = VisitorFactory.getInstance().getVisitor("ProbabilityPredicate");
					_top = (ILogicalOperator) v.visit(null, null, this);
				}
				((OutputSchemaSettable) _top).setOutputSchema(_outputSchema);
			} else {
				MapAO map = new MapAO();
				map.subscribeTo(_top, inputSchema);
				
				
				List<SDFExpression> outputExpressions = new ArrayList<SDFExpression>(expressions);
				map.setExpressions(outputExpressions);
				
				//
//				Iterator<SDFExpression> exprIt = this.expressions.iterator();
//				
//				for (SDFAttribute attr : outputSchema) {
//					if (inputSchema.contains(attr)) {
//						outputExpressions.add(new SDFExpression(attr));
//					} else {
//						// mathematical expressions were added in the order of
//						// their occurence, so whenever an outputattribute is
//						// not found in the inputschema we add the next
//						// expression to the output
//						outputExpressions.add(exprIt.next());
//					}
//				}
//				map.setExpressions(outputExpressions);
				_top = map;
			}

		}
		RenameAO rename = new RenameAO();
		rename.subscribeTo(_top, _top.getOutputSchema());
		rename.setOutputSchema(new SDFSchema(_top.getOutputSchema().getURI(), this.aliasAttributes));

		_top = rename;
		return _top;
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) throws QueryParseException {
		outputAttributes = _top.getOutputSchema().getAttributes();
		aliasAttributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : outputAttributes) {
			SDFAttribute attr = (SDFAttribute) attribute;
			if (attr.getSourceName() != null) {
				// Create new Attribute without sourcepart
				SDFAttribute newAttribute = attr.clone(null, attr.getAttributeName());
				aliasAttributes.add(newAttribute);
			} else {
				aliasAttributes.add(attr);
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTRenamedExpression aliasExpression, Object data) throws QueryParseException {
		ASTExpression curNode = (ASTExpression) aliasExpression.jjtGetChild(0);
		return curNode.jjtAccept(this, null);
	}

	@Override
	public Object visit(ASTExpression expression, Object data) throws QueryParseException {
		ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression.jjtGetParent();
		Node node = getNode(expression);

		// node is null, if the expression is something more complex
		// than a simple Identifier or AggregateExpression.
		// Identifier and AggregateExpressions can be used in a simple
		// projection that just returns a subset of its inputs. Everything
		// else needs to be calculated in a Map operator.
		if (node == null || node instanceof ASTFunctionExpression || node instanceof ASTNumber) {
			if (!aliasExpression.hasAlias()) {
				throw new IllegalArgumentException("Missing alias identifier in SELECT-clause for expression " + expression.toString());
			}
			expressions.add(new SDFExpression(null, expression.toString(), this.attributeResolver, MEP.getInstance()));
			SDFAttribute attribute = null;
			if (node instanceof ASTNumber) {
				ASTNumber number = (ASTNumber) node;
				if (number.getValue().contains(".")) {
					attribute = new SDFAttribute(null, aliasExpression.getAlias(),SDFDatatype.DOUBLE);
				} else {
					attribute = new SDFAttribute(null, aliasExpression.getAlias(),SDFDatatype.LONG);
				}
			} else {
				attribute = new SDFAttribute(null, aliasExpression.getAlias(),SDFDatatype.DOUBLE);
			}
			outputAttributes.add(attribute);
			aliasAttributes.add(attribute);
		} else {
			SDFAttribute attribute = (SDFAttribute) node.jjtAccept(this, null);
			outputAttributes.add(attribute);
			SDFAttribute aliasAttribute;
			if (aliasExpression.hasAlias()) {
				// copy other attributes like datatypes
				aliasAttribute = attribute.clone(null, aliasExpression.getAlias());
//				aliasAttribute.setSourceName("");
//				aliasAttribute.setAttributeName(aliasExpression.getAlias());
			} else {
				aliasAttribute = attribute;
			}
			aliasAttributes.add(aliasAttribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) throws QueryParseException {
		return this.attributeResolver.getAttribute(node.getName());
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) throws QueryParseException {
		String name = node.jjtGetChild(1).toString();
		String aggregateName = node.jjtGetChild(0).toString();
		return this.attributeResolver.getAggregateAttribute(name, aggregateName);
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
	private void checkAttributes(List<SDFAttribute> aliasSchema) {
		for (SDFAttribute attribute : aliasSchema) {
			if (Collections.frequency(aliasSchema, attribute) != 1) {
				throw new IllegalArgumentException("ambigious attribute: " + attribute);
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
