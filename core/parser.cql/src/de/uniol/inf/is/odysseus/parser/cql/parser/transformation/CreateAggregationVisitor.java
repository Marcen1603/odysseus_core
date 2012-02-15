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

import de.uniol.inf.is.odysseus.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAS;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class CreateAggregationVisitor extends AbstractDefaultVisitor {

	private AttributeResolver attributeResolver;

	private AggregateAO ao;

	private SelectAO select;

	private ILogicalOperator top;

	private boolean hasGrouping;
	

	public CreateAggregationVisitor(){
	}
	
	public void init(ILogicalOperator top, AttributeResolver attributeResolver) {
		ao = new AggregateAO();
		this.top = top;
		ao.subscribeToSource(top,0,0,top.getOutputSchema());
		select = null;
		this.attributeResolver = attributeResolver;
		this.hasGrouping = false;
	}

	@Override
	public Object visit(ASTExpression node, Object data) throws QueryParseException {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) throws QueryParseException {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) throws QueryParseException {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAggregateExpression aggrNode, Object data) throws QueryParseException {
		AggregateFunction function = aggrNode.getFunction();
		String attributeName = aggrNode.getAttributeName();
		SDFAttribute attribute = this.attributeResolver
				.getAttribute(attributeName);

		SDFAttribute outAttribute = outAttribute(attribute.toPointString(),
				function, aggrNode);
		if (!ao.getOutputSchema().contains(outAttribute)) {
			ao.addAggregation(attribute, function, outAttribute);
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) throws QueryParseException {
		SDFAttribute attribute = this.attributeResolver.getAttribute(node
				.getName());
		if (attribute == null) {
			throw new IllegalArgumentException("no such attribute: "
					+ node.getName());
		}
		ao.addGroupingAttribute(attribute);
		return null;
	}

	@Override
	public Object visit(ASTAS node, Object data) throws QueryParseException {
		return data;
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) throws QueryParseException {
		node.childrenAccept(this, data);
		return ao;
	}

	private SDFAttribute outAttribute(String attributeName,
			AggregateFunction function, ASTAggregateExpression node) {
		// TODO: Anpassen ... Warum haben die AggregateFunctions nicht den Typen?
		String funcName = function.toString() + "(" + attributeName + ")";
		SDFAttribute attribute = this.attributeResolver.getAttribute(funcName);
		if (attribute == null) {
			SDFDatatype datatype = this.attributeResolver.getAttribute(
					attributeName).getDatatype();
			if (!datatype.isNumeric() && !function.getName().equalsIgnoreCase("COUNT")) {
				throw new IllegalArgumentException("function '"
						+ function.toString()
						+ "' can't be used on non scalar types");
			}
			
			if (function.getName().equalsIgnoreCase("AVG")) {
				attribute = new SDFAttribute(null, funcName, SDFDatatype.DOUBLE);
			} else if (function.getName().equalsIgnoreCase("COUNT")) {
				attribute = new SDFAttribute(null, funcName, SDFDatatype.INTEGER);
			} else {
				// datatype equals datatype of input attribute
				// for other functions
				attribute = new SDFAttribute(null, funcName,datatype);
			}

			this.attributeResolver.addAttribute(attribute);
		}
		return attribute;
	}


	@Override
	public Object visit(ASTGroupByClause node, Object data) throws QueryParseException {
		this.hasGrouping = true;
		return data;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) throws QueryParseException {
		return data;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) throws QueryParseException {
		return data;
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) throws QueryParseException {
		select = new SelectAO();
		select.subscribeToSource(ao,0,0,ao.getOutputSchema());
		IPredicate<RelationalTuple<?>> predicate;
		predicate = CreatePredicateVisitor.toPredicate((ASTPredicate) node
				.jjtGetChild(0), this.attributeResolver);
		select.setPredicate(predicate);
		return select;
	}

	public ILogicalOperator getResult() {
		// remove possible subscriptions!!
		if (this.select != null) {
			for (LogicalSubscription l: select.getSubscriptions()){				
				select.unsubscribeSink(l.getTarget(), l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
			}
			return this.select;
		}
		if (this.ao.hasAggregations() || hasGrouping ){
			for (LogicalSubscription l: ao.getSubscriptions()){				
				ao.unsubscribeSink(l.getTarget(), l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
			}
			return this.ao;
		}else{
			for (LogicalSubscription l: top.getSubscriptions()){				
				top.unsubscribeSink(l.getTarget(), l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
			}
			return top;
		}
			
	}
}
