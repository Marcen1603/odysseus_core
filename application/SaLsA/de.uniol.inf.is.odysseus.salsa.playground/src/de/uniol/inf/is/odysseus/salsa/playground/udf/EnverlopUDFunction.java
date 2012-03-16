package de.uniol.inf.is.odysseus.salsa.playground.udf;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

@UserDefinedFunction(name = "ENVERLOP")
public class EnverlopUDFunction implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

	String init = null;

	@Override
	public void init(String initString) {
		init = initString;
	}

	@Override
	@SuppressWarnings("all")
	public Tuple<? extends IMetaAttribute> process(
			Tuple<? extends IMetaAttribute> in, int port) {
		Tuple<IMetaAttribute> intuple = (Tuple<IMetaAttribute>) in;
		
		for (int i = 0; i < ((List<Tuple<IMetaAttribute>>) intuple.getAttribute(0)).size(); i++) {
			Geometry geometry = (Geometry) ((Tuple) ((List<Tuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i)).getAttribute(0);
			//geometry = geometry.convexHull();
			((List<Tuple<IMetaAttribute>>) intuple.getAttribute(0)).get(i).setAttribute(0, geometry.getEnvelope());
		
		}
		
		//intuple.setAttribute(0, mergetupleList);
		return intuple;
	}



	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
