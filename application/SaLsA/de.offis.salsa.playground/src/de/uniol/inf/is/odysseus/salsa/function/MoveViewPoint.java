package de.uniol.inf.is.odysseus.salsa.function;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class MoveViewPoint extends AbstractFunction<Object> {

    @Override
    public int getArity() {
        return 3;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry and a x and y value.");
        }
        else {
        	// @Kai: richtig so?
            final SDFDatatype[] accTypes = new SDFDatatype[3];
//            accTypes[0] = "List";
//            accTypes[1] = "Double";
//            accTypes[2] = "Double";
            return accTypes;
        }
    }

    @Override
    public String getSymbol() {
        return "MoveViewPoint";
    }

    @Override
    public Object getValue() {
        @SuppressWarnings("unchecked")
        final List<Coordinate> geometry = (List<Coordinate>) this.getInputValue(0);
        final Double x = (Double) this.getInputValue(1);
        final Double y = (Double) this.getInputValue(2);
        for (Coordinate coordinate : geometry) {
            coordinate.x = coordinate.x - x;
            coordinate.y = coordinate.y - y;
        }
        return geometry;
    }

    @Override
    public SDFDatatype getReturnType() {
        return null;
//    	return "List";
    }

}
