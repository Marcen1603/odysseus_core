/**
 * 
 */
package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

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
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
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
    private ITransferArea<Tuple<M>, Tuple<M>> transfer = new TITransferArea<>();
    private VarHelper[][] variables; // Expression.Index
    private final SDFExpression[] expressions;
    @SuppressWarnings("rawtypes")
    private final IPredicate predicate;
    private final SDFSchema inputSchema;
    private final boolean allowNull;
    private int maxHistoryElements = 0;
    private final int frequency;
    private TimeUnit baseTimeUnit;

    /**
 * 
 */
    public GeneratorPO(final SDFSchema inputSchema, final SDFExpression[] expressions, final boolean allowNullInOutput, final IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor,
            IPredicate<?> iPredicate, final int frequency) {
        this.expressions = new SDFExpression[expressions.length];
        this.inputSchema = inputSchema;
        this.allowNull = allowNullInOutput;
        this.groupProcessor = groupProcessor;
        this.predicate = iPredicate;
        this.frequency = frequency;
        this.init(inputSchema, expressions);
    }

    /**
     * @param generatorPO
     */
    public GeneratorPO(final GeneratorPO<M> po) {
        this.expressions = new SDFExpression[po.expressions.length];
        this.inputSchema = po.inputSchema;
        this.allowNull = po.allowNull;
        this.groupProcessor = po.groupProcessor;
        this.predicate = po.predicate;
        this.frequency = po.frequency;
        this.baseTimeUnit = po.baseTimeUnit;
        this.init(this.inputSchema, po.expressions);
    }

    private void init(final SDFSchema schema, final SDFExpression[] expressions) {
        for (int i = 0; i < expressions.length; ++i) {
            this.expressions[i] = expressions[i].clone();
        }
        this.variables = new VarHelper[expressions.length][];
        int i = 0;
        for (final SDFExpression expression : expressions) {
            final List<SDFAttribute> neededAttributes = expression.getAllAttributes();
            final VarHelper[] newArray = new VarHelper[neededAttributes.size()];
            this.variables[i++] = newArray;
            int j = 0;
            for (final SDFAttribute curAttribute : neededAttributes) {
                newArray[j++] = this.initAttribute(schema, curAttribute);
                if (newArray[j - 1].objectPosToUse > 0) {
                    this.maxHistoryElements = Math.max(maxHistoryElements, newArray[j - 1].objectPosToUse);
                }
            }
        }
    }

    private VarHelper initAttribute(final SDFSchema schema, final SDFAttribute curAttribute) {
        return new VarHelper(schema.indexOf(curAttribute), curAttribute.getNumber() < 0 ? 0 : curAttribute.getNumber());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
        return OutputMode.MODIFIED_INPUT;
    }

    public void setBasetimeUnit(final TimeUnit baseTimeUnit) {
        this.baseTimeUnit = baseTimeUnit;
    }

    public TimeUnit getBaseTimeUnit() {
        return this.baseTimeUnit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void process_open() throws OpenFailedException {
        super.process_open();
        transfer.init(this, 1);
    }
    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
        transfer.newElement(punctuation, port);
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Tuple<M> object, final int port) {
        final Long groupId = this.groupProcessor.getGroupID(object);

        // Left stream is the stream with missing elements
        if (predicate.evaluate(object)) {
            LinkedList<Tuple<M>> lastObjects = this.leftGroupsLastObjects.get(groupId);
            if (lastObjects == null) {
                lastObjects = new LinkedList<>();
                this.leftGroupsLastObjects.put(groupId, lastObjects);
            }
            if (lastObjects.size() > 0) {
                lastObjects.removeLast();
            }
            lastObjects.addFirst(object);
        }
        else {
            LinkedList<Tuple<M>> lastObjects = this.rightGroupsLastObjects.get(groupId);
            if (lastObjects == null) {
                lastObjects = new LinkedList<>();
                this.rightGroupsLastObjects.put(groupId, lastObjects);
            }

            final LinkedList<Tuple<M>> leftObjects = this.leftGroupsLastObjects.get(groupId);
            if ((leftObjects != null) && (leftObjects.size() > 0)) {
                final Tuple<M> left = leftObjects.getFirst();

                PointInTime leftStreamTime = left.getMetadata().getStart();
                if ((lastObjects.size() > 0) && (lastObjects.getFirst().getMetadata().getStart().after(leftStreamTime))) {
                    leftStreamTime = lastObjects.getFirst().getMetadata().getStart();
                }
                final PointInTime rightStreamTime = object.getMetadata().getStart();
                final PointInTime delta = rightStreamTime.minus(leftStreamTime);
                final int amount = (int) (delta.getMainPoint() / this.frequency);
                if (amount > 0) {
                    this.generateData(lastObjects, object, left, amount);
                }
            }
            final int lastObjectSize = lastObjects.size();
            // maxHistoryElements is not set. -> Always removes last element
            if (lastObjectSize > this.maxHistoryElements) {
                lastObjects.removeLast();
            }
            lastObjects.addFirst(object);

        }
        transfer.transfer(object);

        PointInTime min = PointInTime.getInfinityTime();
        for (LinkedList<Tuple<M>> group : this.leftGroupsLastObjects.values()) {
            PointInTime start = group.getFirst().getMetadata().getStart();
            if (min.afterOrEquals(start)) {
                min = start;
            }
        }
        transfer.newHeartbeat(min, 0);

    }

    /**
     * @param tupleNow
     * @param lastObjects
     * @param amount
     */
    private void generateData(final LinkedList<Tuple<M>> lastObjects, final Tuple<M> object, final Tuple<M> sample, final int amount) {
        for (int g = 0; g < amount; g++) {
            final Tuple<M> outputVal = sample.clone();
            M metadata = sample.getMetadata();
            Tuple<M> first = lastObjects.getFirst();
            if (first.getMetadata().getStart().after(sample.getMetadata().getStart())) {
                metadata = first.getMetadata();
            }
            outputVal.getMetadata().setStartAndEnd(metadata.getStart().plus((g + 1) * this.frequency), metadata.getEnd().plus((g + 1) * this.frequency));
            boolean nullValueOccured = false;
            synchronized (this.expressions) {
                for (int i = 0; i < this.expressions.length; ++i) {
                    final Object[] values = new Object[this.variables[i].length];
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
                        final Object expr = this.expressions[i].getValue();
                        outputVal.setAttribute(i, expr);
                        if (expr == null) {
                            nullValueOccured = true;
                        }
                    }
                    catch (final Exception e) {
                        nullValueOccured = true;
                        if (!(e instanceof NullPointerException)) {
                            GeneratorPO.LOG.error("Cannot calc result for " + object + " with expression " + this.expressions[i], e);
                            // Not needed. Value is null, if not set!
                            // outputVal.setAttribute(i, null);
                        }
                    }
                    if (this.expressions[i].getType().requiresDeepClone()) {
                        outputVal.setRequiresDeepClone(true);
                    }
                }
            }
            if (!nullValueOccured || (nullValueOccured && this.allowNull)) {
                transfer.transfer(outputVal);
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
    public void setGroupProcessor(final IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor) {
        this.groupProcessor = groupProcessor;
    }

}

class VarHelper {
    int pos;
    int objectPosToUse;

    public VarHelper(int pos, int objectPosToUse) {
        super();
        this.pos = pos;
        this.objectPosToUse = objectPosToUse;
    }

    @Override
    public String toString() {
        return pos + " " + objectPosToUse;
    }
}