package de.uniol.inf.is.odysseus.tools.test.udf;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

@UserDefinedFunction(name="GeometryCollector")
public class GeometryCollectorUDFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	//private String init = null;
	private int iterations = 0;
	private Geometry[] collection;
	
	@Override
	public void init(String initString) {
		//init = initString;
	}

	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> tuple, int port) {
		int attributePos = 0;
		
		for(int i = 0; i < tuple.size(); i++){
			if(tuple.getAttribute(i) instanceof Geometry){
				attributePos = i;
				collection[iterations] = tuple.getAttribute(i);
			}
		}
		iterations++;
		if(iterations == 10){
			tuple.setAttribute(attributePos, new GeometryFactory().createGeometryCollection(collection));
			iterations = 0;
			return tuple;
		}
		return null;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

}
