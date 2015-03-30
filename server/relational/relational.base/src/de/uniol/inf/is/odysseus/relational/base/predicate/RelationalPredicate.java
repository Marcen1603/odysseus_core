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
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalPredicate extends AbstractRelationalPredicate<Tuple<?>> {

	Logger logger = LoggerFactory.getLogger(RelationalPredicate.class);

	private static final long serialVersionUID = 1222104352250883947L;

	// protected SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	// protected int[] attributePositions;

	// final List<SDFAttribute> neededAttributes;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	// protected boolean[] fromRightChannel;

	// protected Map<SDFAttribute, SDFAttribute> replacementMap = new
	// HashMap<SDFAttribute, SDFAttribute>();

	// protected SDFSchema leftSchema;
	// protected SDFSchema rightSchema;

	public RelationalPredicate(SDFExpression expression) {
		// this.expression = expression;
		// this.neededAttributes = expression.getAllAttributes();
		super(expression);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<IPredicate> splitPredicate() {
		List<IPredicate> result = new LinkedList<IPredicate>();
		if (isAndPredicate()) {
			Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
			expressionStack.push(expression.getMEPExpression());

			while (!expressionStack.isEmpty()) {
				IExpression<?> curExpression = expressionStack.pop();
				if (isAndExpression(curExpression)) {
					expressionStack.push(curExpression.toFunction()
							.getArgument(0));
					expressionStack.push(curExpression.toFunction()
							.getArgument(1));
				} else {
					SDFExpression expr = new SDFExpression(curExpression,
							expression.getAttributeResolver(),
							MEP.getInstance());
					RelationalPredicate relationalPredicate = new RelationalPredicate(
							expr);
					relationalPredicate.init(expression.getSchema(), false);
					result.add(relationalPredicate);
				}
			}
			return result;

		}
		result.add(this);
		return result;
	}

	// @Override
	// public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
	// init(leftSchema, rightSchema, true);
	// }
	//
	// public void init(SDFSchema leftSchema, SDFSchema rightSchema, boolean
	// checkRightSchema) {
	// //
	// logger.debug("Init ("+this+"): Left "+leftSchema+" Right "+rightSchema);
	// this.leftSchema = leftSchema;
	// this.rightSchema = rightSchema;
	//
	// List<SDFAttribute> neededAttributes = expression.getAllAttributes();
	// this.attributePositions = new int[neededAttributes.size()];
	// this.fromRightChannel = new boolean[neededAttributes.size()];
	//
	// int i = 0;
	// for (SDFAttribute curAttribute : neededAttributes) {
	// if (curAttribute == null) {
	// throw new IllegalArgumentException("Needed attribute for expression " +
	// expression + " may not be null!");
	// }
	// int pos = leftSchema.indexOf(curAttribute);
	// if (pos == -1) {
	// if (rightSchema == null && checkRightSchema) {
	// throw new IllegalArgumentException("Attribute " + curAttribute +
	// " not in " + leftSchema + " and rightSchema is null!");
	// }
	// if (checkRightSchema) {
	// // if you get here, there is an attribute
	// // in the predicate that does not exist
	// // in the left schema, so there must also be
	// // a right schema
	// pos = indexOf(rightSchema, curAttribute);
	// if (pos == -1) {
	// throw new IllegalArgumentException("Attribute " + curAttribute +
	// " not in " + rightSchema);
	// }
	// }
	// this.fromRightChannel[i] = true;
	// }
	// this.attributePositions[i++] = pos;
	// }
	// }
	//
	// private int indexOf(SDFSchema schema, SDFAttribute attr) {
	// SDFAttribute cqlAttr = getReplacement(attr);
	// Iterator<SDFAttribute> it = schema.iterator();
	// for (int i = 0; it.hasNext(); ++i) {
	// SDFAttribute a = it.next();
	// if (cqlAttr.equalsCQL(a)) {
	// return i;
	// }
	// }
	// return -1;
	// }

	// private SDFAttribute getReplacement(SDFAttribute a) {
	// SDFAttribute ret = a;
	// SDFAttribute tmp = null;
	// while ((tmp = replacementMap.get(ret)) != null) {
	// ret = tmp;
	// }
	// return ret;
	// }

	public RelationalPredicate(RelationalPredicate predicate) {
		super(predicate);
		// this.attributePositions = predicate.attributePositions == null ? null
		// : (int[]) predicate.attributePositions.clone();
		// this.fromRightChannel = predicate.fromRightChannel == null ? null :
		// (boolean[]) predicate.fromRightChannel.clone();
		// this.expression = predicate.expression == null ? null :
		// predicate.expression.clone();
		// this.replacementMap = new HashMap<SDFAttribute,
		// SDFAttribute>(predicate.replacementMap);
		// this.neededAttributes = new
		// ArrayList<SDFAttribute>(predicate.neededAttributes);
		// logger.debug("Cloned "+this+ " "+attributePositions);
	}

	@Override
	public boolean evaluate(Tuple<?> input) {
		Object[] values ;
		try {
			values = new Object[this.attributePositions.length];
		} catch (NullPointerException e) {
			if (attributePositions == null) {
				throw new IllegalStateException("The predicate "
						+ this.expression + " is not initialized!");
			}else{
				throw e;
			}
				
		}

		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindMetaAttribute(input.getMetadata());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		// TODO: IMetaAttribute
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
		additionalContent.putAll(left.getAdditionalContent());
		additionalContent.putAll(right.getAdditionalContent());

		// FIXME Merge meta data
		// this.expression.bindMetaAttribute();
		this.expression.bindAdditionalContent(additionalContent);
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	public boolean evaluate(Tuple<?> input, KeyValueObject<?> additional) {
		Object[] values = new Object[neededAttributes.size()];

		for (int i = 0; i < neededAttributes.size(); ++i) {
			if (!fromRightChannel[i]) {
				values[i] = input.getAttribute(this.attributePositions[i]);
			} else {
				values[i] = additional.getAttribute(neededAttributes.get(i)
						.getURI());
			}
		}
		this.expression.bindMetaAttribute(input.getMetadata());
		this.expression.bindAdditionalContent(input.getAdditionalContent());
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public RelationalPredicate clone() {
		return new RelationalPredicate(this);
	}

	// @Override
	// public String toString() {
	// return this.expression.toString();
	// }
	//
	// @Override
	// public List<SDFAttribute> getAttributes() {
	// return Collections.unmodifiableList(this.expression.getAllAttributes());
	// }

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RelationalPredicate)) {
			return false;
		}
		return this.expression.equals(((RelationalPredicate) other).expression);
	}

	@Override
	public int hashCode() {
		return 23 * this.expression.hashCode();
	}

	//
	// @Override
	// public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr)
	// {
	// if (!curAttr.equals(newAttr)) {
	// replacementMap.put(curAttr, newAttr);
	// }
	// else {
	// logger.warn("Replacement " + curAttr + " --> " + newAttr +
	// " not added because they are equal!");
	// }
	// }
	//
	// // nur testweise zum Evaluieren
	// public SDFExpression getExpression() {
	// return expression;
	// }

	// @Override
	// public boolean equals(IPredicate<Tuple<?>> pred) {
	// // Falls die Expressions nicht identisch sind, ist dennoch eine
	// // inhaltliche ���quivalenz m���glich
	// if (!this.equals((Object) pred)) {
	// // boolean isContainedIn1 = this.isContainedIn(pred);
	// // boolean isContainedIn2 = pred.isContainedIn(this);
	// return this.isContainedIn(pred) && pred.isContainedIn(this);
	// }
	// return true;
	// }

	// @Override
	// @SuppressWarnings({ "rawtypes" })
	// public boolean isContainedIn(IPredicate<?> o) {
	// if (!(o instanceof RelationalPredicate)) {
	// return false;
	// }
	// RelationalPredicate rp2 = (RelationalPredicate) o;
	//
	// // Komplexe Pr���dikate
	// // AND
	// if (this.isAndPredicate()) {
	// // Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist
	// // (Zus���tzliche Versch���rfung bestehender
	// Pr���dikate)
	// if (!rp2.isAndPredicate()) {
	// List<IPredicate> spred = splitPredicate(false);
	//
	// for (IPredicate<?> p : spred) {
	// if (p.isContainedIn(o)) {
	// return true;
	// }
	// }
	// return false;
	// }
	// // TODO: Noch mal ���berpr���fen
	// // // Falls es sich beim anderen Pr���dikat ebenfalls um ein
	// // AndPredicate handelt, m���ssen beide Pr���dikate
	// verglichen
	// // werden
	// // (inklusiver aller "Unterpr���dikate")
	// // if(o instanceof AndPredicate) {
	// // AndPredicate<T> ap = (AndPredicate<T>) o;
	// //
	// //
	// //
	// // ArrayList<IPredicate<?>> a = extractAllPredicates(this);
	// // ArrayList<IPredicate<?>> b = extractAllPredicates(ap);
	// //
	// // // F���r JEDES Pr���dikat aus dem anderen AndPredicate
	// muss ein
	// // enthaltenes Pr���dikat in diesem AndPredicate gefunden werden
	// // // (Nur weitere Versch���rfungen sind zul���ssig, deshalb
	// darf
	// // keine
	// // Bedingung des anderen Pr���dikats st���rker sein)
	// // for(IPredicate<?> predb : b) {
	// // // if(predb instanceof OrPredicate) {
	// // // return false;
	// // // }
	// // boolean foundmatch = false;
	// // for(IPredicate<?> preda : a) {
	// // // if(preda instanceof OrPredicate) {
	// // // return false;
	// // // }
	// // if(preda.isContainedIn(predb)) {
	// // foundmatch = true;
	// // }
	// // }
	// // if(!foundmatch) {
	// // return false;
	// // }
	// // }
	// // return true;
	// // }
	// //
	// return false;
	// }
	// // OR
	// // TODO: Aus dem alten OR-Predicate extrahieren
	// if (this.isOrPredicate()) {
	// return false;
	// }
	// // NOT
	// // TODO: Geht das besser?
	// if (this.isNotPredicate()) {
	// if (rp2.isNotPredicate()) {
	// return false;
	// }
	// return false;
	// }
	// // BASIS-Pr���dikat
	// // Unterschiedliche Anzahl Attribute
	// if (this.getAttributes().size() != rp2.getAttributes().size()) {
	// return false;
	// }
	// try {
	// // Noch mal zu parsen scheint mir wenig sinnvoll zu sein ...
	// IExpression<?> ex1 = this.expression.getMEPExpression();//
	// MEP.parse(this.expression.getExpression());
	// IExpression<?> ex2 = rp2.expression.getMEPExpression();//
	// MEP.parse(rp2.getExpression().getExpression());
	// if (ex1.getReturnType().equals(ex2.getReturnType()) && ex1.isFunction())
	// {
	// IFunction<?> if1 = (IFunction<?>) ex1;
	// IFunction<?> if2 = (IFunction<?>) ex2;
	// if (if1.getArity() != 2) {
	// return false;
	// }
	//
	// IExpression<?> firstArgument1 = if1.getArgument(0);
	// IExpression<?> secondArgument1 = if1.getArgument(1);
	// IExpression<?> firstArgument2 = if2.getArgument(0);
	// IExpression<?> secondArgument2 = if2.getArgument(1);
	//
	// String symbol1 = if1.getSymbol();
	// String symbol2 = if2.getSymbol();
	// // Funktionen enthalten nur ein Attribut und ansonsten
	// // Konstanten
	// if (this.getAttributes().size() == 1) {
	//
	// // gleiches Attribut auf der linken Seite, Konstante auf der
	// // rechten
	// if (firstArgument1.isVariable() && secondArgument1.isConstant() &&
	// firstArgument2.isVariable() && secondArgument2.isConstant()
	// && firstArgument1.toVariable().equals(firstArgument2.toVariable())) {
	// Double c1 = Double.parseDouble(secondArgument1.toString());
	// Double c2 = Double.parseDouble(secondArgument2.toString());
	//
	// // Funktion kleiner-als
	// if (symbol1.equals("<") && (symbol2.equals("<") || symbol2.equals("<="))
	// && c1 <= c2) {
	// return true;
	// }
	// // Funktion kleiner-gleich-als
	// if (symbol1.equals("<=") && ((symbol2.equals("<=") && c1 <= c2) ||
	// (symbol2.equals("<") && c1 < c2))) {
	// return true;
	// }
	// // Funktion ist-gleich oder ist-nicht-gleich
	// if (((symbol1.equals("==") && symbol2.equals("==")) ||
	// (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0)
	// {
	// return true;
	// }
	// // Funktion gr������er-als
	// if (symbol1.equals(">") && (symbol2.equals(">") || symbol2.equals(">="))
	// && c1 >= c2) {
	// return true;
	// }
	// // Funktion gr������er-gleich-als
	// if (symbol1.equals(">=") && ((symbol2.equals(">=") && c1 >= c2) ||
	// (symbol2.equals(">") && c1 > c2))) {
	// return true;
	// }
	// // gleiches Attribut auf der rechten Seite, Konstante
	// // auf der linken Seite
	// }
	// else if (secondArgument1.isVariable() && firstArgument1.isConstant() &&
	// secondArgument2.isVariable() && firstArgument2.isConstant()
	// && secondArgument1.toVariable().equals(secondArgument2.toVariable())) {
	//
	// Double c1 = Double.parseDouble(firstArgument1.toString());
	// Double c2 = Double.parseDouble(firstArgument2.toString());
	//
	// // Funktion kleiner-als
	// if (symbol1.equals("<") && (symbol2.equals("<") || symbol2.equals("<="))
	// && c1 >= c2) {
	// return true;
	// }
	// // Funktion kleiner-gleich-als
	// if (symbol1.equals("<=") && ((symbol2.equals("<=") && c1 >= c2) ||
	// (symbol2.equals("<") && c1 > c2))) {
	// return true;
	// }
	// // Funktion ist-gleich oder ist-nicht-gleich
	// if (((symbol1.equals("==") && symbol2.equals("==")) ||
	// (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0)
	// {
	// return true;
	// }
	// // Funktion gr������er-als
	// if (symbol1.equals(">") && (symbol2.equals(">") || symbol2.equals(">="))
	// && c1 <= c2) {
	// return true;
	// }
	// // Funktion gr������er-gleich-als
	// if (symbol1.equals(">=") && ((symbol2.equals(">=") && c1 <= c2) ||
	// (symbol2.equals(">") && c1 < c2))) {
	// return true;
	// }
	// // Attribut bei F1 links, bei F2 rechts
	// }
	// else if (firstArgument1.isVariable() && secondArgument1.isConstant() &&
	// secondArgument2.isVariable() && firstArgument2.isConstant()
	// && firstArgument1.toVariable().equals(secondArgument2.toVariable())) {
	// Double c1 = Double.parseDouble(secondArgument1.toString());
	// Double c2 = Double.parseDouble(firstArgument2.toString());
	//
	// // F1 kleiner-als, F2 gr������er-als oder
	// // gr������er-gleich-als
	// if (symbol1.equals("<") && (symbol2.equals(">") || symbol2.equals(">="))
	// && c1 <= c2) {
	// return true;
	// }
	// // F1 kleiner-gleich-als, F2 gr������er-als oder
	// // gr������er-gleich-als
	// if (symbol1.equals("<=") && (symbol2.equals(">") && c1 < c2) ||
	// (symbol2.equals(">=")) && c1 <= c2) {
	// return true;
	// }
	// // Funktion ist-gleich oder ist-nicht-gleich
	// if (((symbol1.equals("==") && symbol2.equals("==")) ||
	// (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0)
	// {
	// return true;
	// }
	// // F1 gr������er-als, F2 kleiner-als oder
	// // kleiner-gleich-als
	// if (symbol1.equals(">") && (symbol2.equals("<") || symbol2.equals("<="))
	// && c1 >= c2) {
	// return true;
	// }
	// // F1 gr������er-gleich-als, F2 kleiner-als oder
	// // kleiner-gleich-als
	// if (symbol1.equals(">=") && (symbol2.equals("<") && c1 > c2) ||
	// (symbol2.equals("<=")) && c1 >= c2) {
	// return true;
	// }
	//
	// // Attribut bei F1 rechts, bei F2 links
	// }
	// else if (secondArgument1.isVariable() && firstArgument1.isConstant() &&
	// firstArgument2.isVariable() && secondArgument2.isConstant()
	// && secondArgument1.toVariable().equals(firstArgument2.toVariable())) {
	//
	// Double c1 = Double.parseDouble(firstArgument1.toString());
	// Double c2 = Double.parseDouble(secondArgument2.toString());
	//
	// // F1 kleiner-als, F2 gr������er-als oder
	// // gr������er-gleich-als
	// if (symbol1.equals("<") && (symbol2.equals(">") || symbol2.equals(">="))
	// && c1 >= c2) {
	// return true;
	// }
	//
	// // F1 kleiner-gleich-als, F2 gr������er-als oder
	// // gr������er-gleich-als
	// if (symbol1.equals("<=") && (symbol2.equals(">") && c1 > c2) ||
	// (symbol2.equals(">=")) && c1 >= c2) {
	// return true;
	// }
	//
	// // Funktion ist-gleich oder ist-nicht-gleich
	// if (((symbol1.equals("==") && symbol2.equals("==")) ||
	// (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0)
	// {
	// return true;
	// }
	//
	// // F1 gr������er-als, F2 kleiner-als oder
	// // kleiner-gleich-als
	// if (symbol1.equals(">") && (symbol2.equals("<") || symbol2.equals("<="))
	// && c1 <= c2) {
	// return true;
	// }
	//
	// // F1 gr������er-gleich-als, F2 kleiner-als oder
	// // kleiner-gleich-als
	// if (symbol1.equals(">=") && (symbol2.equals("<") && c1 < c2) ||
	// (symbol2.equals("<=")) && c1 <= c2) {
	// return true;
	// }
	// }
	// // Funktionen sind Vergleiche zwischen zwei Attributen
	// }
	// else if (this.getAttributes().size() == 2) {
	// Variable v11 = firstArgument1.toVariable();
	// Variable v12 = secondArgument1.toVariable();
	// Variable v21 = firstArgument2.toVariable();
	// Variable v22 = secondArgument2.toVariable();
	//
	// // Attribute sind links und rechts gleich
	// if (v11.equals(v21) && v12.equals(v22)) {
	// if ((symbol1.equals("<") && symbol2.equals("<")) || (symbol1.equals("<=")
	// && symbol2.equals("<=")) || (symbol1.equals("==") &&
	// symbol2.equals("=="))
	// || (symbol1.equals("!=") && symbol2.equals("!=")) ||
	// (symbol1.equals(">=") && symbol2.equals(">=")) || (symbol1.equals(">") &&
	// symbol2.equals(">"))) {
	// return true;
	// }
	// }
	// // linkes Attribut von F1 ist gleich rechtem Attribut von F2
	// // und umgekehrt
	// if (v11.equals(v22) && v12.equals(v21)) {
	// if ((symbol1.equals("<") && symbol2.equals(">")) || (symbol1.equals("<=")
	// && symbol2.equals(">=")) || (symbol1.equals("==") &&
	// symbol2.equals("=="))
	// || (symbol1.equals("!=") && symbol2.equals("!=")) ||
	// (symbol1.equals(">=") && symbol2.equals("<=")) || (symbol1.equals(">") &&
	// symbol2.equals("<"))) {
	// return true;
	// }
	// }
	// }
	//
	// }
	//
	// }
	// catch (Exception e) {
	// e.printStackTrace();
	// }
	// return false;
	// }
	//
	// // Helpermethods
	// public boolean isAndPredicate() {
	// return isAndPredicate(expression.getMEPExpression());
	// }
	//
	// static boolean isAndPredicate(IExpression<?> iExpression) {
	// return iExpression.isFunction() && iExpression.toFunction() instanceof
	// AndOperator;
	// }
	//
	// public boolean isOrPredicate() {
	// return expression.getMEPExpression().isFunction() &&
	// expression.getMEPExpression().toFunction() instanceof OrOperator;
	// }
	//
	// public boolean isNotPredicate() {
	// return expression.getMEPExpression().isFunction() &&
	// expression.getMEPExpression().toFunction() instanceof NotOperator;
	// }

	// @SuppressWarnings("rawtypes")
	// @Override
	// public List<IPredicate> conjunctiveSplit(boolean init) {
	// return splitPredicate(init);
	// }
	//
	// /**
	// * Returns List of conjunctive predicates
	// *
	// * @return
	// */
	// @SuppressWarnings("rawtypes")
	// public List<IPredicate> splitPredicate(boolean init) {
	// List<IPredicate> result = new LinkedList<IPredicate>();
	// if (isAndPredicate()) {
	// Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
	// expressionStack.push(expression.getMEPExpression());
	//
	// while (!expressionStack.isEmpty()) {
	// IExpression<?> curExpression = expressionStack.pop();
	// if (isAndPredicate(curExpression)) {
	// expressionStack.push(curExpression.toFunction().getArgument(0));
	// expressionStack.push(curExpression.toFunction().getArgument(1));
	// }
	// else {
	// SDFExpression expr = new SDFExpression(curExpression,
	// expression.getAttributeResolver(), MEP.getInstance());
	// RelationalPredicate relationalPredicate = new RelationalPredicate(expr);
	// relationalPredicate.init(expression.getSchema(), null, false);
	// result.add(relationalPredicate);
	// }
	// }
	// return result;
	//
	// }
	// result.add(this);
	// return result;
	// }

	@Override
	public boolean isAlwaysTrue() {
		return expression.isAlwaysTrue();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<Tuple<?>> and(IPredicate<Tuple<?>> predicate) {
        if (predicate instanceof RelationalPredicate) {
            SDFExpression expr = ((RelationalPredicate) predicate).expression;
            AndOperator and = new AndOperator();
            and.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
            RelationalPredicate andPredicate = new RelationalPredicate(new SDFExpression(and.toString(), MEP.getInstance()));
            return andPredicate;
        }
        return super.and(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<Tuple<?>> or(IPredicate<Tuple<?>> predicate) {
        if (predicate instanceof RelationalPredicate) {
            SDFExpression expr = ((RelationalPredicate) predicate).expression;
            OrOperator or = new OrOperator();
            or.setArguments(new IExpression<?>[] { expression.getMEPExpression(), expr.getMEPExpression() });
            // We need to reparse the expression because of multiple instances
            // of the same variable may exist
            RelationalPredicate orPredicate = new RelationalPredicate(new SDFExpression(or.toString(), MEP.getInstance()));
            return orPredicate;
        }
        return super.or(predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<Tuple<?>> not() {
        NotOperator not = new NotOperator();
        not.setArguments(new IExpression<?>[] { expression.getMEPExpression() });
        RelationalPredicate notPredicate = new RelationalPredicate(new SDFExpression(not, expression.getAttributeResolver(), MEP.getInstance()));
        return notPredicate;
    }

	public static void main(String[] args) {
		SDFAttribute a = new SDFAttribute("", "p_out", SDFDatatype.DOUBLE,
				null, null, null);
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("", a);
		RelationalPredicate pred = new RelationalPredicate(new SDFExpression(
				"p_out <=0 || isNaN(p_out)", MEP.getInstance()));

		System.out.println(pred.toString());
		pred.init(schema, null, false);
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, 8);
		KeyValueObject<IMetaAttribute> additional = new KeyValueObject<IMetaAttribute>();
		additional.setAttribute("b", 5);
		System.out.println(pred.evaluate(tuple, additional));
	}

}
