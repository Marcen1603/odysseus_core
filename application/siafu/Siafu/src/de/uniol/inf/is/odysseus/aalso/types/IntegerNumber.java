/*
 * Copyright NEC Europe Ltd. 2006-2007
 * 
 * This file is part of the context simulator called Siafu.
 * 
 * Siafu is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * Siafu is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.uniol.inf.is.odysseus.aalso.types;


/**
 * This <code>Publishable</code> encapsulates an <code>int</code> primitive
 * and makes it serializable (it can be flattened and blown up again) for use
 * with Siafu.
 * 
 * @author Miquel Martin
 * 
 */
public class IntegerNumber implements Publishable {

	/**
	 * The integer number contained in this object.
	 */
	private int i;

	/**
	 * Build an <code>IntegerNumber</code> encapsulating <code>i</code>.
	 * 
	 * @param i
	 *            the number to use
	 */
	public IntegerNumber(final int i) {
		this.i = i;
	}

	/**
	 * Build an <code>IntegerNumber</code> object by taking the number that
	 * follows the ":" delimiter * of the flattened data object.
	 * 
	 * @param flatData
	 *            the object to base this <code>Text</code> on
	 */
	public IntegerNumber(final FlatData flatData) {
		String data = flatData.getData();
		TypeUtils.check(this, data);
		Integer number = new Integer(data.substring(data.indexOf(':') + 1));
		i = number.intValue();
	}

	/**
	 * Generate a string with the number in the object.
	 * 
	 * @return the string representing the number
	 */
	@Override
    public String toString() {
		return "" + i;
	}

	/**
	 * Get the <code>int</code> encapsulated by this object.
	 * 
	 * @return the <code>int</code> value
	 */
	public int getNumber() {
		return i;
	}

	/**
	 * Generates a flattened version of the data, of the type
	 * <code>IntegerNumber:int</code> where content is the string encapsulated
	 * in the <code>IntegerNumber</code> object.
	 * 
	 * @return a <code>FlatData</code> object representing this object
	 */
	@Override
    public FlatData flatten() {
		String data;
		// We use simple name, because our package is de.nec.nle.siafu.data
		data = this.getClass().getSimpleName() + ":";
		data += i;
		return new FlatData(data);
	}

	@Override
    public boolean equals(Object o) {
		if (!(o instanceof IntegerNumber)) {
			return false;
		}
        IntegerNumber in = (IntegerNumber) o;
        return (in.getNumber() == i);
	}

	@Override
    public int hashCode() {
		return (this.getClass().getName() + i).hashCode();
	}

}
