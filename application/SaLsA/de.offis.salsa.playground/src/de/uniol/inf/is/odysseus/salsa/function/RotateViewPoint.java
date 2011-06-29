package de.uniol.inf.is.odysseus.salsa.function;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class RotateViewPoint extends AbstractFunction<Object> {

    @Override
    public int getArity() {
        return 2;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                    + " argument(s): A geometry and an angle.");
        }
        else {
        	// @Kai: richtig so?
            final SDFDatatype[] accTypes = new SDFDatatype[2];
//            accTypes[0] = "List";
//            accTypes[1] = "Double";
            return accTypes;
        }
    }

    @Override
    public String getSymbol() {
        return "RotateViewPoint";
    }

    @Override
    public Object getValue() {
        @SuppressWarnings("unchecked")
        final List<Coordinate> geometry = (List<Coordinate>) this.getInputValue(0);
        final Double angle = (Double) this.getInputValue(1);
        for (Coordinate coordinate : geometry) {
            double x = coordinate.x;
            double y = coordinate.y;
            coordinate.x = x * Math.cos(angle) - y * Math.sin(angle);
            coordinate.y = x * Math.sin(angle) + y * Math.cos(angle);
        }
        return geometry;
    }

    @Override
    public SDFDatatype getReturnType() {
//        return "List";
    	return null;
    }

}
