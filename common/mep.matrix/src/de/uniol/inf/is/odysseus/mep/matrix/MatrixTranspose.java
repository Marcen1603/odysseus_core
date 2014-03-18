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

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;


/*
 * Diese Klasse dient dazu, aus einer �bergebenen Matrix die Transponierte zu berechnen.
 */
public class MatrixTranspose extends AbstractFunction<Object> {

	private static final long serialVersionUID = 4225196063477856350L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.MATRIX_DOUBLE};

	public MatrixTranspose() {
		super("MatrixTrans",1,accTypes,SDFDatatype.MATRIX_DOUBLE);
	}	

	/*
	 * Liefert die Transponierte der �bergebenen Matrix zur�ck.
	 */
	@Override
	public Object getValue() {
		double[][] v = new RealMatrixImpl((double[][]) getInputValue(0)).transpose().getData();
		return v;
	}

	
}
