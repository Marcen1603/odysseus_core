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
package de.uniol.inf.is.odysseus.scars.operator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.MetadataCreationPO;

public class MetadataObjectRelationalCreationPO<M extends IProbability> extends MetadataCreationPO<M, MVRelationalTuple<M>> {

	private static final long serialVersionUID = 1L;

	public MetadataObjectRelationalCreationPO(Class<M> type) {
		super(type);
	}
	
	public MetadataObjectRelationalCreationPO(MetadataObjectRelationalCreationPO<M> po) {
		super(po);
	}
	
	@Override
	public void process_next(MVRelationalTuple<M> object, int port) {
		try {
			assignMetadata(object);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.transfer(object);
	};
	
	@SuppressWarnings("unchecked")
	private void assignMetadata(Object tuple ){
		try {
			if( tuple instanceof IMetaAttributeContainer ) {
				((IMetaAttributeContainer<M>)tuple).setMetadata(getType().newInstance());
			}
			if( tuple instanceof MVRelationalTuple<?>) {
				MVRelationalTuple<M> t = (MVRelationalTuple<M>) tuple;
				for( int i = 0; i < t.getAttributeCount(); i++ ) {
					assignMetadata(t.getAttribute(i));
				}
			}
		} catch( IllegalAccessException ex ) {
			ex.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
}
