/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.mep.functions.math;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/*
 * Diese Klasse dient dazu, aus einem �bergebenen Double-Wert die Wurzel zu ziehen.
 * @Deprecated Use SqrtFunction
 */
public class SqrtValue extends SqrtFunction {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7488942114484992572L;
    private static final Logger LOG = LoggerFactory.getLogger(SqrtValue.class);

    // /*
    // * Liefert die Anzahl der m�glichen �bergabeparameter zur�ck.
    // */
    // @Override
    // public int getArity() {
    // return 1;
    // }

    /*
     * Liefert den Namen zur�ck mit welchem die Funktion aufgerufen wird.
     */
    @Override
    public String getSymbol() {
        return "SqrtValue";
    }

    // /*
    // * Liefert die Wurzel des �bergebenen Wertes zur�ck.
    // */
    // @Override
    // public Object getValue() {
    // double d = Math.sqrt((Double)getInputValue(0));
    // //System.out.println("Gating: " + d);
    // return d;
    // }

    // /*
    // * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
    // */
    // @Override
    // public SDFDatatype getReturnType() {
    // return SDFDatatype.DOUBLE;
    // }

    public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT };

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos > 0) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
        }
        LOG.debug(getSymbol() + " is deprecated and will be removed in future releases. Please use " + super.getSymbol());
        return accTypes;
    }

}
