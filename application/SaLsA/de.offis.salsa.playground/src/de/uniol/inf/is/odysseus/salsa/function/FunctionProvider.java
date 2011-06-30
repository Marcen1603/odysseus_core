package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class FunctionProvider implements IFunctionProvider {
    public FunctionProvider() {
        System.out.println("+++++++++++++++++++++ adding SaLsA functions. ++++++++++++++++++++++");
    }

    @Override
    public List<IFunction<?>> getFunctions() {

        List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
        functions.add(new ExtractSegments());
        functions.add(new MoveViewPoint());
        functions.add(new RotateViewPoint());

        return functions;
    }
}
