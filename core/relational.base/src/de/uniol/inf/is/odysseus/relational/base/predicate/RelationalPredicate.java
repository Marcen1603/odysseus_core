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
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NotOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.OrOperator;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalPredicate extends AbstractPredicate<Tuple<?>>
		implements IRelationalPredicate {

	private static final long serialVersionUID = 1222104352250883947L;

	protected SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	protected int[] attributePositions;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	protected boolean[] fromRightChannel;

	protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	protected SDFSchema leftSchema;
	protected SDFSchema rightSchema;

	public RelationalPredicate(SDFExpression expression) {
		this.expression = expression;
	}

	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		this.leftSchema = leftSchema;
		this.rightSchema = rightSchema;

		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		this.attributePositions = new int[neededAttributes.size()];
		this.fromRightChannel = new boolean[neededAttributes.size()];

		int i = 0;
		for (SDFAttribute curAttribute : neededAttributes) {

			int pos = indexOf(leftSchema, curAttribute);
			if (pos == -1) {
				if (rightSchema == null) {
					throw new IllegalArgumentException("Attribute "
							+ curAttribute + " not in " + leftSchema
							+ " and rightSchema is null!");
				}
				// if you get here, there is an attribute
				// in the predicate that does not exist
				// in the left schema, so there must also be
				// a right schema
				pos = indexOf(rightSchema, curAttribute);
				if (pos == -1) {
					throw new IllegalArgumentException("Attribute "
							+ curAttribute + " not in " + rightSchema);
				}
				this.fromRightChannel[i] = true;
			}
			this.attributePositions[i++] = pos;
		}
	}

	private int indexOf(SDFSchema schema, SDFAttribute attr) {
		SDFAttribute cqlAttr = getReplacement(attr);
		Iterator<SDFAttribute> it = schema.iterator();
		for (int i = 0; it.hasNext(); ++i) {
			SDFAttribute a = it.next();
			if (cqlAttr.equalsCQL(a)) {
				return i;
			}
		}
		return -1;
	}

	private SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp = replacementMap.get(ret)) != null) {
			ret = tmp;
		}
		return ret;
	}

	public RelationalPredicate(RelationalPredicate predicate) {
		this.attributePositions = predicate.attributePositions == null ? null
				: (int[]) predicate.attributePositions.clone();
		this.fromRightChannel = predicate.fromRightChannel == null ? null
				: (boolean[]) predicate.fromRightChannel.clone();
		this.expression = predicate.expression == null ? null
				: predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(
				predicate.replacementMap);
	}

	@Override
	public boolean evaluate(Tuple<?> input) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			values[i] = input.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePositions.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			values[i] = r.getAttribute(this.attributePositions[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public RelationalPredicate clone() {
		return new RelationalPredicate(this);
	}

	@Override
	public String toString() {
		return this.expression.toString();
	}

	@Override
	public List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(this.expression.getAllAttributes());
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RelationalPredicate)) {
			return false;
		}
        return this.expression
        		.equals(((RelationalPredicate) other).expression);
	}

	@Override
	public int hashCode() {
		return 23 * this.expression.hashCode();
	}

	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		replacementMap.put(curAttr, newAttr);
	}

	// nur testweise zum Evaluieren
	public SDFExpression getExpression() {
		return expression;
	}

	@Override
	public boolean equals(IPredicate<Tuple<?>> pred) {
		// Falls die Expressions nicht identisch sind, ist dennoch eine
		// inhaltliche Äquivalenz möglich
		if (!this.equals((Object) pred)) {
			// boolean isContainedIn1 = this.isContainedIn(pred);
			// boolean isContainedIn2 = pred.isContainedIn(this);
			return this.isContainedIn(pred) && pred.isContainedIn(this);
		}
        return true;
	}

	@Override
	@SuppressWarnings({"rawtypes"})
	public boolean isContainedIn(IPredicate<?> o) {
		if (!(o instanceof RelationalPredicate)) {
			return false;
		}
		RelationalPredicate rp2 = (RelationalPredicate) o;

		// Komplexe Prädikate
		// AND
		if (this.isAndPredicate()) {
			// Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist
			// (Zusätzliche Verschärfung bestehender Prädikate)
			if (!rp2.isAndPredicate()) {
				List<IPredicate> spred = splitPredicate();

				for (IPredicate<?> p : spred) {
					if (p.isContainedIn(o)) {
						return true;
					}
				}
				return false;
			}
			// TODO: Noch mal überprüfen
			// // Falls es sich beim anderen Prädikat ebenfalls um ein
			// AndPredicate handelt, müssen beide Prädikate verglichen werden
			// (inklusiver aller "Unterprädikate")
			// if(o instanceof AndPredicate) {
			// AndPredicate<T> ap = (AndPredicate<T>) o;
			//
			//
			//
			// ArrayList<IPredicate<?>> a = extractAllPredicates(this);
			// ArrayList<IPredicate<?>> b = extractAllPredicates(ap);
			//
			// // Für JEDES Prädikat aus dem anderen AndPredicate muss ein
			// enthaltenes Prädikat in diesem AndPredicate gefunden werden
			// // (Nur weitere Verschärfungen sind zulässig, deshalb darf keine
			// Bedingung des anderen Prädikats stärker sein)
			// for(IPredicate<?> predb : b) {
			// // if(predb instanceof OrPredicate) {
			// // return false;
			// // }
			// boolean foundmatch = false;
			// for(IPredicate<?> preda : a) {
			// // if(preda instanceof OrPredicate) {
			// // return false;
			// // }
			// if(preda.isContainedIn(predb)) {
			// foundmatch = true;
			// }
			// }
			// if(!foundmatch) {
			// return false;
			// }
			// }
			// return true;
			// }
			//
			return false;
		}
		// OR
		// TODO: Aus dem alten OR-Predicate extrahieren
		if (this.isOrPredicate()){
			return false;
		}
		// NOT 
		// TODO: Geht das besser?
		if (this.isNotPredicate()){
			if (rp2.isNotPredicate()){
				return false;
			}
			return false;
		}
		// BASIS-Prädikat
		// Unterschiedliche Anzahl Attribute
		if (this.getAttributes().size() != rp2.getAttributes().size()) {
			return false;
		}
		try {
			// Noch mal zu parsen scheint mir wenig sinnvoll zu sein ...
			IExpression<?> ex1 = this.expression.getMEPExpression();// MEP.parse(this.expression.getExpression());
			IExpression<?> ex2 = rp2.expression.getMEPExpression();// MEP.parse(rp2.getExpression().getExpression());
			if (ex1.getReturnType().equals(ex2.getReturnType())
					&& ex1.isFunction()) {
				IFunction<?> if1 = (IFunction<?>) ex1;
				IFunction<?> if2 = (IFunction<?>) ex2;
				if (if1.getArity() != 2) {
					return false;
				}

				IExpression<?> firstArgument1 = if1.getArgument(0);
				IExpression<?> secondArgument1 = if1.getArgument(1);
				IExpression<?> firstArgument2 = if2.getArgument(0);
				IExpression<?> secondArgument2 = if2.getArgument(1);

				String symbol1 = if1.getSymbol();
				String symbol2 = if2.getSymbol();
				// Funktionen enthalten nur ein Attribut und ansonsten
				// Konstanten
				if (this.getAttributes().size() == 1) {

					// gleiches Attribut auf der linken Seite, Konstante auf der
					// rechten
					if (firstArgument1.isVariable()
							&& secondArgument1.isConstant()
							&& firstArgument2.isVariable()
							&& secondArgument2.isConstant()
							&& firstArgument1.toVariable().equals(
									firstArgument2.toVariable())) {
						Double c1 = Double.parseDouble(secondArgument1
								.toString());
						Double c2 = Double.parseDouble(secondArgument2
								.toString());

						// Funktion kleiner-als
						if (symbol1.equals("<")
								&& (symbol2.equals("<") || symbol2.equals("<="))
								&& c1 <= c2) {
							return true;
						}
						// Funktion kleiner-gleich-als
						if (symbol1.equals("<=")
								&& ((symbol2.equals("<=") && c1 <= c2) || (symbol2
										.equals("<") && c1 < c2))) {
							return true;
						}
						// Funktion ist-gleich oder ist-nicht-gleich
						if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1
								.equals("!=") && symbol2.equals("!=")))
								&& c1.compareTo(c2) == 0) {
							return true;
						}
						// Funktion größer-als
						if (symbol1.equals(">")
								&& (symbol2.equals(">") || symbol2.equals(">="))
								&& c1 >= c2) {
							return true;
						}
						// Funktion größer-gleich-als
						if (symbol1.equals(">=")
								&& ((symbol2.equals(">=") && c1 >= c2) || (symbol2
										.equals(">") && c1 > c2))) {
							return true;
						}
						// gleiches Attribut auf der rechten Seite, Konstante
						// auf der linken Seite
					} else if (secondArgument1.isVariable()
							&& firstArgument1.isConstant()
							&& secondArgument2.isVariable()
							&& firstArgument2.isConstant()
							&& secondArgument1.toVariable().equals(
									secondArgument2.toVariable())) {

						Double c1 = Double.parseDouble(firstArgument1
								.toString());
						Double c2 = Double.parseDouble(firstArgument2
								.toString());

						// Funktion kleiner-als
						if (symbol1.equals("<")
								&& (symbol2.equals("<") || symbol2.equals("<="))
								&& c1 >= c2) {
							return true;
						}
						// Funktion kleiner-gleich-als
						if (symbol1.equals("<=")
								&& ((symbol2.equals("<=") && c1 >= c2) || (symbol2
										.equals("<") && c1 > c2))) {
							return true;
						}
						// Funktion ist-gleich oder ist-nicht-gleich
						if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1
								.equals("!=") && symbol2.equals("!=")))
								&& c1.compareTo(c2) == 0) {
							return true;
						}
						// Funktion größer-als
						if (symbol1.equals(">")
								&& (symbol2.equals(">") || symbol2.equals(">="))
								&& c1 <= c2) {
							return true;
						}
						// Funktion größer-gleich-als
						if (symbol1.equals(">=")
								&& ((symbol2.equals(">=") && c1 <= c2) || (symbol2
										.equals(">") && c1 < c2))) {
							return true;
						}
						// Attribut bei F1 links, bei F2 rechts
					} else if (firstArgument1.isVariable()
							&& secondArgument1.isConstant()
							&& secondArgument2.isVariable()
							&& firstArgument2.isConstant()
							&& firstArgument1.toVariable().equals(
									secondArgument2.toVariable())) {
						Double c1 = Double.parseDouble(secondArgument1
								.toString());
						Double c2 = Double.parseDouble(firstArgument2
								.toString());

						// F1 kleiner-als, F2 größer-als oder größer-gleich-als
						if (symbol1.equals("<")
								&& (symbol2.equals(">") || symbol2.equals(">="))
								&& c1 <= c2) {
							return true;
						}
						// F1 kleiner-gleich-als, F2 größer-als oder
						// größer-gleich-als
						if (symbol1.equals("<=")
								&& (symbol2.equals(">") && c1 < c2)
								|| (symbol2.equals(">=")) && c1 <= c2) {
							return true;
						}
						// Funktion ist-gleich oder ist-nicht-gleich
						if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1
								.equals("!=") && symbol2.equals("!=")))
								&& c1.compareTo(c2) == 0) {
							return true;
						}
						// F1 größer-als, F2 kleiner-als oder kleiner-gleich-als
						if (symbol1.equals(">")
								&& (symbol2.equals("<") || symbol2.equals("<="))
								&& c1 >= c2) {
							return true;
						}
						// F1 größer-gleich-als, F2 kleiner-als oder
						// kleiner-gleich-als
						if (symbol1.equals(">=")
								&& (symbol2.equals("<") && c1 > c2)
								|| (symbol2.equals("<=")) && c1 >= c2) {
							return true;
						}

						// Attribut bei F1 rechts, bei F2 links
					} else if (secondArgument1.isVariable()
							&& firstArgument1.isConstant()
							&& firstArgument2.isVariable()
							&& secondArgument2.isConstant()
							&& secondArgument1.toVariable().equals(
									firstArgument2.toVariable())) {

						Double c1 = Double.parseDouble(firstArgument1
								.toString());
						Double c2 = Double.parseDouble(secondArgument2
								.toString());

						// F1 kleiner-als, F2 größer-als oder größer-gleich-als
						if (symbol1.equals("<")
								&& (symbol2.equals(">") || symbol2.equals(">="))
								&& c1 >= c2) {
							return true;
						}

						// F1 kleiner-gleich-als, F2 größer-als oder
						// größer-gleich-als
						if (symbol1.equals("<=")
								&& (symbol2.equals(">") && c1 > c2)
								|| (symbol2.equals(">=")) && c1 >= c2) {
							return true;
						}

						// Funktion ist-gleich oder ist-nicht-gleich
						if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1
								.equals("!=") && symbol2.equals("!=")))
								&& c1.compareTo(c2) == 0) {
							return true;
						}

						// F1 größer-als, F2 kleiner-als oder kleiner-gleich-als
						if (symbol1.equals(">")
								&& (symbol2.equals("<") || symbol2.equals("<="))
								&& c1 <= c2) {
							return true;
						}

						// F1 größer-gleich-als, F2 kleiner-als oder
						// kleiner-gleich-als
						if (symbol1.equals(">=")
								&& (symbol2.equals("<") && c1 < c2)
								|| (symbol2.equals("<=")) && c1 <= c2) {
							return true;
						}
					}
					// Funktionen sind Vergleiche zwischen zwei Attributen
				} else if (this.getAttributes().size() == 2) {
					Variable v11 = firstArgument1.toVariable();
					Variable v12 = secondArgument1.toVariable();
					Variable v21 = firstArgument2.toVariable();
					Variable v22 = secondArgument2.toVariable();

					// Attribute sind links und rechts gleich
					if (v11.equals(v21) && v12.equals(v22)) {
						if ((symbol1.equals("<") && symbol2.equals("<"))
								|| (symbol1.equals("<=") && symbol2
										.equals("<="))
								|| (symbol1.equals("==") && symbol2
										.equals("=="))
								|| (symbol1.equals("!=") && symbol2
										.equals("!="))
								|| (symbol1.equals(">=") && symbol2
										.equals(">="))
								|| (symbol1.equals(">") && symbol2.equals(">"))) {
							return true;
						}
					}
					// linkes Attribut von F1 ist gleich rechtem Attribut von F2
					// und umgekehrt
					if (v11.equals(v22) && v12.equals(v21)) {
						if ((symbol1.equals("<") && symbol2.equals(">"))
								|| (symbol1.equals("<=") && symbol2
										.equals(">="))
								|| (symbol1.equals("==") && symbol2
										.equals("=="))
								|| (symbol1.equals("!=") && symbol2
										.equals("!="))
								|| (symbol1.equals(">=") && symbol2
										.equals("<="))
								|| (symbol1.equals(">") && symbol2.equals("<"))) {
							return true;
						}
					}
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	// Helpermethods
	public boolean isAndPredicate() {
		return isAndPredicate(expression.getMEPExpression());
	}

	static boolean isAndPredicate(IExpression<?> iExpression) {
		return iExpression.isFunction()
				&& iExpression.toFunction() instanceof AndOperator;
	}

	public boolean isOrPredicate() {
		return expression.getMEPExpression().isFunction()
				&& expression.getMEPExpression().toFunction() instanceof OrOperator;
	}

	public boolean isNotPredicate() {
		return expression.getMEPExpression().isFunction()
				&& expression.getMEPExpression().toFunction() instanceof NotOperator;
	}

	/**
	 * Returns List of conjunctive predicates
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<IPredicate> splitPredicate() {
		if (isAndPredicate()) {
			List<IPredicate> result = new LinkedList<IPredicate>();
			Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
			expressionStack.push(expression.getMEPExpression());

			while (!expressionStack.isEmpty()) {
				IExpression<?> curExpression = expressionStack.pop();
				if (isAndPredicate(curExpression)) {
					expressionStack.push(curExpression.toFunction()
							.getArgument(0));
					expressionStack.push(curExpression.toFunction()
							.getArgument(1));
				} else {
					SDFExpression expr = new SDFExpression(curExpression,
							expression.getAttributeResolver(), MEP.getInstance());
					result.add(new RelationalPredicate(expr));
				}
			}
			return result;

		}
		throw new RuntimeException(
				"Split can only be called on conjunctive predicates");

	}

}
