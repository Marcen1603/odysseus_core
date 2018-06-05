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

/**
 * Describes an object which will be informed if an entry of
 * {@link Configuration} is modified.
 * 
 * @author Wolf Bauer
 * 
 * @param <T>
 *            Type of the supported entries.
 */
public interface IValueChangeListener<T extends ISetting<?>> {
	/**
	 * Informs this object that an entry is modified.
	 * 
	 * @param newValueContainer
	 *            The entry that is modified.
	 */
	public void settingChanged(T newValueContainer);
}
