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

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MatrixReadFunction extends AbstractReadFunction<double[][]> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7027839983770315809L;
	private static final String DELIMITER = ",";
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

	public MatrixReadFunction() {
		super("readMatrix", 1, accTypes, SDFDatatype.MATRIX_DOUBLE);
	}

	@Override
	public double[][] getValue() {
		String path = getInputValue(0);
		return getValueInternal(path, DELIMITER, new int[] { 0 });
	}

}
