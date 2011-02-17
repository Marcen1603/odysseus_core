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
package de.uniol.inf.is.odysseus.rcp.viewer.select;

import java.util.Collection;


public interface ISelector<T> {

	public void select( T node );
	public void select( Collection<? extends T> nodes );
	
	public void unselect( T node );
	public void unselect( Collection<? extends T> nodes );
	public void unselectAll();
	
	public Collection<T> getSelected();
	public int getSelectionCount();
	
	public boolean isSelected( T node );

	public void addSelectListener( ISelectListener<T> listener );
	public void removeSelectListener( ISelectListener<T> listener );
	public void notifySelectListener( ISelector<T> sender, Collection<? extends T> selected );
	public void notifyUnselectListener( ISelector<T> sender, Collection<? extends T> unselected );
}
