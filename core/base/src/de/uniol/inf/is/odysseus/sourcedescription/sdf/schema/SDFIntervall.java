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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

/**
 * Dient dazu ein Intervall mit einer oberen und einer unteren Grenze
 * abzubilden, wobei die Grenzen entweder enthalten sind, oder nicht
 */

// ToDo Fehlerueberpruefung beim Intervall
public class SDFIntervall extends SDFElement{

    /**
	 * 
	 */
	private static final long serialVersionUID = 2455613297854275011L;

	/**
	 * @uml.property  name="leftBorder"
	 */
    // Linke Seite des Intervalls (ein Double, damit hier auch
    // null, d.h. unendlich stehen kann)
    private Double leftBorder = null;

    /**
	 * @uml.property  name="leftIsOpen"
	 */
    private boolean leftIsOpen = true;

    /**
	 * @uml.property  name="rightBorder"
	 */
    // rechte Seite
    private Double rightBorder = null;

    /**
	 * @uml.property  name="rightIsOpen"
	 */
    private boolean rightIsOpen = true;


	public SDFIntervall(String uri, Double leftBorder, boolean leftIsOpen,
			Double rightBorder, boolean rightIsOpen) {
		super(uri);
		this.leftBorder = leftBorder;
		this.rightBorder = rightBorder;
		this.leftIsOpen = leftIsOpen;
		this.rightIsOpen = rightIsOpen;
	}
	
	public SDFIntervall(String uri){
		super(uri);
	}

    /**
     * 
     * @uml.property name="leftBorder"
     */
    public void setLeftBorder(Double leftBorder) {
        this.leftBorder = leftBorder;
    }
    
	public void setLeftBorder(double i, boolean b) {
		setLeftBorder(i);
		setLeftIsOpen(b);		
	}

    /**
     * 
     * @uml.property name="leftBorder"
     */
    public Double getLeftBorder() {
        return leftBorder;
    }

    /**
     * 
     * @uml.property name="leftIsOpen"
     */
    public void setLeftIsOpen(boolean leftIsOpen) {
        this.leftIsOpen = leftIsOpen;
    }

    /**
     * 
     * @uml.property name="leftIsOpen"
     */
    public boolean isLeftIsOpen() {
        return leftIsOpen;
    }

    /**
     * 
     * @uml.property name="rightBorder"
     */
    public void setRightBorder(Double rightBorder) {
        this.rightBorder = rightBorder;
    }
    
	public void setRightBorder(double i, boolean b) {
		setRightBorder(i);
		setRightIsOpen(b);		
	}


    /**
     * 
     * @uml.property name="rightBorder"
     */
    public Double getRightBorder() {
        return rightBorder;
    }

    /**
     * 
     * @uml.property name="rightIsOpen"
     */
    public void setRightIsOpen(boolean rightIsOpen) {
        this.rightIsOpen = rightIsOpen;
    }

    /**
     * 
     * @uml.property name="rightIsOpen"
     */
    public boolean isRightIsOpen() {
        return rightIsOpen;
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		if (this.leftIsOpen)
			ret.append("]");
		else
			ret.append("[");
		if (this.leftBorder != null)
			ret.append(this.leftBorder);
		ret.append(", ");
		if (this.rightBorder != null)
			ret.append(this.rightBorder);
		if (this.rightIsOpen)
			ret.append("[");
		else
			ret.append("]");
		return ret.toString();
	}


}