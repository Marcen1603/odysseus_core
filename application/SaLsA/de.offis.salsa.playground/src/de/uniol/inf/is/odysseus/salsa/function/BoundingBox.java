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
package de.uniol.inf.is.odysseus.salsa.function;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class BoundingBox extends AbstractFunction<Geometry> {
    /**
     * 
     */
    private static final long serialVersionUID = 6794116043110763731L;
    public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
        {
                SDFSpatialDatatype.SPATIAL_POINT, SDFSpatialDatatype.SPATIAL_LINE_STRING,
                SDFSpatialDatatype.SPATIAL_POLYGON, SDFSpatialDatatype.SPATIAL_MULTI_POINT,
                SDFSpatialDatatype.SPATIAL_MULTI_LINE_STRING,
                SDFSpatialDatatype.SPATIAL_MULTI_POLYGON,
                SDFSpatialDatatype.SPATIAL_GEOMETRY_COLLECTION, SDFSpatialDatatype.SPATIAL_GEOMETRY
        }
    };

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(final int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): Two geometries.");
        }
        return BoundingBox.accTypes[argPos];
    }

    @Override
    public String getSymbol() {
        return "BoundingBox";
    }

    @Override
    public Geometry getValue() {
        return ((Geometry) this.getInputValue(0)).getEnvelope();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFSpatialDatatype.SPATIAL_GEOMETRY;
    }

}
