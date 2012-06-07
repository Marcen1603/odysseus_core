package de.uniol.inf.is.odysseus.salsa.playground.udf;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

import com.vividsolutions.jts.geom.Polygon;

@UserDefinedFunction(name="Classify")
public class SimpleClassifyUDFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	String init = null;
	
	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
		Polygon polygon = (Polygon)in.getAttribute(0); 
		
		Tuple<? extends IMetaAttribute> tuple = new Tuple<IMetaAttribute>(in.size()+1, false);
		
		
		
		
		tuple.setAttribute(0,polygon);
		tuple.setAttribute(1, "DuNo!");
		
		double area = polygon.getArea();
		
		if(area > 260 && area < 2000){
			tuple.setAttribute(1, "Human");
		}
		
		if(area > 3000){
			tuple.setAttribute(1, "Scooter");
		}
		
		return tuple;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
