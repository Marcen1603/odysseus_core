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
package de.uniol.inf.is.odysseus.labdataserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BettenGenerator {

	public static void main(String[] args) throws IOException {
		for (int bett=1;bett<=10;bett++){
			FileWriter writer = new FileWriter(new File("labdata_cfg/ideaal_bett"+bett+".csv"));
			System.out.println("Bett "+bett);
			writer.write("timestamp;id;weight0;weight1;weight2;weight3\n");
			for (int messung=1;messung <= 10000; messung++){
				writer.write(messung+";"+bett+";"+Math.round(Math.random()*60.0*100)/100.0+";"+Math.round(Math.random()*60.0*100)/100.0+";"+Math.round(Math.random()*60.0*100)/100.0+";"+Math.round(Math.random()*60.0*100)/100.0+"\n");
			}
			writer.flush();
		}
	}
}
