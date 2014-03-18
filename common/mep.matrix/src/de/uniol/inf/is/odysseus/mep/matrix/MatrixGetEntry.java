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
package de.uniol.inf.is.odysseus.mep.matrix;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;


/*
 * Diese Klasse dient dazu, einen bestimmten Wert aus einer Matrix auszulesen.
 */
public class MatrixGetEntry extends AbstractFunction<Double>{

	private static final long serialVersionUID = 8960765237278191962L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.INTEGER};

	public MatrixGetEntry() {
		super("MatrixEntry",3,accTypes,SDFDatatype.DOUBLE);
	}
		
	/*
	 * Liefert einen bestimmten Wert aus einer �bergebenen Matrix zur�ck.
	 */
	@Override
	public Double getValue() {
		double d = ((double[][]) getInputValue(0))[((Double)getInputValue(1)).intValue()][((Double)getInputValue(2)).intValue()];
		return d;
	}



	
}
