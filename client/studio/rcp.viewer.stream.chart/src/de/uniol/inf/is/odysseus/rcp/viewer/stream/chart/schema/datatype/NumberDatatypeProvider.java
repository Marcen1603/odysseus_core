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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.datatype;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;


public class NumberDatatypeProvider extends AbstractViewableDatatype<Double> {	
	
    public NumberDatatypeProvider() {
        super.addProvidedSDFDatatype(SDFDatatype.BYTE);
        super.addProvidedSDFDatatype(SDFDatatype.SHORT);     
        super.addProvidedSDFDatatype(SDFDatatype.INTEGER);
        super.addProvidedSDFDatatype(SDFDatatype.LONG);
        super.addProvidedSDFDatatype(SDFDatatype.START_TIMESTAMP);
        super.addProvidedSDFDatatype(SDFDatatype.END_TIMESTAMP);
        super.addProvidedSDFDatatype(SDFDatatype.TIMESTAMP);
        super.addProvidedSDFDatatype(SDFDatatype.FLOAT);
        super.addProvidedSDFDatatype(SDFDatatype.DOUBLE);
        super.addProvidedClass(Byte.class);
        super.addProvidedClass(byte.class);
        super.addProvidedClass(Short.class);
        super.addProvidedClass(short.class);
        super.addProvidedClass(Integer.class);
        super.addProvidedClass(int.class);
        super.addProvidedClass(Long.class);
        super.addProvidedClass(long.class);
        super.addProvidedClass(Float.class);
        super.addProvidedClass(float.class);
        super.addProvidedClass(Double.class);
        super.addProvidedClass(double.class);
    }
	
	@Override
	public Double convertToValue(Object value) {
	    if (value == null) {
	        return Double.NaN;
	    }
		Number num = (Number)value;
		return new Double(num.doubleValue());
	}

}
