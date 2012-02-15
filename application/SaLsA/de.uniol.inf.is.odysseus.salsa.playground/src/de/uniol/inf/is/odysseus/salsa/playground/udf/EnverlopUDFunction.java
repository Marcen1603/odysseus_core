package de.uniol.inf.is.odysseus.salsa.playground.udf;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

@UserDefinedFunction(name = "ENVERLOP")
public class EnverlopUDFunction implements IUserDefinedFunction<RelationalTuple<? extends IMetaAttribute>, RelationalTuple<? extends IMetaAttribute>> {

	String init = null;

	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	public RelationalTuple<? extends IMetaAttribute> process(
			RelationalTuple<? extends IMetaAttribute> in, int port) {
		RelationalTuple<IMetaAttribute> intuple = (RelationalTuple<IMetaAttribute>) in;
		
		for (int i = 0; i < ((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).size(); i++) {
			Geometry geometry = (Geometry) ((RelationalTuple) ((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i)).getAttribute(0);
			//geometry = geometry.convexHull();
			((List<RelationalTuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i).setAttribute(0, geometry.getEnvelope());
		
		}
		
		//intuple.setAttribute(0, mergetupleList);
		return intuple;
	}



	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
