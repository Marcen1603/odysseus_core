/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticTimeInterval extends TimeInterval implements IProbabilisticTimeInterval {

    /**
	 * 
	 */
    private static final long serialVersionUID = -9030157268224460919L;
    /** The classes. */
    @SuppressWarnings("unchecked")
    public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { ITimeInterval.class, IProbabilistic.class };
    /** The tuple probability. */
    private final IProbabilistic probabilistic;

	static final SDFSchema schema;
	static{
		schema = SDFSchemaFactory.createNewSchema(TimeInterval.schema, Probabilistic.schema);
	}

	@Override
	public SDFSchema getSchema() {
		return schema;
	}

    
    /**
     * 
     * Default constructor.
     */
    public ProbabilisticTimeInterval() {
        super();
        this.probabilistic = new Probabilistic();
    }

    /**
     * Clone constructor.
     * 
     * @param copy
     *            The object to copy from
     */
    public ProbabilisticTimeInterval(final ProbabilisticTimeInterval copy) {
        super(copy);
        this.probabilistic = copy.probabilistic.clone();
    }

	@Override
	public void fillValueList(List<Tuple<?>> values) {
		super.fillValueList(values);
		probabilistic.fillValueList(values);
	}
    
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ProbabilisticTimeInterval clone() {
        return new ProbabilisticTimeInterval(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public double getExistence() {
        return this.probabilistic.getExistence();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void setExistence(final double existence) {
        Preconditions.checkArgument((existence >= 0.0) && (existence <= 1.0));
        this.probabilistic.setExistence(existence);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + " | " + this.probabilistic;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String csvToString(WriteOptions options) {
        return super.csvToString(options) + options.getDelimiter()
                + this.probabilistic.csvToString(options);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getCSVHeader(final char delimiter) {
        return super.getCSVHeader(delimiter) + delimiter + this.probabilistic.getCSVHeader(delimiter);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Class<? extends IMetaAttribute>[] getClasses() {
        return ProbabilisticTimeInterval.CLASSES;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "ProbabilisticTimeInterval";
    }
}
