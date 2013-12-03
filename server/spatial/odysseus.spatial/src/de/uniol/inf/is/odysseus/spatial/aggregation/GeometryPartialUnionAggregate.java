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

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class GeometryPartialUnionAggregate<T> implements IPartialAggregate<T> {

    private Geometry geometry;

    public GeometryPartialUnionAggregate(final Geometry geoometry) {
        this.geometry = (Geometry) this.geometry.clone();
    }

    public GeometryPartialUnionAggregate(final GeometryPartialUnionAggregate<T> geometryPartialAggregate) {
        this.geometry = (Geometry) geometryPartialAggregate.geometry.clone();
    }

    public Geometry getGeometry() {
        return this.geometry;
    }

    public void union(final Geometry other) {
        this.geometry = this.geometry.union(other);
    }

    @Override
    public String toString() {
        final StringBuffer ret = new StringBuffer("GeometryPartialAggregate (").append(this.hashCode()).append(")")
                .append(this.geometry);
        return ret.toString();
    }

    @Override
    public GeometryPartialUnionAggregate<T> clone() {
        return new GeometryPartialUnionAggregate<T>(this);
    }

}
