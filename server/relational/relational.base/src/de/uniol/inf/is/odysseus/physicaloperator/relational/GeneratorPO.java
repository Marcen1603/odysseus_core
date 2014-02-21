/**
 * 
 */
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class GeneratorPO<M extends ITimeInterval> extends AbstractPipe<Tuple<M>, Tuple<M>> {
    private static final Logger LOG = LoggerFactory.getLogger(GeneratorPO.class);
    private final Map<Long, LinkedList<Tuple<M>>> leftGroupsLastObjects = new HashMap<>();
    private final Map<Long, LinkedList<Tuple<M>>> rightGroupsLastObjects = new HashMap<>();
    private IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor = null;
    protected VarHelper[][] variables; // Expression.Index
    private SDFExpression[] expressions;
    private final SDFSchema inputSchema;
    private final boolean allowNull;
    private int maxHistoryElements = 0;
    private int frequency;
    private TimeUnit baseTimeUnit;

    /**
 * 
 */
    public GeneratorPO(SDFSchema inputSchema, SDFExpression[] expressions, boolean allowNullInOutput, IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor, int frequency) {
        this.inputSchema = inputSchema;
        this.allowNull = allowNullInOutput;
        this.groupProcessor = groupProcessor;
        this.frequency = frequency;
        init(inputSchema, expressions);
    }

    /**
     * @param generatorPO
     */
    public GeneratorPO(GeneratorPO<M> po) {
        this.inputSchema = po.inputSchema;
        this.allowNull = po.allowNull;
        this.groupProcessor = po.groupProcessor;
        this.frequency = po.frequency;
    }

    private void init(SDFSchema schema, SDFExpression[] expressions) {
        this.expressions = new SDFExpression[expressions.length];
        for (int i = 0; i < expressions.length; ++i) {
            this.expressions[i] = expressions[i].clone();
        }
        this.variables = new VarHelper[expressions.length][];
        int i = 0;
        for (SDFExpression expression : expressions) {
            List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            VarHelper[] newArray = new VarHelper[neededAttributes.size()];
            this.variables[i++] = newArray;
            int j = 0;
            for (SDFAttribute curAttribute : neededAttributes) {
                newArray[j++] = initAttribute(schema, curAttribute);
            }
        }
    }

    private VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
        return new VarHelper(schema.indexOf(curAttribute), curAttribute.getNumber() < 0 ? 0 : curAttribute.getNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    public void setBasetimeUnit(TimeUnit baseTimeUnit) {
        this.baseTimeUnit = baseTimeUnit;
    }

    public TimeUnit getBaseTimeUnit() {
        return baseTimeUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_next(Tuple<M> object, int port) {
        Long groupId = groupProcessor.getGroupID(object);

        // Left stream is the stream with missing elements
        if (port == 0) {
            LinkedList<Tuple<M>> lastObjects = leftGroupsLastObjects.get(groupId);
            if (lastObjects == null) {
                lastObjects = new LinkedList<>();
                leftGroupsLastObjects.put(groupId, lastObjects);
            }
            if (lastObjects.size() > 0) {
                lastObjects.removeLast();
            }
            lastObjects.addFirst(object);
        }
        else {
            LinkedList<Tuple<M>> lastObjects = rightGroupsLastObjects.get(groupId);
            if (lastObjects == null) {
                lastObjects = new LinkedList<>();
                rightGroupsLastObjects.put(groupId, lastObjects);
            }

            LinkedList<Tuple<M>> leftObjects = leftGroupsLastObjects.get(groupId);
            if ((leftObjects != null) && (leftObjects.size() > 0)) {
                Tuple<M> left = leftObjects.getFirst();

                PointInTime leftStreamTime = left.getMetadata().getStart();
                PointInTime rightStreamTime = object.getMetadata().getStart();
                PointInTime delta = rightStreamTime.minus(leftStreamTime);
                int amount = (int) (TimeUnit.MILLISECONDS.convert(delta.getMainPoint(), getBaseTimeUnit()) / frequency);
                if (amount > 0) {
                    generateData(lastObjects, object, left, amount);
                }
            }
            int lastObjectSize = lastObjects.size();
            if (lastObjectSize > maxHistoryElements) {
                lastObjects.removeLast();
            }
            lastObjects.addFirst(object);

        }
        transfer(object);
    }

    /**
     * @param tupleNow
     * @param lastObjects
     * @param amount
     */
    private void generateData(LinkedList<Tuple<M>> lastObjects, Tuple<M> object, Tuple<M> sample, int amount) {
        for (int g = 0; g < amount; g++) {
            Tuple<M> outputVal = sample.clone();
            M metadata = sample.getMetadata();
            outputVal.getMetadata().setStartAndEnd(metadata.getStart().plus((g + 1) * frequency), metadata.getEnd().plus((g + 1) * frequency));
            boolean nullValueOccured = false;
            synchronized (this.expressions) {
                for (int i = 0; i < this.expressions.length; ++i) {
                    Object[] values = new Object[this.variables[i].length];
                    for (int j = 0; j < this.variables[i].length; ++j) {
                        Tuple<M> obj = null;
                        if (this.variables[i][j].objectPosToUse == 0) {
                            obj = object;
                        }
                        else {
                            if (lastObjects.size() >= this.variables[i][j].objectPosToUse) {
                                obj = lastObjects.get(this.variables[i][j].objectPosToUse - 1);
                            }
                        }
                        if (obj != null) {
                            values[j] = obj.getAttribute(this.variables[i][j].pos);
                        }
                    }

                    try {
                        this.expressions[i].bindMetaAttribute(object.getMetadata());
                        this.expressions[i].bindAdditionalContent(object.getAdditionalContent());
                        this.expressions[i].bindVariables(values);
                        Object expr = this.expressions[i].getValue();
                        outputVal.setAttribute(i, expr);
                        if (expr == null) {
                            nullValueOccured = true;
                        }
                    }
                    catch (Exception e) {
                        nullValueOccured = true;
                        if (!(e instanceof NullPointerException)) {
                            LOG.error("Cannot calc result for " + object + " with expression " + expressions[i], e);
                            // Not needed. Value is null, if not set!
                            // outputVal.setAttribute(i, null);
                        }
                    }
                    if (this.expressions[i].getType().requiresDeepClone()) {
                        outputVal.setRequiresDeepClone(true);
                    }
                }
            }
            if (!nullValueOccured || (nullValueOccured && allowNull)) {
                transfer(outputVal);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
        return new GeneratorPO<M>(this);
    }

    /**
     * @param relationalGroupProcessor
     */
    public void setGroupProcessor(IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor) {
        this.groupProcessor = groupProcessor;
    }
}
