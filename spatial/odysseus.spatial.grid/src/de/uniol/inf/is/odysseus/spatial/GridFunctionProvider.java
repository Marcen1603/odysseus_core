package de.uniol.inf.is.odysseus.spatial;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.spatial.functions.InverseGrid;
import de.uniol.inf.is.odysseus.spatial.functions.MoveViewPoint;
import de.uniol.inf.is.odysseus.spatial.functions.RotateGrid;
import de.uniol.inf.is.odysseus.spatial.functions.RotateViewPoint;
import de.uniol.inf.is.odysseus.spatial.functions.SubGrid;
import de.uniol.inf.is.odysseus.spatial.functions.ToGrid;

public class GridFunctionProvider implements IFunctionProvider {

	public GridFunctionProvider() {
	}

	@Override
	public List<IFunction<?>> getFunctions() {

		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();

		// Grid Functions
		functions.add(new InverseGrid());
		functions.add(new MoveViewPoint());
		functions.add(new RotateGrid());
		functions.add(new RotateViewPoint());
		functions.add(new SubGrid());
		functions.add(new ToGrid());

		return functions;
	}

}
