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
package de.uniol.inf.is.odysseus.mep.functions.time;

import java.util.Calendar;
import java.util.Date;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * Extracts the millisecond part of the date
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class MilliSecondFunction extends AbstractUnaryDateFunction<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3692542445584838749L;

	public MilliSecondFunction() {
		super("millisecond", SDFDatatype.LONG);
	}

	@Override
	public Long getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		long mills = calendar.getTimeInMillis();
		return mills;
	}

}
