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
 * Extracts the hour part of the date
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class HourFunction extends AbstractUnaryDateFunction<Integer>{

	private static final long serialVersionUID = -622255720592751554L;

	public HourFunction() {
		super("hour", SDFDatatype.INTEGER);
	}
	
	@Override
	public Integer getValue() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getInputValue(0));
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

}
