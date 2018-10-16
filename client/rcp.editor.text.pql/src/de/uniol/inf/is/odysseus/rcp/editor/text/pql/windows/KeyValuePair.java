/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.rcp.editor.text.pql.windows;

import com.google.common.base.Preconditions;

class KeyValuePair {

	private String key;
	private String value;

	public KeyValuePair() {
		this("key", "value");
	}

	public KeyValuePair(String key, String value) {
		setKey(key);
		setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key; // Preconditions.checkNotNull(key, "Key of key-value-pair must not be null!");
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value; // Preconditions.checkNotNull(value, "Value of key-value-pair must not be null!");
	}

}
