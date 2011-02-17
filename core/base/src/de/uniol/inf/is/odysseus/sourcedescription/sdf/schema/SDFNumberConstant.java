/** Copyright [2011] [The Odysseus Team]
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
/*
 * Created on 06.12.2004
 *
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author Marco Grawunder
 *
 */
public class SDFNumberConstant extends SDFConstant {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2412682973178628837L;

	/**
     * @param URI
     * @param value
     */
    public SDFNumberConstant(String URI, double value) {
        super(URI, ""+value,SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
    }

    /**
     * @param constValue
     */
    public SDFNumberConstant(SDFConstant constValue) {
        super(constValue.getURI(false),constValue.getString(), constValue.getDatatype());
    }

    /**
     * @param URI
     * @param value
     */
    public SDFNumberConstant(String URI, String value) {
        super(URI,value, SDFDatatypeFactory.getDatatype(SDFDatatypes.Number));
    }

    @Override
    protected void setValue(Object value) {
    	if (value instanceof Number) {
    		this.doubleValue = ((Number)value).doubleValue();
    		this.value = null;
    	}
    }
    
	@Override
	public boolean isDouble() {
		return true;
	}
	
	@Override
	public boolean isString() {
		return false;
	}

}
