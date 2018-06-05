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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleNumberToDateFormat extends NumberFormat {
	
	private static final long serialVersionUID = -2329525234684181945L;
	
	private SimpleDateFormat sdf;

	public SimpleNumberToDateFormat(String datePattern){
		this.sdf = new SimpleDateFormat(datePattern);
	}
	
	
	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		long numberLong = (long)number;
		return this.format(numberLong, toAppendTo, pos);		
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		Date date = new Date(number);
		StringBuffer sb = sdf.format(date, toAppendTo, pos);
		return sb;
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition) {
		return this.sdf.parse(source, parsePosition).getTime();		
	}

}
