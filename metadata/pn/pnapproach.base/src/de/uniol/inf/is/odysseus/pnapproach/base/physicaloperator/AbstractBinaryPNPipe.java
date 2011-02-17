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
package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



public abstract class AbstractBinaryPNPipe<Read extends IMetaAttributeContainer<? extends IPosNeg>, Write extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPNPipe<Read, Write> {
	
	int LEFT = 0;
	int RIGHT = 1;
	
	protected AbstractBinaryPNPipe(){
	}
	
	public AbstractBinaryPNPipe(AbstractBinaryPNPipe<Read,Write> pipe){
		super(pipe);
	}
	

	@Override
	public final void subscribeToSource(ISource<? extends Read> source, int sinkInPort, int sourceOutPort, SDFAttributeList schema) {
		if (sinkInPort != 0 && sinkInPort != 1) {
			throw new IllegalArgumentException("Subscription on illegal port ("
					+ sinkInPort + ") for binary opperator");
		}
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
	}
}
