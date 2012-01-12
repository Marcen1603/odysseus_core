package de.uniol.inf.is.odysseus.salsa.playground.udf;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

@UserDefinedFunction(name="salsatransform")
public class SaLsAUDFunction<R> implements
		IUserDefinedFunction<R, R> {


	@Override
	public R process(R in, int port) {
		//System.out.println(this+"("+init+") PROCESS "+in);
		
		RelationalTuple<IMetaAttribute> tuple = (RelationalTuple<IMetaAttribute>)in;	
		
		//POINT (25 15749)
		String[] cpoint = tuple.getAttribute(1).toString().substring(7,tuple.getAttribute(1).toString().length()-1).split(" ");	
		tuple.setAttribute(1, new GeometryFactory().createPoint(new Coordinate(Double.parseDouble(cpoint[0]) , Double.parseDouble(cpoint[1]))));
		
		//POLYGON ((10 15734, 40 15764, 20 15744, 30 15754, 10 15734))
		Coordinate[] coordinates = new Coordinate[5];
		int i = 0;
		for(String polyPoint  : tuple.getAttribute(2).toString().substring(9,tuple.getAttribute(2).toString().length()-2).split(",")){				
			coordinates[i] = new Coordinate(		
					Double.parseDouble(polyPoint.substring(1).split(" ")[0])
					, 
					Double.parseDouble(polyPoint.substring(1).split(" ")[1])
					);
			
			i++;
		}
		tuple.setAttribute(2,  new GeometryFactory().createPolygon(new GeometryFactory().createLinearRing(coordinates) , null));
		
		System.out.println(tuple.toString());
		return (R) tuple;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public void init(String initString) {
		// TODO Auto-generated method stub
		
	}

}
