/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.core.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;

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

    protected final List<SDFAttribute> neededAttributes;

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

    public void init(List<SDFSchema> schema, boolean checkRightSchema){
    	if (schema.size() == 1){
    		init(schema.get(0), null, checkRightSchema);
    	}else if (schema.size()==2){
    		init(schema.get(0), schema.get(1), checkRightSchema);    		
    	}else{
    		throw new IllegalArgumentException("Predicate cannot have more than two input schema");
    	}
    	
    }
    
    public void init(SDFSchema leftSchema, SDFSchema rightSchema, boolean checkRightSchema) {
        logger.debug("Init ("+this+"): Left "+leftSchema+" Right "+rightSchema);
        this.leftSchema = leftSchema;
        this.rightSchema = rightSchema;

        List<SDFAttribute> neededAttributes = expression.getAllAttributes();
        this.attributePositions = new int[neededAttributes.size()];
        this.fromRightChannel = new boolean[neededAttributes.size()];

        int i = 0;
        for (SDFAttribute curAttr : neededAttributes) {
        	SDFAttribute replOfCurAttr = this.getReplacement(curAttr);
            SDFAttribute curAttribute = leftSchema.findAttribute(replOfCurAttr.getURI());
            if (curAttribute == null && !checkRightSchema){
            	logger.error("Attribute "+curAttr.getURI()+" not found in "+leftSchema);
            	 throw new IllegalArgumentException("Attribute "+curAttr.getURI()+" not found in "+leftSchema);
            }
            int pos = leftSchema.indexOf(curAttribute);
            if (pos == -1) {
                if (rightSchema == null && checkRightSchema) {
                    throw new IllegalArgumentException("Attribute " + curAttribute + " not in " + leftSchema + " and rightSchema is null!");
                }
                if (rightSchema != null && checkRightSchema) {
                    // if you get here, there is an attribute
                    // in the predicate that does not exist
                    // in the left schema, so there must also be
                    // a right schema
                	curAttribute = rightSchema.findAttribute(replOfCurAttr.getURI());
                    pos = rightSchema.indexOf(curAttribute);
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
        return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction().getSymbol().equalsIgnoreCase("&&");
    }

    public boolean isOrPredicate() {
        return expression.getMEPExpression().isFunction() && expression.getMEPExpression().toFunction().getSymbol().equalsIgnoreCase("||");
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
    abstract public List<IPredicate> splitPredicate();


    /**
     * Return true if the given relational predicate is of the form:
     * 
     * A.x=B.y AND A.y=B.z AND * ...
     * 
     * @return <code>true</code> iff the relational predicate is of the given
     *         form
     */
    public boolean isEquiPredicate() {
        Objects.requireNonNull(this.getExpression());
        Objects.requireNonNull(this.getExpression().getMEPExpression());
        final IExpression<?> expression = this.getExpression().getMEPExpression();
        return isEquiExpression(expression);
    }
    
    /**
     * Return the map of attributes used in an equi predicate.
     * 
     * @param resolver
     *            The attribute resolver
     * @return The map of attributes
     */
    public Map<SDFAttribute, List<SDFAttribute>> getEquiExpressionAtributes(final IAttributeResolver resolver) {
        Objects.requireNonNull(this.getExpression());
        Objects.requireNonNull(this.getExpression().getMEPExpression());
        final IExpression<?> expression = this.getExpression().getMEPExpression();
        return getEquiExpressionAtributes(expression, resolver);
    }
    
    /**
     * Return the map of attributes used in an equi expression.
     * 
     * @param expression
     *            The expression
     * @param resolver
     *            The attribute resolver
     * @return The map of attributes
     */
    private Map<SDFAttribute, List<SDFAttribute>> getEquiExpressionAtributes(final IExpression<?> expression, final IAttributeResolver resolver) {
        Objects.requireNonNull(expression);
        Objects.requireNonNull(resolver);
        final Map<SDFAttribute, List<SDFAttribute>> attributes = new HashMap<SDFAttribute, List<SDFAttribute>>();
        if ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("&&"))) {
            final Map<SDFAttribute, List<SDFAttribute>> leftAttributes = getEquiExpressionAtributes(((AndOperator) expression).getArgument(0), resolver);
            for (final SDFAttribute key : leftAttributes.keySet()) {
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).addAll(leftAttributes.get(key));
            }
            final Map<SDFAttribute, List<SDFAttribute>> rigthAttributes = getEquiExpressionAtributes(((AndOperator) expression).getArgument(1), resolver);
            for (final SDFAttribute key : rigthAttributes.keySet()) {
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).addAll(rigthAttributes.get(key));
            }
        }
        if ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("=="))) {
            final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
            final IExpression<?> arg1 = eq.getArgument(0);
            final IExpression<?> arg2 = eq.getArgument(1);
            if ((arg1 instanceof Variable) && (arg2 instanceof Variable)) {
                final SDFAttribute key = resolver.getAttribute(((Variable) arg1).getIdentifier());
                if (!attributes.containsKey(key)) {
                    attributes.put(key, new ArrayList<SDFAttribute>());
                }
                attributes.get(key).add(resolver.getAttribute(((Variable) arg2).getIdentifier()));
            }
        }
        return attributes;
    }
    
    /**
     * Return true if the given expression is of the form:
     * 
     * A.x=B.y AND A.y=B.z AND * ...
     * 
     * @param expression
     *            The expression
     * @return <code>true</code> iff the expression is of the given form
     */
    private boolean isEquiExpression(final IExpression<?> expression) {
        Objects.requireNonNull(expression);
        if (expression instanceof AndOperator) {
            return isEquiExpression(((AndOperator) expression).getArgument(0)) && isEquiExpression(((AndOperator) expression).getArgument(1));

        }
        if ((expression.isFunction()) && (expression.toFunction().getSymbol().equalsIgnoreCase("=="))) {
            final IBinaryOperator<?> eq = (IBinaryOperator<?>) expression;
            final IExpression<?> arg1 = eq.getArgument(0);
            final IExpression<?> arg2 = eq.getArgument(1);
            if ((arg1 instanceof Variable) && (arg2 instanceof Variable)) {
                return true;
            }
        }
        return false;
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
                        Double c1;
                        Double c2;
						try {
							c1 = Double.parseDouble(secondArgument1.toString());
							c2 = Double.parseDouble(secondArgument2.toString());
						} catch (Exception e) {
							return false;
						}

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

                        Double c1;
                        Double c2;
						try {
							c1 = Double.parseDouble(firstArgument1.toString());
							c2 = Double.parseDouble(firstArgument2.toString());
						} catch (Exception e) {
							return false;
						}

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
                        Double c1;
                        Double c2;
						try {
							c1 = Double.parseDouble(secondArgument1.toString());
							c2 = Double.parseDouble(firstArgument2.toString());
						} catch (Exception e) {
							return false;
						}

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

                        Double c1;
                        Double c2;
						try {
							c1 = Double.parseDouble(firstArgument1.toString());
							c2 = Double.parseDouble(secondArgument2.toString());
						} catch (Exception e) {
							return false;
						}

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
            System.err.println("Query Sharing Error: "+e.getMessage());
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

    protected boolean isAndExpression(IExpression<?> expression) {
        return expression.isFunction() && expression.toFunction().getSymbol().equalsIgnoreCase("&&");
    }

//    private int indexOf(SDFSchema schema, SDFAttribute attr) {
//        SDFAttribute cqlAttr = getReplacement(attr);
//        Iterator<SDFAttribute> it = schema.iterator();
//        for (int i = 0; it.hasNext(); ++i) {
//            SDFAttribute a = it.next();
//            if (cqlAttr.equalsCQL(a)) {
//                return i;
//            }
//        }
//        return -1;
//    }

    private SDFAttribute getReplacement(SDFAttribute a) {
        SDFAttribute ret = a;
        SDFAttribute tmp = null;
        while ((tmp = replacementMap.get(ret)) != null) {
            ret = getReplacement(tmp);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> and(IPredicate<T> predicate) {
        return new AndPredicate<>(this, predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> or(IPredicate<T> predicate) {
        return new OrPredicate<>(this, predicate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPredicate<T> not() {
        return new NotPredicate<>(this);

    }
}
