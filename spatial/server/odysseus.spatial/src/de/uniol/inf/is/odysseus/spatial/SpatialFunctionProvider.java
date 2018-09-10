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
package de.uniol.inf.is.odysseus.spatial;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.spatial.functions.ApproximateDistance;
import de.uniol.inf.is.odysseus.spatial.functions.AsCartesianCoordinates;
import de.uniol.inf.is.odysseus.spatial.functions.AsGeometry;
import de.uniol.inf.is.odysseus.spatial.functions.AsGeometryCollection;
import de.uniol.inf.is.odysseus.spatial.functions.AsLineString;
import de.uniol.inf.is.odysseus.spatial.functions.AsMultiLineString;
import de.uniol.inf.is.odysseus.spatial.functions.AsMultiPoint;
import de.uniol.inf.is.odysseus.spatial.functions.AsMultiPolygon;
import de.uniol.inf.is.odysseus.spatial.functions.AsPoint;
import de.uniol.inf.is.odysseus.spatial.functions.AsPolarCoordinates;
import de.uniol.inf.is.odysseus.spatial.functions.AsPolygon;
import de.uniol.inf.is.odysseus.spatial.functions.CalculateBearing;
import de.uniol.inf.is.odysseus.spatial.functions.CalculateDistance;
import de.uniol.inf.is.odysseus.spatial.functions.CalculateEndingCoordinates;
import de.uniol.inf.is.odysseus.spatial.functions.FromWKT;
import de.uniol.inf.is.odysseus.spatial.functions.GeoHashCoverage;
import de.uniol.inf.is.odysseus.spatial.functions.GeoHashInside;
import de.uniol.inf.is.odysseus.spatial.functions.GetCentroid;
import de.uniol.inf.is.odysseus.spatial.functions.GetCoordinate;
import de.uniol.inf.is.odysseus.spatial.functions.GetXFromSpatial;
import de.uniol.inf.is.odysseus.spatial.functions.GetYFromSpatial;
import de.uniol.inf.is.odysseus.spatial.functions.OrthodromicDistance;
import de.uniol.inf.is.odysseus.spatial.functions.ST_SetSRID;
import de.uniol.inf.is.odysseus.spatial.functions.ST_Transform;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialBuffer;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialContains;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialConvexHull;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialCoveredBy;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialCovers;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialCrosses;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialDisjoint;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialDistance;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialEquals;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIntersection;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIsLine;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIsPolygon;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIsWithinDistance;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialOrthodromicMetricDistance;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialTouches;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialUnion;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialUnionBuffer;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialWithin;
import de.uniol.inf.is.odysseus.spatial.functions.ToCartesianCoordinate;
import de.uniol.inf.is.odysseus.spatial.functions.ToEllipsoid;
import de.uniol.inf.is.odysseus.spatial.functions.ToGeoHash;
import de.uniol.inf.is.odysseus.spatial.functions.ToPoint;
import de.uniol.inf.is.odysseus.spatial.functions.ToPolarCoordinate;

public class SpatialFunctionProvider implements IFunctionProvider {

	public SpatialFunctionProvider() {
	}

	@Override
	public List<IMepFunction<?>> getFunctions() {
		final List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
		functions.add(new SpatialContains());
		functions.add(new SpatialCoveredBy());
		functions.add(new SpatialCovers());
		functions.add(new SpatialCrosses());
		functions.add(new SpatialDisjoint());
		functions.add(new SpatialEquals());
		functions.add(new SpatialIntersection());
		functions.add(new SpatialIsWithinDistance());
		functions.add(new SpatialTouches());
		functions.add(new SpatialWithin());

		functions.add(new SpatialConvexHull());
		functions.add(new SpatialUnion());
		functions.add(new SpatialUnionBuffer());
		functions.add(new SpatialBuffer());

		functions.add(new AsGeometry());
		functions.add(new AsGeometryCollection());
		functions.add(new AsPoint());
		functions.add(new AsMultiPoint());
		functions.add(new AsLineString());
		functions.add(new AsMultiLineString());
		functions.add(new AsPolygon());
		functions.add(new AsMultiPolygon());
		functions.add(new GetCentroid());

		functions.add(new SpatialIsPolygon());
		functions.add(new SpatialIsLine());
		functions.add(new FromWKT());
		functions.add(new ST_Transform());
		functions.add(new ST_SetSRID());

		functions.add(new AsPolarCoordinates());
		functions.add(new AsCartesianCoordinates());
		functions.add(new ToPolarCoordinate());
		functions.add(new ToCartesianCoordinate());

		functions.add(new ToPoint());

		functions.add(new SpatialDistance());
		functions.add(new OrthodromicDistance());
		functions.add(new ApproximateDistance());

		functions.add(new CalculateBearing());
		functions.add(new CalculateEndingCoordinates());
		functions.add(new CalculateDistance());
		functions.add(new ToEllipsoid());
		functions.add(new GetCoordinate());

		functions.add(new GetXFromSpatial());
		functions.add(new GetYFromSpatial());

		functions.add(new SpatialOrthodromicMetricDistance());

		functions.add(new ToGeoHash());
		functions.add(new GeoHashCoverage());
		functions.add(new GeoHashInside());

		return functions;
	}

}
