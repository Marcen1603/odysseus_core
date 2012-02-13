package de.uniol.inf.is.odysseus.spatial;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.spatial.functions.AsGeometry;
import de.uniol.inf.is.odysseus.spatial.functions.AsGeometryCollection;
import de.uniol.inf.is.odysseus.spatial.functions.AsLineString;
import de.uniol.inf.is.odysseus.spatial.functions.AsMultiLineString;
import de.uniol.inf.is.odysseus.spatial.functions.AsMultiPoint;
import de.uniol.inf.is.odysseus.spatial.functions.AsMultiPolygon;
import de.uniol.inf.is.odysseus.spatial.functions.AsPoint;
import de.uniol.inf.is.odysseus.spatial.functions.AsPolygon;
import de.uniol.inf.is.odysseus.spatial.functions.FromWKT;
import de.uniol.inf.is.odysseus.spatial.functions.ST_SetSRID;
import de.uniol.inf.is.odysseus.spatial.functions.ST_Transform;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialBuffer;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialContains;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialConvexHull;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialCoveredBy;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialCovers;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialCrosses;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialDisjoint;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialEquals;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIntersection;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIsLine;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIsPolygon;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialIsWithinDistance;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialTouches;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialUnion;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialUnionBuffer;
import de.uniol.inf.is.odysseus.spatial.functions.SpatialWithin;

public class SpatialFunctionProvider implements IFunctionProvider {

	public SpatialFunctionProvider() {
	}

	@Override
	public List<IFunction<?>> getFunctions() {
		if (SpatialDatatypeProvider.datadictionary == null)
			return new ArrayList<IFunction<?>>();
		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
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
		
		functions.add(new SpatialIsPolygon());
		functions.add(new SpatialIsLine());
		functions.add(new FromWKT());
		functions.add(new ST_Transform());
		functions.add(new ST_SetSRID());

		return functions;
	}

}
