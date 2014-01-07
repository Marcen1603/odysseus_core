/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractRelationalPredicate<T extends Tuple<?>> extends AbstractPredicate<T> implements IRelationalPredicate<T> {
    /**
     * 
     */
    private static final long serialVersionUID = 5696868093867495724L;

    private Logger logger = LoggerFactory.getLogger(RelationalPredicate.class);

    protected final SDFExpression expression;

    // stores which attributes are needed at which position for
    // variable bindings
    protected int[] attributePositions;

    final List<SDFAttribute> neededAttributes;

    // fromRightChannel[i] stores if the getAttribute(attributePositions[i])
    // should be called on the left or on the right input tuple
    protected boolean[] fromRightChannel;

    protected Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

    protected SDFSchema leftSchema;
    protected SDFSchema rightSchema;

    /**
     * 
     */
    public AbstractRelationalPredicate(SDFExpression expression) {
        this.expression = expression;
        this.neededAttributes = expression.getAllAttributes();
    }

    public AbstractRelationalPredicate(AbstractRelationalPredicate<T> predicate) {
        this.attributePositions = predicate.attributePositions == null ? null : (int[]) predicate.attributePositions.clone();
        this.fromRightChannel = predicate.fromRightChannel == null ? null : (boolean[]) predicate.fromRightChannel.clone();
        this.expression = predicate.expression == null ? null : predicate.expression.clone();
        this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
        this.neededAttributes = new ArrayList<SDFAttribute>(predicate.neededAttributes);
        // logger.debug("Cloned "+this+ " "+attributePositions);
    }

    /**
     * @return the expression
     */
    public SDFExpression getExpression() {
        return this.expression;
    }
    
    @Override
    public List<SDFAttribute> getAttributes() {
    	return neededAttributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
        init(leftSchema, rightSchema, true);
    }

    public void init(SDFSchema leftSchema, SDFSchema rightSchema, boolean checkRightSchema) {
        logger.debug("Init ("+this+"): Left "+leftSchema+" Right "+rightSchema);
        this.leftSchema = leftSchema;
        this.rightSchema = rightSchema;

        List<SDFAttribute> neededAttributes = expression.getAllAttributes();
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];

        int i = 0;
        for (SDFAttribute curAttribute : neededAttributes) {
            if (curAttribute == null) {
                throw new IllegalArgumentException("Needed attribute for expression " + expression + " may not be null!");
            }
            int pos = leftSchema.indexOf(curAttribute);
            if (pos == -1) {
                if (rightSchema == null && checkRightSchema) {
                    throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + leftSchema + " and rightSchema is null!");
                }
                if (checkRightSchema) {
                    // if you get here, there is an attribute
                    // in the predicate that does not exist
                    // in the left schema, so there must also be
                    // a right schema
                    pos = indexOf(rightSchema, curAttribute);
                    if (pos == -1) {
                        throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + rightSchema);
                    }
                }
                this.fromRightChannel[i] = true;
            }
            this.attributePositions[i++] = pos;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(IPredicate<T> pred) {
        // Falls die Expressions nicht identisch sind, ist dennoch eine
        // inhaltliche ���quivalenz m���glich
        if (!this.equals((Object) pred)) {
            // boolean isContainedIn1 = this.isContainedIn(pred);
            // boolean isContainedIn2 = pred.isContainedIn(this);
            return this.isContainedIn(pred) && pred.isContainedIn(this);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
        if (!curAttr.equals(newAttr)) {
            replacementMap.put(curAttr, newAttr);
        }
        else {
            logger.warn("Replacement " + curAttr + " --> " + newAttr + " not added because they are equal!");
        }
    }

    public boolean isAndPredicate() {
        return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction() instanceof AndOperator;
    }

    public boolean isOrPredicate() {
        return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction() instanceof OrOperator;
    }

    public boolean isNotPredicate() {
        return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction() instanceof NotOperator;
    }

    /**
     * Proxy for splitPredicate function.
     * 
     * @param init
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<IPredicate> splitPredicate(boolean init) {
        return splitPredicate();
    }

    @SuppressWarnings("rawtypes")
    public List<IPredicate> splitPredicate() {
        List<IPredicate> result = new LinkedList<IPredicate>();
        if (isAndPredicate()) {
            Stack<IExpression<?>> expressionStack = new Stack<IExpression<?>>();
            expressionStack.push(expression.getMEPExpression());

            while (!expressionStack.isEmpty()) {
                IExpression<?> curExpression = expressionStack.pop();
                if (isAndExpression(curExpression)) {
                    expressionStack.push(curExpression.toFunction().getArgument(0));
                    expressionStack.push(curExpression.toFunction().getArgument(1));
                }
                else {
                    SDFExpression expr = new SDFExpression(curExpression, expression.getAttributeResolver(), MEP.getInstance());
                    RelationalPredicate relationalPredicate = new RelationalPredicate(expr);
                    relationalPredicate.init(expression.getSchema(), null, false);
                    result.add(relationalPredicate);
                }
            }
            return result;

        }
        result.add(this);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isContainedIn(IPredicate<?> o) {
        if (!(o instanceof AbstractRelationalPredicate)) {
            return false;
        }
        AbstractRelationalPredicate<?> rp2 = (AbstractRelationalPredicate<?>) o;

        // Komplexe Pr���dikate
        // AND
        if (this.isAndPredicate()) {
            // Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist
            // (Zus���tzliche Versch���rfung bestehender Pr���dikate)
            if (!rp2.isAndPredicate()) {
                @SuppressWarnings("rawtypes")
                List<IPredicate> spred = splitPredicate();

                for (IPredicate<?> p : spred) {
                    if (p.isContainedIn(o)) {
                        return true;
                    }
                }
                return false;
            }
            // TODO: Noch mal ���berpr���fen
            // // Falls es sich beim anderen Pr���dikat ebenfalls um ein
            // AndPredicate handelt, m���ssen beide Pr���dikate verglichen
            // werden
            // (inklusiver aller "Unterpr���dikate")
            // if(o instanceof AndPredicate) {
            // AndPredicate<T> ap = (AndPredicate<T>) o;
            //
            //
            //
            // ArrayList<IPredicate<?>> a = extractAllPredicates(this);
            // ArrayList<IPredicate<?>> b = extractAllPredicates(ap);
            //
            // // F���r JEDES Pr���dikat aus dem anderen AndPredicate muss ein
            // enthaltenes Pr���dikat in diesem AndPredicate gefunden werden
            // // (Nur weitere Versch���rfungen sind zul���ssig, deshalb darf
            // keine
            // Bedingung des anderen Pr���dikats st���rker sein)
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
        if (this.isOrPredicate()) {
            return false;
        }
        // NOT
        // TODO: Geht das besser?
        if (this.isNotPredicate()) {
            if (rp2.isNotPredicate()) {
                return false;
            }
            return false;
        }
        // BASIS-Pr���dikat
        // Unterschiedliche Anzahl Attribute
        if (this.getAttributes().size() != rp2.getAttributes().size()) {
            return false;
        }
        try {
            // Noch mal zu parsen scheint mir wenig sinnvoll zu sein ...
            IExpression<?> ex1 = this.expression.getMEPExpression();// MEP.parse(this.expression.getExpression());
            IExpression<?> ex2 = rp2.expression.getMEPExpression();// MEP.parse(rp2.getExpression().getExpression());
            if (ex1.getReturnType().equals(ex2.getReturnType()) && ex1.isFunction()) {
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
                    if (firstArgument1.isVariable() && secondArgument1.isConstant() && firstArgument2.isVariable() && secondArgument2.isConstant()
                            && firstArgument1.toVariable().equals(firstArgument2.toVariable())) {
                        Double c1 = Double.parseDouble(secondArgument1.toString());
                        Double c2 = Double.parseDouble(secondArgument2.toString());

                        // Funktion kleiner-als
                        if (symbol1.equals("<") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 <= c2) {
                            return true;
                        }
                        // Funktion kleiner-gleich-als
                        if (symbol1.equals("<=") && ((symbol2.equals("<=") && c1 <= c2) || (symbol2.equals("<") && c1 < c2))) {
                            return true;
                        }
                        // Funktion ist-gleich oder ist-nicht-gleich
                        if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
                            return true;
                        }
                        // Funktion gr������er-als
                        if (symbol1.equals(">") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 >= c2) {
                            return true;
                        }
                        // Funktion gr������er-gleich-als
                        if (symbol1.equals(">=") && ((symbol2.equals(">=") && c1 >= c2) || (symbol2.equals(">") && c1 > c2))) {
                            return true;
                        }
                        // gleiches Attribut auf der rechten Seite, Konstante
                        // auf der linken Seite
                    }
                    else if (secondArgument1.isVariable() && firstArgument1.isConstant() && secondArgument2.isVariable() && firstArgument2.isConstant()
                            && secondArgument1.toVariable().equals(secondArgument2.toVariable())) {

                        Double c1 = Double.parseDouble(firstArgument1.toString());
                        Double c2 = Double.parseDouble(firstArgument2.toString());

                        // Funktion kleiner-als
                        if (symbol1.equals("<") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 >= c2) {
                            return true;
                        }
                        // Funktion kleiner-gleich-als
                        if (symbol1.equals("<=") && ((symbol2.equals("<=") && c1 >= c2) || (symbol2.equals("<") && c1 > c2))) {
                            return true;
                        }
                        // Funktion ist-gleich oder ist-nicht-gleich
                        if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
                            return true;
                        }
                        // Funktion gr������er-als
                        if (symbol1.equals(">") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 <= c2) {
                            return true;
                        }
                        // Funktion gr������er-gleich-als
                        if (symbol1.equals(">=") && ((symbol2.equals(">=") && c1 <= c2) || (symbol2.equals(">") && c1 < c2))) {
                            return true;
                        }
                        // Attribut bei F1 links, bei F2 rechts
                    }
                    else if (firstArgument1.isVariable() && secondArgument1.isConstant() && secondArgument2.isVariable() && firstArgument2.isConstant()
                            && firstArgument1.toVariable().equals(secondArgument2.toVariable())) {
                        Double c1 = Double.parseDouble(secondArgument1.toString());
                        Double c2 = Double.parseDouble(firstArgument2.toString());

                        // F1 kleiner-als, F2 gr������er-als oder
                        // gr������er-gleich-als
                        if (symbol1.equals("<") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 <= c2) {
                            return true;
                        }
                        // F1 kleiner-gleich-als, F2 gr������er-als oder
                        // gr������er-gleich-als
                        if (symbol1.equals("<=") && (symbol2.equals(">") && c1 < c2) || (symbol2.equals(">=")) && c1 <= c2) {
                            return true;
                        }
                        // Funktion ist-gleich oder ist-nicht-gleich
                        if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
                            return true;
                        }
                        // F1 gr������er-als, F2 kleiner-als oder
                        // kleiner-gleich-als
                        if (symbol1.equals(">") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 >= c2) {
                            return true;
                        }
                        // F1 gr������er-gleich-als, F2 kleiner-als oder
                        // kleiner-gleich-als
                        if (symbol1.equals(">=") && (symbol2.equals("<") && c1 > c2) || (symbol2.equals("<=")) && c1 >= c2) {
                            return true;
                        }

                        // Attribut bei F1 rechts, bei F2 links
                    }
                    else if (secondArgument1.isVariable() && firstArgument1.isConstant() && firstArgument2.isVariable() && secondArgument2.isConstant()
                            && secondArgument1.toVariable().equals(firstArgument2.toVariable())) {

                        Double c1 = Double.parseDouble(firstArgument1.toString());
                        Double c2 = Double.parseDouble(secondArgument2.toString());

                        // F1 kleiner-als, F2 gr������er-als oder
                        // gr������er-gleich-als
                        if (symbol1.equals("<") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 >= c2) {
                            return true;
                        }

                        // F1 kleiner-gleich-als, F2 gr������er-als oder
                        // gr������er-gleich-als
                        if (symbol1.equals("<=") && (symbol2.equals(">") && c1 > c2) || (symbol2.equals(">=")) && c1 >= c2) {
                            return true;
                        }

                        // Funktion ist-gleich oder ist-nicht-gleich
                        if (((symbol1.equals("==") && symbol2.equals("==")) || (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
                            return true;
                        }

                        // F1 gr������er-als, F2 kleiner-als oder
                        // kleiner-gleich-als
                        if (symbol1.equals(">") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 <= c2) {
                            return true;
                        }

                        // F1 gr������er-gleich-als, F2 kleiner-als oder
                        // kleiner-gleich-als
                        if (symbol1.equals(">=") && (symbol2.equals("<") && c1 < c2) || (symbol2.equals("<=")) && c1 <= c2) {
                            return true;
                        }
                    }
                    // Funktionen sind Vergleiche zwischen zwei Attributen
                }
                else if (this.getAttributes().size() == 2) {
                    Variable v11 = firstArgument1.toVariable();
                    Variable v12 = secondArgument1.toVariable();
                    Variable v21 = firstArgument2.toVariable();
                    Variable v22 = secondArgument2.toVariable();

                    // Attribute sind links und rechts gleich
                    if (v11.equals(v21) && v12.equals(v22)) {
                        if ((symbol1.equals("<") && symbol2.equals("<")) || (symbol1.equals("<=") && symbol2.equals("<=")) || (symbol1.equals("==") && symbol2.equals("=="))
                                || (symbol1.equals("!=") && symbol2.equals("!=")) || (symbol1.equals(">=") && symbol2.equals(">=")) || (symbol1.equals(">") && symbol2.equals(">"))) {
                            return true;
                        }
                    }
                    // linkes Attribut von F1 ist gleich rechtem Attribut von F2
                    // und umgekehrt
                    if (v11.equals(v22) && v12.equals(v21)) {
                        if ((symbol1.equals("<") && symbol2.equals(">")) || (symbol1.equals("<=") && symbol2.equals(">=")) || (symbol1.equals("==") && symbol2.equals("=="))
                                || (symbol1.equals("!=") && symbol2.equals("!=")) || (symbol1.equals(">=") && symbol2.equals("<=")) || (symbol1.equals(">") && symbol2.equals("<"))) {
                            return true;
                        }
                    }
                }

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.expression.toString();
    }

    private boolean isAndExpression(IExpression<?> expression) {
        return expression.isFunction() && expression.toFunction() instanceof AndOperator;

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
}
