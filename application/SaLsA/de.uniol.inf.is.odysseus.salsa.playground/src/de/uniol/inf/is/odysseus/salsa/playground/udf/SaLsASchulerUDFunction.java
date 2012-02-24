package de.uniol.inf.is.odysseus.salsa.playground.udf;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.MultiPoint;

@UserDefinedFunction(name="SchulerBG")
public class SaLsASchulerUDFunction<R> implements
		IUserDefinedFunction<R, R> {

	String init = null;
	
	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	public R process(R in, int port) {
		//System.out.println(this+"("+init+") PROCESS "+in);
		RelationalTuple<IMetaAttribute> tuple = (RelationalTuple<IMetaAttribute>)in;
		
		MultiPoint multiPoint = (MultiPoint)tuple.getAttribute(0); 
		
		for(int i=0; i < multiPoint.getCoordinates().length; i++){
			//multiPoint.
		}
		
		for(Coordinate cord: multiPoint.getCoordinates()){
			if(cord.x > 200){
			
			}
		}
		
		return (R)tuple;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
