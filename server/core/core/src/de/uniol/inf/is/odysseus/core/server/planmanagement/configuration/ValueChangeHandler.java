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
package de.uniol.inf.is.odysseus.core.server.planmanagement.configuration;

import java.io.Serializable;
import java.util.ArrayList;

public class ValueChangeHandler<T extends ISetting<?>> implements IValueChangeHandler<T>, Serializable {

	private static final long serialVersionUID = -297686698027444767L;
	/**
	 * List of objects which are informed if an entry value changes.
	 */
	private ArrayList<IValueChangeListener<T>> changeListener = new ArrayList<IValueChangeListener<T>>();
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IValueChangeHandler#addValueChangeListener(de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IValueChangeListener)
	 */
	@Override
	public void addValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.add(listener);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IValueChangeHandler#removeValueChangeListener(de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IValueChangeListener)
	 */
	@Override
	public void removeValueChangeListener(IValueChangeListener<T> listener) {
		this.changeListener.remove(listener);
	}
	
	/**
	 * Sends a setting changed event to all registered listeners.
	 * 
	 * @param newEntry Entry which changed. 
	 */
	protected void settingChanged(T newEntry) {
		for (IValueChangeListener<T> listener : this.changeListener) {
			listener.settingChanged(newEntry);
		}
	}
	

}
