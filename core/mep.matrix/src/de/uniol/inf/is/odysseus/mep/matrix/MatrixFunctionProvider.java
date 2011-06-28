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
package de.uniol.inf.is.odysseus.mep.matrix;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

public class MatrixFunctionProvider implements IFunctionProvider{

	public MatrixFunctionProvider(){
	}
	
	@Override
	public List<IFunction<?>> getFunctions() {
		
		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		functions.add(new MatrixAdd());
		functions.add(new MatrixGetEntry());
		functions.add(new MatrixInvert());
		functions.add(new MatrixMult());
		functions.add(new MatrixSub());
		functions.add(new MatrixTranspose());
			
		return functions;
	}

}
