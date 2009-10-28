package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.nfunk.jep.ParseException;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.objecttracking.logicaloperator.ProjectMVAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMatrixExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProjectionMatrix;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProjectionVector;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRenamedExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectAll;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * @author Jonas Jacobi
 */
public class CreateProjectionVisitor extends AbstractDefaultVisitor {
	private AttributeResolver attributeResolver;

	private ILogicalOperator top;

	private List<SDFExpression> expressions = new ArrayList<SDFExpression>();
	private SDFAttributeList outputSchema = new SDFAttributeList();
	private SDFAttributeList aliasSchema = new SDFAttributeList();

	double[][] projectionMatrix = null;
	double[] projectionVector = null;

	public CreateProjectionVisitor() {
	}

	// TODO kompletten visitor draus machen, ohne diese methode
	public AbstractLogicalOperator createProjection(
			ASTSelectStatement statement, ILogicalOperator top,
			AttributeResolver attributeResolver) {
		this.top = top;
		SimpleNode node = (SimpleNode) statement.jjtGetChild(0);
		this.attributeResolver = attributeResolver;
		SDFAttributeList inputSchema = top.getOutputSchema();

		// create output schema
		node.childrenAccept(this, null);
		// validate output schema
		checkAttributes(aliasSchema);

		// create operator: if there is a need for a project/map operator,
		// create one and set top operator to it
		if (!outputSchema.equals(inputSchema) || projectionMatrix != null) {
			// if there are no mathematical expressions, a simple ProjectAO will
			// do, otherwise a Map operator is needed
			if (expressions.isEmpty()) {
				// createrestrictlist
				
				if(projectionMatrix == null){
					ProjectAO project = new ProjectAO();
					project.subscribeTo(top);
					project.setInputSchema(inputSchema);
					project.setOutputSchema(outputSchema);
					//project.updateRestrictList();
					top = project;
				}
				else{
					ProjectAO project = createMVProjection(inputSchema);
					top = project;
				}
			} else {
				MapAO map = new MapAO();
				map.subscribeTo(top);
				List<SDFExpression> outputExpressions = new ArrayList<SDFExpression>(
						outputSchema.size());

				Iterator<SDFExpression> exprIt = this.expressions.iterator();
				for (SDFAttribute attr : outputSchema) {
					if (inputSchema.contains(attr)) {
						outputExpressions.add(new SDFExpression(attr));
					} else {
						// mathematical expressions were added in the order of
						// their occurence, so whenever an outputattribute is
						// not found in the inputschema we add the next
						// expression to the output
						outputExpressions.add(exprIt.next());
					}
				}
				map.setExpressions(outputExpressions);
				top = map;
			}
			top.setOutputSchema(outputSchema);
		}
		RenameAO rename = new RenameAO();
		rename.subscribeTo(top);
		rename.setInputSchema(top.getOutputSchema());
		rename.setOutputSchema(this.aliasSchema);

		return rename;
	}
	
	public ProjectMVAO createMVProjection(SDFAttributeList inputSchema){
		ProjectMVAO project = new ProjectMVAO();
		project.subscribeTo(top);
		project.setInputSchema(inputSchema);
		project.setOutputSchema(outputSchema);
		//project.updateRestrictList();
		// cannot be done if a MapAO is used, so it must be done
		// here
		project.setProjectMatrix(this.projectionMatrix);
		project.setProjectVector(this.projectionVector);
		
		return project;
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) {
		outputSchema = top.getOutputSchema();
		for (SDFAttribute attribute : top.getOutputSchema()) {
			CQLAttribute cqlAttribute = (CQLAttribute) attribute;
			if (cqlAttribute.getSourceName() != null) {
				CQLAttribute newAttribute = cqlAttribute.clone();
				newAttribute.setSourceName(null);
				aliasSchema.add(newAttribute);
			} else {
				aliasSchema.add(cqlAttribute);
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTRenamedExpression aliasExpression, Object data) {
		ASTExpression curNode = (ASTExpression) aliasExpression.jjtGetChild(0);
		return curNode.jjtAccept(this, null);
	}

	@Override
	public Object visit(ASTExpression expression, Object data) {
		ASTRenamedExpression aliasExpression = (ASTRenamedExpression) expression
				.jjtGetParent();
		Node node = getNode(expression);

		// node is null, if the expression is something more complex
		// than a simple Identifier or AggregateExpression.
		// Identifier and AggregateExpressions can be used in a simple
		// projection that just returns a subset of its inputs. Everything
		// else needs to be calculated in a Map operator.
		if (node == null || node instanceof ASTFunctionExpression) {
			if (!aliasExpression.hasAlias()) {
				throw new IllegalArgumentException(
						"Missing alias identifier in SELECT-clause for expression "
								+ expression.toString());
			}
			try {
				expressions.add(new SDFExpression(null, expression.toString(),
						this.attributeResolver));
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
			CQLAttribute attribute = new CQLAttribute(null, aliasExpression
					.getAlias());
			attribute.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
			outputSchema.add(attribute);
			aliasSchema.add(attribute);
		} else {
			CQLAttribute attribute = (CQLAttribute) node.jjtAccept(this, null);
			outputSchema.add(attribute);
			CQLAttribute aliasAttribute;
			if (aliasExpression.hasAlias()) {
				// copy other attributes like datatypes
				aliasAttribute = attribute.clone();
				aliasAttribute.setSourceName("");
				aliasAttribute.setAttributeName(aliasExpression.getAlias());
			} else {
				aliasAttribute = attribute;
			}
			aliasSchema.add(aliasAttribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return this.attributeResolver.getAttribute(node.getName());
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) {
		return this.attributeResolver.getAggregateAttribute(node);
	}

	@Override
	public Object visit(ASTProjectionMatrix node, Object data) {
		this.projectionMatrix = (double[][]) node.childrenAccept(this, null);
		return null;
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) {
		this.projectionVector = ((double[][]) node.childrenAccept(this, null))[0];
		return null;
	}

	@Override
	public Object visit(ASTMatrixExpression matrixExpr, Object data) {
		ArrayList<?> rows = matrixExpr.getMatrix();
		projectionMatrix = new double[rows.size()][((ArrayList<?>) rows.get(0))
				.size()];
		for (int u = 0; u < rows.size(); u++) {
			for (int v = 0; v < ((ArrayList<?>) rows.get(u)).size(); v++) {
				projectionMatrix[u][v] = (Double) ((ArrayList<?>) rows.get(u))
						.get(v);
			}
		}
		return projectionMatrix;
	}

	/**
	 * checks wether outputschema contains ambigious identifiers
	 */
	private void checkAttributes(SDFAttributeList outputSchema) {
		for (SDFAttribute attribute : aliasSchema) {
			if (Collections.frequency(aliasSchema, attribute) != 1) {
				throw new IllegalArgumentException("ambigious attribute: "
						+ attribute);
			}
		}
	}
}
