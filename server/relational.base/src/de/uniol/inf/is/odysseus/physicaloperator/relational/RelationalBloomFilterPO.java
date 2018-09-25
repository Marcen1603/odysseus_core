/**********************************************************************************
  * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BloomFilterAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 * @param <M>
 */
@SuppressWarnings("unused")
public class RelationalBloomFilterPO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, Tuple<M>> {
    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(RelationalBloomFilterPO.class);

    private int[] restrictList;
    private final Funnel<Tuple<M>> funnel;
    private BloomFilter<Tuple<M>> filter;
    private final int expectedInsertions;
    private final double falsePositiveProbability;

    public RelationalBloomFilterPO(final BloomFilterAO operator) {
        final List<SDFAttribute> attributes = operator.getAttributes();
        if (attributes != null) {
            this.restrictList = new int[operator.getAttributes().size()];
            int i = 0;

            final SDFSchema inputSchema = operator.getInputSchema();

            for (final SDFAttribute restrictAttribute : operator.getAttributes()) {
                final int pos = inputSchema.indexOf(restrictAttribute);
                if (pos == -1) {
                    throw new IllegalArgumentException("Attribute " + restrictAttribute + " not found in input schema");
                }
                this.restrictList[i++] = pos;
            }
        }
        else {
            this.restrictList = new int[operator.getInputSchema().size()];
            for (int i = 0; i < this.restrictList.length; i++) {
                this.restrictList[i] = i;
            }
        }
        this.falsePositiveProbability = operator.getFalsePositiveProbability();
        this.expectedInsertions = operator.getExpectedInsertions();
        this.funnel = new Funnel<Tuple<M>>() {

            /**
             *
             */
            private static final long serialVersionUID = 9194887025041759170L;

            @Override
            public void funnel(final Tuple<M> tuple, final PrimitiveSink into) {
                if (RelationalBloomFilterPO.this.restrictList != null) {
                    for (final int pos : RelationalBloomFilterPO.this.restrictList) {
                        final SDFDatatype datatype = operator.getInputSchema().get(pos).getDatatype();

                        if (datatype.isByte()) {
                            into.putByte(((Number) tuple.getAttribute(pos)).byteValue());
                        }
                        else if (datatype.isShort()) {
                            into.putShort(((Number) tuple.getAttribute(pos)).shortValue());
                        }
                        else if (datatype.isInteger()) {
                            into.putInt(((Number) tuple.getAttribute(pos)).intValue());
                        }
                        else if (datatype.isLong()) {
                            into.putLong(((Number) tuple.getAttribute(pos)).longValue());
                        }
                        else if (datatype.isFloat()) {
                            into.putFloat(((Number) tuple.getAttribute(pos)).floatValue());
                        }
                        else if (datatype.isDouble()) {
                            into.putDouble(((Number) tuple.getAttribute(pos)).doubleValue());
                        }
                        else if (datatype.isChar()) {
                            into.putChar((Character) tuple.getAttribute(pos));
                        }
                        else if (datatype.isBoolean()) {
                            into.putBoolean((Boolean) tuple.getAttribute(pos));
                        }
                        else if (datatype.isString()) {
                            into.putString(tuple.getAttribute(pos).toString(), Charsets.UTF_8);
                        }
                        else if (datatype.isByteBuffer()) {
                            into.putBytes(((ByteBuffer) tuple.getAttribute(pos)).array());
                        }
                        else {
                            into.putBytes(tuple.getAttribute(pos).toString().getBytes());
                        }
                    }
                }
            }
        };

    }

    public RelationalBloomFilterPO(final RelationalBloomFilterPO<M> operator) {
        super();
        final int length = operator.restrictList.length;
        this.restrictList = new int[length];
        System.arraycopy(operator.restrictList, 0, this.restrictList, 0, length);
        this.expectedInsertions = operator.expectedInsertions;
        this.falsePositiveProbability = operator.falsePositiveProbability;
        this.funnel = operator.funnel;
    }

    @Override
    protected void process_open() throws OpenFailedException {
        this.filter = BloomFilter.create(this.funnel, this.expectedInsertions, this.falsePositiveProbability);
        super.process_open();
    }

    @Override
    protected void process_close() {
        super.process_close();
        this.filter = null;
    }

    @Override
    public void processPunctuation(final IPunctuation punctuation, final int port) {
        this.sendPunctuation(punctuation);
    }

    @Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;

    }

    @Override
    protected void process_next(final Tuple<M> object, final int port) {
        if (!this.filter.mightContain(object)) {
            this.filter.put(object);
            this.transfer(object);
        }
    }

    @Override
    public boolean isSemanticallyEqual(final IPhysicalOperator operator) {
        if (!(operator instanceof RelationalBloomFilterPO)) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final RelationalBloomFilterPO<M> other = (RelationalBloomFilterPO<M>) operator;
        if (this.restrictList.length == other.restrictList.length) {
            for (int i = 0; i < this.restrictList.length; i++) {
                if (this.restrictList[i] != other.restrictList[i]) {
                    return false;
                }
            }
            return true;
        }
        return super.isSemanticallyEqual(operator);
    }

}
