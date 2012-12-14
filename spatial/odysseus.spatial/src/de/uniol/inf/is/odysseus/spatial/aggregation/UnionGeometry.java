/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.spatial.aggregation;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class UnionGeometry extends AbstractAggregateFunction<Tuple<?>, Tuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -594477978741672401L;
    private final int         attribPos;

    public UnionGeometry(final int[] pos) {
        super("MergeGeometry");
        this.attribPos = pos[0];
    }

    @Override
    public IPartialAggregate<Tuple<?>> init(final Tuple<?> in) {
        final IPartialAggregate<Tuple<?>> grid = new GeometryPartialUnionAggregate<Tuple<?>>(
                (Geometry) in.getAttribute(this.attribPos));
        return grid;
    }

    @Override
    public IPartialAggregate<Tuple<?>> merge(final IPartialAggregate<Tuple<?>> p, final Tuple<?> toMerge,
            final boolean createNew) {
        GeometryPartialUnionAggregate<Tuple<?>> geometry = null;
        if (createNew) {
            geometry = new GeometryPartialUnionAggregate<Tuple<?>>(
                    ((GeometryPartialUnionAggregate<Tuple<?>>) p).getGeometry());
        }
        else {
            geometry = (GeometryPartialUnionAggregate<Tuple<?>>) p;
        }
        geometry.union((Geometry) toMerge.getAttribute(this.attribPos));
        return geometry;
    }

    @Override
    public Tuple<?> evaluate(final IPartialAggregate<Tuple<?>> p) {
        final GeometryPartialUnionAggregate<Tuple<?>> geometry = (GeometryPartialUnionAggregate<Tuple<?>>) p;
        final Tuple<? extends IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
        tuple.setAttribute(0, geometry.getGeometry());
        return tuple;
    }

}
