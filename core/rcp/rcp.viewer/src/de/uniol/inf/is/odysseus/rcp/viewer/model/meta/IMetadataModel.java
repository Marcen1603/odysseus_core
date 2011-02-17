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
package de.uniol.inf.is.odysseus.rcp.viewer.model.meta;

import java.util.Collection;

public interface IMetadataModel<M> {

	public Collection<String> getProvidedMetadataTypes();
	public M getMetadataItem( String metadataType );
	public void addMetadataItem( String type, M metaDataItem );
	public void removeMetadataItem( String type );
	public void resetMetadataItem( String type );

	public void addMetadataChangeListener( IMetadataChangeListener<M> listener );
	public void removeMetadataChangeListener( IMetadataChangeListener<M> listener );
	public void notifyMetadataChangedListener( String changedType );
}
