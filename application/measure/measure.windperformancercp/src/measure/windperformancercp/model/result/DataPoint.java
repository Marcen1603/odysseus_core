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
package measure.windperformancercp.model.result;

import java.sql.Timestamp;

import org.eclipse.swt.graphics.RGB;

public class DataPoint {
	double windSpeed;
	double power;
	double variance;
	RGB color;
	Timestamp tstamp;
	
	public DataPoint(double ws, double p, double var, RGB col, Timestamp t){
		this.windSpeed = ws;
		this.power = p;
		this.variance = var;
		this.color = col;
		this.tstamp = t;
	}
	
	public void setTimeColor(){
		Timestamp actts = new Timestamp(System.currentTimeMillis());
		long diff = Math.abs(actts.getTime() -tstamp.getTime())/1000;
		
		color.blue = Math.max(0, (int)(color.blue - diff));
		color.red = Math.max(0, (int)(color.red -diff));
		color.green = Math.max(0, (int)(color.green -diff));
	}
	
	

}
