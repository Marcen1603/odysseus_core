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
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaAttributeList;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;

/**
 * @author Jonas Jacobi
 */
public class MetadataCreationPO<M extends IMetaAttribute, In extends IMetaAttributeContainer<M>> extends
		AbstractPipe<In, In> implements Serializable{

	private static final long serialVersionUID = 3783851208646530940L;

	private Class<M> type;

	public MetadataCreationPO(Class<M> type) {
		this.type = type;
	}

	public MetadataCreationPO(MetadataCreationPO<M, In> metadataCreationPO) {
		this.type = metadataCreationPO.type;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_next(In object, int port) {
		try {
			object.setMetadata(type.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.transfer(object);
	}

	@Override
	public String toString() {
		return super.toString() + " " + type.getName();
	}

	public Class<M> getType() {
		return type;
	}
	
	@Override
	public MetadataCreationPO<M, In> clone()  {
		return new MetadataCreationPO<M,In>(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof MetadataCreationPO)) {
			return false;
		}
		MetadataCreationPO<?,?> mdcpo = (MetadataCreationPO<?,?>) ipo;
		if(this.getSubscribedToSource().equals(mdcpo.getSubscribedToSource()) && (this.getType().toString().equals(mdcpo.getType().toString()))) {
			return true;
		}
		return false;
	}
		
	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		List<SDFMetaAttribute> metalist = new ArrayList<SDFMetaAttribute>(super.getMetaAttributeSchema().getAttributes());
		SDFMetaAttribute mataAttribute = new SDFMetaAttribute(type);
		if(!metalist.contains(mataAttribute)){
			metalist.add(mataAttribute);
		}
		return new SDFMetaAttributeList(super.getMetaAttributeSchema().getURI(), metalist);
	}		
}
