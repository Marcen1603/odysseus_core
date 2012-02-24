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
package de.uniol.inf.is.odysseus.core.server.metadata;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;


/**
 * @author Jonas Jacobi
 */
public class MetaAttributeContainer<T extends IMetaAttribute> implements IMetaAttributeContainer<T> {
	private static final long serialVersionUID = -4027708515386331790L;
	
	private T metadata;

	public MetaAttributeContainer() {
		this.metadata = null;
	}

	public MetaAttributeContainer(T data) {
		this.metadata = data;
	}

	@SuppressWarnings("unchecked")
	public MetaAttributeContainer(MetaAttributeContainer<T> copy) {
		if (copy.metadata != null) {
			this.metadata = (T) copy.metadata.clone();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.dynaquest.queryexecution.po.base.IMetaAttribute#getMetadata()
	 */
	@Override
	public final T getMetadata() {
		return metadata;
	}

	@Override
	public MetaAttributeContainer<T> clone() {
		return new MetaAttributeContainer<T>(this);
	}

	@Override
	public void setMetadata(T metadata) {
		this.metadata = metadata;
	}
}
