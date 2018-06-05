/**
 * 
 */
package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private final Map<Object, LinkedList<Tuple<M>>> leftGroupsLastObjects = new HashMap<>();
    private final Map<Object, LinkedList<Tuple<M>>> rightGroupsLastObjects = new HashMap<>();
    private IGroupProcessor<Tuple<M>, Tuple<M>> groupProcessor;
    private final ITransferArea<Tuple<M>, Tuple<M>> transfer = new TITransferArea<>();
    private final boolean multi;
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
            final IPredicate<?> iPredicate, final int frequency, final boolean multi) {
        this.expressions = new SDFExpression[expressions.length];
        this.inputSchema = inputSchema;
        this.allowNull = allowNullInOutput;
        this.groupProcessor = groupProcessor;
        this.predicate = iPredicate;
        this.frequency = frequency;
        this.multi = multi;
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
        this.multi = po.multi;
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
                    this.maxHistoryElements = Math.max(this.maxHistoryElements, newArray[j - 1].objectPosToUse);
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
        this.transfer.init(this, 1);
    }

    @Override
    public void processPunctuation(final IPunctuation punctuation, final int port) {
        this.transfer.sendPunctuation(punctuation);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Tuple<M> object, final int port) {
        Objects.requireNonNull(this.groupProcessor);
        final Object groupId = this.groupProcessor.getGroupID(object);

        // Left stream is the stream with missing elements
        if (this.predicate.evaluate(object)) {
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
                // We have two cases:
                // 1. In the right group is an element with t_R and in the left
                // group is an element with t_L and t_L > t_R -> Use the
                // timestamp from the
                // left plus the frequency
                // 2. In the right group is an element with t_R and in the left
                // group is an element with t_L and t_R > t_L -> Use the
                // timestamp from the
                // right
                M streamTimeinterval = left.getMetadata();
                streamTimeinterval.setStartAndEnd(streamTimeinterval.getStart().plus(this.frequency), streamTimeinterval.getEnd().plus(this.frequency));
                if ((lastObjects.size() > 0) && (lastObjects.getFirst().getMetadata().getStart().after(streamTimeinterval.getStart()))) {
                    streamTimeinterval = lastObjects.getFirst().getMetadata();
                }
                final PointInTime delta = object.getMetadata().getStart().minus(streamTimeinterval.getStart());
                // Number of tuples that should exist between the last
                // generation and the current tuple (not inclusive)
                final int n = (int) (delta.getMainPoint() / this.frequency) - 1;
                if (n > 0) {
                    // Generate one tuple with timestamp [t_0, t_0 +
                    // n*frequency)
                    if (!this.multi) {
                        this.generateData(lastObjects, object, left, streamTimeinterval.getStart(), streamTimeinterval.getStart().plus(n * this.frequency));
                    }
                    else {
                        // Generate n tuple with timestamp [t_i, t_i +
                        // frequency)
                        for (int i = 0; i < n; i++) {
                            this.generateData(lastObjects, object, left, streamTimeinterval.getStart().plus(i * this.frequency), streamTimeinterval.getEnd().plus((i + 1) * this.frequency));
                        }
                    }
                }
            }
            final int lastObjectSize = lastObjects.size();
            // maxHistoryElements is not set. -> Always removes last element
            if (lastObjectSize > this.maxHistoryElements) {
                lastObjects.removeLast();
            }
            lastObjects.addFirst(object);

        }
        this.transfer.transfer(object);

        PointInTime min = PointInTime.getInfinityTime();
        for (final LinkedList<Tuple<M>> group : this.leftGroupsLastObjects.values()) {
            final PointInTime start = group.getFirst().getMetadata().getStart();
            if (min.afterOrEquals(start)) {
                min = start;
            }
        }
        this.transfer.newHeartbeat(min, 0);

    }

    /**
     * @param tupleNow
     * @param lastObjects
     * @param amount
     */
    private void generateData(final LinkedList<Tuple<M>> lastObjects, final Tuple<M> object, final Tuple<M> sample, final PointInTime start, final PointInTime end) {
        final Tuple<M> outputVal = object.clone();
        outputVal.getMetadata().setStartAndEnd(start, end);
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
//                    this.expressions[i].bindMetaAttribute(object.getMetadata());
//                    this.expressions[i].bindAdditionalContent(object.getAdditionalContent());
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
            this.transfer.transfer(outputVal);
        }
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

    public VarHelper(final int pos, final int objectPosToUse) {
        super();
        this.pos = pos;
        this.objectPosToUse = objectPosToUse;
    }

    @Override
    public String toString() {
        return this.pos + " " + this.objectPosToUse;
    }
}