/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.udf;

import de.uniol.inf.is.odysseus.context.store.ContextStore;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.physicaloperator.IUserDefinedFunction;

/**
 * 
 * @author Dennis Geesen
 * Created at: 06.02.2012
 */
@UserDefinedFunction(name="STORE_CONTEXT")
public class SimpleStoreOperator<R> implements IUserDefinedFunction<R, R> {

	private String storeName;
	
	@Override
	public void init(String initString) {
		this.storeName = initString;				
	}

	@Override
	public R process(R in, int port) {
		ContextStore.getInstance().insertValue(storeName, in); 
		return in;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
}
