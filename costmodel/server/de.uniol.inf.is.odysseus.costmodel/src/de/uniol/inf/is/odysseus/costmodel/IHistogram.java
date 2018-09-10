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
package de.uniol.inf.is.odysseus.costmodel;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public interface IHistogram extends Cloneable {

	public SDFAttribute getAttribute();
	
	public double getMinimum();
	public double getMaximum();
	
	public double getOccurences(double value);
	public double getOccurenceRange(double from, double to);
	
	public double getValueCount();
	public int getIntervalCount();
	public double[] getIntervalBorders();
	public double[] getAllOccurences();
	
	public IHistogram clone();
	public IHistogram cutLower(double value);
	public IHistogram cutHigher(double value);
	public IHistogram toRelative();
	public IHistogram toAbsolute(double countNum);
	
	public boolean isRelative();
	public IHistogram normalize();
}
