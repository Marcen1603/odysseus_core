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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeParameter extends
		AbstractParameter<TimeValueItem> {

	private static final long serialVersionUID = -3129785072529123574L;

	@Override
	protected void internalAssignment() {
		if( inputValue instanceof TimeValueItem ) {
			setValue((TimeValueItem)inputValue);
			return;
		}
		
		long value;
		TimeUnit unit;
		if (inputValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> in = ((List<Object>) inputValue);
			if (in.size() == 2) {
				if(in.get(0) instanceof Long){
					value = (Long)in.get(0);
				}else{
					value = Long.parseLong(in.get(0).toString());
				}
				unit = TimeUnit.valueOf(((String)in.get(1)).toUpperCase());
			} else {
				throw new RuntimeException(
						" Could not determine value/unit pair!");
			}
		} else {
			unit = null;
			value = (Long)inputValue;
		}
		TimeValueItem timeValueItem = new TimeValueItem(value, unit);
		setValue(timeValueItem);

	}

	@Override
	protected String getPQLStringInternal() {
		if( inputValue instanceof TimeValueItem) {
			TimeValueItem item = (TimeValueItem)inputValue;
			return "[" + item.getTime() + ", '" + item.getUnit().toString() + "']";
		} else if (inputValue instanceof List) {
			@SuppressWarnings("unchecked")
			List<Object> in = ((List<Object>) inputValue);
			if (in.size() == 2) {
				return "[" + in.get(0) + ", '" + in.get(1) + "']";
			} 
			throw new RuntimeException(" Could not determine value/unit pair!");
		} 
		return "" + inputValue;
	}
}
