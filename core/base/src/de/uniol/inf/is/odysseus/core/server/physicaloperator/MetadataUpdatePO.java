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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;

/**
 * @author Jonas Jacobi
 */
public class MetadataUpdatePO<M extends IClone, T extends IMetaAttributeContainer<? extends M>> extends AbstractPipe<T, T>{

	private IMetadataUpdater<M, T> metadataFactory;

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	public MetadataUpdatePO(IMetadataUpdater<M, T> mFac) {
		super();
		this.metadataFactory = mFac;
		this.setName(getName()+" "+mFac.getName());
	}

	public MetadataUpdatePO(MetadataUpdatePO<M, T> metadataUpdatePO) {
		this.metadataFactory = metadataUpdatePO.metadataFactory;
	}

	@Override
	protected void process_next(T object, int port) {
		this.metadataFactory.updateMetadata(object);
		transfer(object);
	}
	
	@Override
	public MetadataUpdatePO<M, T> clone() {
		return new MetadataUpdatePO<M,T>(this);
	}
	
	@Override
	public String toString(){
		return super.toString() + " updateFac: " + this.metadataFactory.getClass();
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof MetadataUpdatePO)) {
			return false;
		}
		MetadataUpdatePO<?,?> mdupo = (MetadataUpdatePO<?,?>) ipo;
		if(this.hasSameSources(mdupo) &&
				this.metadataFactory.getName().equals(mdupo.metadataFactory.getName())) {
			return true;
		}
		return false;
	}
}
