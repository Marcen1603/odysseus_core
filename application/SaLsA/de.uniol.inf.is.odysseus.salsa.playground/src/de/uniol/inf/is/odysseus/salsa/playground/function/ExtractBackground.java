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
package de.uniol.inf.is.odysseus.salsa.playground.function;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

/**
 * @author kpancratz
 *
 */
public class ExtractBackground<T> extends AbstractFunction<Geometry> {

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		return null;
	}

	@Override
	public String getSymbol() {
		return null;
	}

	@Override
	public Geometry getValue() {
		return null;
	}

	@Override
	public SDFDatatype getReturnType() {
		return null;
	}

}