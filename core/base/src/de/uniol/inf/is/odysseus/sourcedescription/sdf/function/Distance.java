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
/**
 * 
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class Distance extends AbstractFunction<Double> {
	private static Double RADIUS = 6367000.0;

	public static void setRadius(double radius) {
		RADIUS = radius;
	}

	@Override
	public int getArity() {
		return 4;
	}

	@Override
	public String getSymbol() {
		return "distance";
	}

	@Override
	public Double getValue() {
		Object fromLat = getInputValue(0);
		Object fromLng = getInputValue(1);
		Object toLat = getInputValue(2);
		Object toLng = getInputValue(3);

		double deltaLatitude = Math.sin(Math
				.toRadians((((Double) fromLat) - ((Double) toLat))) / 2);
		double deltaLongitude = Math.sin(Math
				.toRadians((((Double) fromLng) - ((Double) toLng))) / 2);
		double circleDistance = 2 * Math.asin(Math.min(1, Math
				.sqrt(deltaLatitude * deltaLatitude
						+ Math.cos(Math.toRadians((Double) fromLat))
						* Math.cos(Math.toRadians((Double) toLat))
						* deltaLongitude * deltaLongitude)));
		Double distance = Math.abs(RADIUS * circleDistance);
		return distance;
	}

	@Override
	public String getReturnType() {
		return "Double";
	}

	
	public String[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 3){
			throw new IllegalArgumentException("abs has only 4 arguments.");
		}
		else{
			String[] accTypes = new String[1];
			accTypes[0] = "Double";
			return accTypes;
		}
	}

}
