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

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Probabilistic implements IProbabilistic {
    /**
	 * 
	 */
    private static final long serialVersionUID = -147594856639774242L;
    /** The classes. */
    @SuppressWarnings("unchecked")
    public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IProbabilistic.class };
    /** Tuple existence probability. */
    private double existence;

	public static final SDFSchema schema;
	static{
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		attributes.add(new SDFAttribute("Probabilistic", "existence", SDFDatatype.DOUBLE, null));
		schema = SDFSchemaFactory.createNewSchema("Probabilistic", Tuple.class, attributes);
	}
	
	@Override
	public SDFSchema getSchema() {
		return schema;
	}
    
    /**
     * Default constructor.
     */
    public Probabilistic() {
        this.existence = 1.0;
    }

    /**
     * Creates a new {@link Probabilistic} with the given existence value.
     * 
     * @param existence
     *            The existence value
     */
    public Probabilistic(final double existence) {
        Preconditions.checkArgument((existence >= 0.0) && (existence <= 1.0));
        this.existence = existence;
    }

    /**
     * Clone constructor.
     * 
     * @param copy
     *            The object to copy from
     */
    public Probabilistic(final Probabilistic copy) {
        this.existence = copy.existence;

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final double getExistence() {
        return this.existence;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void setExistence(final double existence) {
        this.existence = existence;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String csvToString(WriteOptions options) {
        if (options.hasFloatingFormatter()) {
            return options.getFloatingFormatter().format(this.existence);
        }
        else {
            return "" + this.existence;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String getCSVHeader(final char delimiter) {
        return "probability";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final IProbabilistic clone() {
        return new Probabilistic(this);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "" + this.existence;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Class<? extends IMetaAttribute>[] getClasses() {
        return Probabilistic.CLASSES;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Probabilistic";
    }

}
