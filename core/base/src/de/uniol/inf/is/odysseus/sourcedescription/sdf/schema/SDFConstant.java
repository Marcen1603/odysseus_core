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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

public abstract class SDFConstant extends SDFSchemaElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3115993645233098070L;
	/**
	 * @uml.property  name="value"
	 */
	String value = null;
	protected Double doubleValue;

	public SDFConstant(String URI, String value, SDFDatatype type) {
		super(URI, type);
		this.value = value;
	}

	public abstract boolean isString();
	public abstract boolean isDouble();
	
	public String getString() {
		return value.toString();
	}

	public double getDouble() {
		//cache double conversion
		if (this.doubleValue == null) {
			this.doubleValue = Double.parseDouble(value);
		}
		return this.doubleValue;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(" Konstantenwert:");
		if (value != null)
			ret.append(value.toString());
		return ret.toString();
	}
	
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
	public boolean equals(Object obj) {
        if (obj instanceof SDFConstant){
             //System.out.println("SDFConstant:equals this "+this.getString()+" == "+((SDFConstant)obj).getString());
             return ((SDFConstant)obj).getString().equals(this.getString());
        }
        //System.out.println("SDFConstant:equals this "+this.getString()+" "+obj);
        return false;
    }

	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		xmlRetValue.append(indent);
		xmlRetValue.append(value);
	}
	
	protected void setValue(Object value) {
		if (value instanceof Number) {
			this.doubleValue = ((Number)value).doubleValue();
			this.value = ((Number)value).toString();
			return;
		}
		
		if (value instanceof String) {
			this.doubleValue = null;
			this.value = (String) value;
			return;
		}
		
		throw new IllegalArgumentException("Invalid type of value");
	}
}