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
package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.List;
import java.util.Vector;

import de.uniol.inf.is.odysseus.planmanagement.IOperatorOwner;

public abstract class AbstractIterablePipe<R, W> extends AbstractPipe<R, W>
		implements IIterableSource<W> {
	protected List<IOperatorOwner> deactivateRequestControls = new Vector<IOperatorOwner>();
	
	public AbstractIterablePipe(){};
	
	public AbstractIterablePipe(AbstractIterablePipe<R,W> pipe){
		super(pipe);
	}
	
	@Override
	public void removeOwner(IOperatorOwner owner) {
		super.removeOwner(owner);
		this.deactivateRequestControls.remove(owner);
	}
	
	@Override
	public boolean isDone() {
		return super.isDone();
	}
}
