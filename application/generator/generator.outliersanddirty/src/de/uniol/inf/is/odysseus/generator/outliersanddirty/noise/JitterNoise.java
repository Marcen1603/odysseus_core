/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.generator.outliersanddirty.noise;

import java.util.Random;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.06.2011
 */
public class JitterNoise implements INoise {

	private double schwankung;

	public JitterNoise(double wertschwankung){
		this.schwankung = wertschwankung;
	}
	
	@Override
	public double addNoise(double value) {
		Random rand = new Random();
		double plusMinus = (rand.nextDouble() * schwankung)/2;
		if(rand.nextBoolean()){
			return value+plusMinus;
		}
        return value-plusMinus;
	}

}
