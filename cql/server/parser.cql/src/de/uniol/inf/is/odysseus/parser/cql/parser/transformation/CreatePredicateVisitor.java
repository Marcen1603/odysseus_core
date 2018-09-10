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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTNotPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTOrPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProbabilityPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTQuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.AbstractQuantificationPredicate;

@SuppressWarnings("unchecked")
public class CreatePredicateVisitor extends AbstractDefaultVisitor {

	@Override
	public Object visit(ASTPredicate node, Object data) throws QueryParseException {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) throws QueryParseException {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) throws QueryParseException {
		SDFExpression expression = new SDFExpression("", node.getPredicate(), (IAttributeResolver) data, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
		RelationalExpression<IMetaAttribute> rp = new RelationalExpression<>(expression);
		return rp;
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) throws QueryParseException {
		IPredicate<? super Tuple<?>> left = (IPredicate<? super Tuple<?>>) node.jjtGetChild(0).jjtAccept(this, data);
		IPredicate<? super Tuple<?>> right = (IPredicate<? super Tuple<?>>) node.jjtGetChild(1).jjtAccept(this, data);
		return ComplexPredicateHelper.createAndPredicate(left, right);
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) throws QueryParseException {
		IPredicate<? super Tuple<?>> left = (IPredicate<? super Tuple<?>>) node.jjtGetChild(0).jjtAccept(this, data);
		IPredicate<? super Tuple<?>> right = (IPredicate<? super Tuple<?>>) node.jjtGetChild(1).jjtAccept(this, data);
		return ComplexPredicateHelper.createOrPredicate(left, right);
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) throws QueryParseException {
		IPredicate<Tuple<?>> predicate = (IPredicate<Tuple<?>>) node.jjtGetChild(0).jjtAccept(this, data);
		return ComplexPredicateHelper.createNotPredicate(predicate);
	}

	@Override
	public Object visit(ASTQuantificationPredicate node, Object data) throws QueryParseException {
		return new QuantificationPredicate((AbstractQuantificationPredicate) node.jjtGetChild(0));
	}

	@Override
	public Object visit(ASTProbabilityPredicate probPred, Object data) throws QueryParseException {
		try {
			Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateMVProjectionVisitor");
		} catch (Exception e) {
			throw new QueryParseException("invalid use of probability predicates - missing plugin");
		}

		IVisitor v = VisitorFactory.getInstance().getVisitor("ProbabilityPredicate");
		return v.visit(probPred, null, null);
	}

	public static IPredicate<Tuple<?>> toPredicate(ASTPredicate predicate, IAttributeResolver resolver) throws QueryParseException {
		IPredicate<Tuple<?>> retVal = (IPredicate<Tuple<?>>) new CreatePredicateVisitor().visit(predicate, resolver);
		return retVal;
	}

}
