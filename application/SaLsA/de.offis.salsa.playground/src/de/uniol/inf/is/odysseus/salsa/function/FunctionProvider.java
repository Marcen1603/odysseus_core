package de.uniol.inf.is.odysseus.salsa.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class FunctionProvider implements IFunctionProvider {
    public FunctionProvider() {
        System.out.println("+++++++++++++++++++++ adding SaLsA functions. ++++++++++++++++++++++");
    }

    @Override
    public List<IFunction<?>> getFunctions() {

        final List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
        functions.add(new ExtractSegments());
        functions.add(new MoveViewPoint());
        functions.add(new RotateViewPoint());
        functions.add(new ToDoubleGrid());
        functions.add(new ToFloatGrid());
        functions.add(new ToByteGrid());
        functions.add(new ToBooleanGrid());
        functions.add(new InverseDoubleGrid());
        functions.add(new InverseFloatGrid());
        functions.add(new InverseByteGrid());
        functions.add(new InverseBooleanGrid());
        functions.add(new IEPF());
        functions.add(new IsGridFree());
        functions.add(new ClearDoubleGrid());
        functions.add(new ClearFloatGrid());
        functions.add(new ClearByteGrid());
        functions.add(new ClearBooleanGrid());
        functions.add(new SubDoubleGrid());
        functions.add(new SubByteGrid());
        functions.add(new SubFloatGrid());
        functions.add(new SubBooleanGrid());
        return functions;
    }
}
