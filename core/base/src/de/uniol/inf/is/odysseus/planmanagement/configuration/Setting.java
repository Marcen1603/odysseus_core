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
package de.uniol.inf.is.odysseus.planmanagement.configuration;

public class Setting<E> implements ISetting<E> {
	/**
	 * Value of the setting.
	 */
	private E value = null;

	/**
	 * Creates a new execution setting.
	 * 
	 * @param value
	 *            value of the setting.
	 */
	protected Setting(E value) {
		setValue(value);
	}

	/**
	 * Sets the value of this setting.
	 * 
	 * @param value
	 *            new value of this setting.
	 */
	protected void setValue(E value) {
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.configuration.ISetting#getValue()
	 */
	@Override
	public E getValue() {
		return value;
	}
}
