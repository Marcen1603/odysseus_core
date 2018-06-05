package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;

/**
 * Implementation of Map operator using threads for each expression.
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <T>
 */
public class RelationalThreadedMapPO<T extends IMetaAttribute> extends AbstractPipe<Tuple<T>, Tuple<T>> {
    static private Logger logger = LoggerFactory.getLogger(RelationalThreadedMapPO.class);

    private VarHelper[][] variables; // Expression.Index
    private ThreadedExpression[] expressions;
    private final SDFSchema inputSchema;
    private final ExecutorService threadPool;
    final private LinkedList<Tuple<T>> lastObjects = new LinkedList<>();
    private int maxHistoryElements = 0;
    final private boolean statebased;
    final private boolean allowNull;

    public RelationalThreadedMapPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean statebased, boolean allowNullInOutput, int threads) {
        this.inputSchema = inputSchema;
        this.statebased = statebased;
        this.allowNull = allowNullInOutput;
        if (threads > 0) {
            this.threadPool = Executors.newFixedThreadPool(threads);
        } else {
            int availableProcessors = Runtime.getRuntime().availableProcessors();
            this.threadPool = Executors.newFixedThreadPool(Math.min(availableProcessors, expressions.length));
        }
        init(inputSchema, expressions);
    }

    private void init(SDFSchema schema, SDFExpression[] expressions) {
        this.expressions = new ThreadedExpression[expressions.length];
        for (int i = 0; i < expressions.length; ++i) {
            final SDFExpression expression = expressions[i].clone();
            this.expressions[i] = new ThreadedExpression(expression);
        }
        this.variables = new VarHelper[expressions.length][];
        int i = 0;
        for (SDFExpression expression : expressions) {
            List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            VarHelper[] newArray = new VarHelper[neededAttributes.size()];

            this.variables[i++] = newArray;
            int j = 0;
            for (SDFAttribute curAttribute : neededAttributes) {
                if (curAttribute.getSourceName() != null && curAttribute.getSourceName().startsWith("__last_")) {
                    if (!statebased) {
                        throw new RuntimeException("Map cannot be used with __last_! Used StateMap instead!");
                    }
                    int pos = Integer.parseInt(curAttribute.getSourceName().substring("__last_".length(), curAttribute.getSourceName().indexOf('.')));
                    if (pos > maxHistoryElements) {
                        maxHistoryElements = pos + 1;
                    }
                    String realAttrStr = curAttribute.getURI().substring(curAttribute.getURI().indexOf('.') + 1);
                    String newSource = realAttrStr.substring(0, realAttrStr.indexOf('.'));
                    String newName = realAttrStr.substring(realAttrStr.indexOf('.') + 1);
                    if ("null".equals(newSource)) {
                        newSource = null;
                    }
                    SDFAttribute newAttribute = new SDFAttribute(newSource, newName, curAttribute);
                    int index = schema.indexOf(newAttribute);
                    newArray[j++] = new VarHelper(index, pos);
                } else {
                    newArray[j++] = new VarHelper(schema.indexOf(curAttribute), 0);
                }
            }
        }
    }

    public RelationalThreadedMapPO(RelationalThreadedMapPO<T> relationalMapPO) {
        this.inputSchema = relationalMapPO.inputSchema.clone();
        this.statebased = relationalMapPO.statebased;
        this.allowNull = relationalMapPO.allowNull;
        this.threadPool = Executors.newFixedThreadPool(relationalMapPO.expressions.length);
        SDFExpression[] expressions = new SDFExpression[relationalMapPO.expressions.length];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = relationalMapPO.expressions[i].getExpression();
        }
        init(relationalMapPO.inputSchema, expressions);
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.NEW_ELEMENT;
    }

    @SuppressWarnings("unchecked")
    @Override
    final protected void process_next(Tuple<T> object, int port) {
        boolean nullValueOccured = false;
        Tuple<T> outputVal = new Tuple<T>(this.expressions.length, false);
        outputVal.setMetadata((T) object.getMetadata().clone());
        int lastObjectSize = this.lastObjects.size();
        if (lastObjectSize > maxHistoryElements) {
            lastObjects.removeLast();
            lastObjectSize--;
        }
        lastObjects.addFirst(object);
        lastObjectSize++;
        synchronized (this.expressions) {
            Future<?>[] results = new Future<?>[this.expressions.length];
            for (int i = 0; i < this.expressions.length; ++i) {
                Object[] values = new Object[this.variables[i].length];
                for (int j = 0; j < this.variables[i].length; ++j) {
                    Tuple<T> obj = null;
                    if (lastObjectSize > this.variables[i][j].getObjectPosToUse()) {
                        obj = lastObjects.get(this.variables[i][j].getObjectPosToUse());
                    }
                    if (obj != null) {
                        values[j] = obj.getAttribute(this.variables[i][j].getPos());
                    }
                }

                try {
//                    this.expressions[i].getExpression().bindMetaAttribute(object.getMetadata());
//                    this.expressions[i].getExpression().bindAdditionalContent(object.getAdditionalContent());
                    this.expressions[i].getExpression().bindVariables(values);
                    results[i] = threadPool.submit(this.expressions[i]);
                } catch (Exception e) {
                    nullValueOccured = true;
                    if (!(e instanceof NullPointerException)) {
                        logger.error("Cannot calc result ", e);
                        // Not needed. Value is null, if not set!
                        // outputVal.setAttribute(i, null);
                    }
                }
            }
            for (int i = 0; i < this.expressions.length; ++i) {
                try {
                    Object expr = results[i].get();
                    outputVal.setAttribute(i, expr);
                    if (expr == null) {
                        nullValueOccured = true;
                    }
                } catch (Exception e) {
                    nullValueOccured = true;
                    if (!(e instanceof NullPointerException)) {
                        logger.error("Cannot calc result ", e);
                        // Not needed. Value is null, if not set!
                        // outputVal.setAttribute(i, null);
                    }
                }
                if (this.expressions[i].getExpression().getType().requiresDeepClone()) {
                    outputVal.setRequiresDeepClone(true);
                }
            }

        }
        if (!nullValueOccured || (nullValueOccured && allowNull)) {
            transfer(outputVal);
        }

    }
    
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

    @Override
    @SuppressWarnings({ "rawtypes" })
    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
        if (!(ipo instanceof RelationalThreadedMapPO)) {
            return false;
        }
        RelationalThreadedMapPO rmpo = (RelationalThreadedMapPO) ipo;

        if (!this.getOutputSchema().equals(rmpo.getOutputSchema())) {
            return false;
        }

        if (this.inputSchema.compareTo(rmpo.inputSchema) == 0) {
            if (this.expressions.length == rmpo.expressions.length) {
                for (int i = 0; i < this.expressions.length; i++) {
                    if (!this.expressions[i].getExpression().equals(rmpo.expressions[i].getExpression())) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isStatebased() {
        return statebased;
    }

}

class ThreadedExpression implements Callable<Object> {
    private SDFExpression expression;

    public ThreadedExpression(SDFExpression expression) {
        this.expression = expression;
    }

    public SDFExpression getExpression() {
        return this.expression;
    }

    @Override
    public Object call() throws Exception {
        return expression.getValue();
    }
}