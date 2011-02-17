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
package de.uniol.inf.is.odysseus.generator.syntheticclusters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class SyntheticClusterProvider extends StreamClientHandler {

	// CREATE STREAM clusters (timestamp STARTTIMESTAMP, valX DOUBLE, valY DOUBLE) CHANNEL localhost : 54321;
	
	
	private long time = 0;
	private int numberOfPotentialClusters = 4; // gerade anzahl
	private double distanceBetweenPeeks = 70.0d;

	private double standardabweichung = 10.0;

	private Random rand = new Random();
	private double[] peekX;
	private double[] peekY;

	@Override
	public void init() {
		System.out.println("Creating clusters: " + numberOfPotentialClusters);
		System.out.println("... with distance between means: " + distanceBetweenPeeks);
		System.out.println("... with standard derivation: " + standardabweichung);
		int perLine = (int) Math.ceil(Math.sqrt(numberOfPotentialClusters));

		peekX = new double[numberOfPotentialClusters];
		peekY = new double[numberOfPotentialClusters];

		int row = 1;
		int col = 1;
		for (int i = 0; i < numberOfPotentialClusters; i++) {
			peekX[i] = (row) * distanceBetweenPeeks;
			peekY[i] = (col) * distanceBetweenPeeks;
			System.out.println(" "+(i+1)+". Mean: " + peekX[i] + ":" + peekY[i]);
			
			if ((col % perLine) == 0) {
				row++;
				col = 1;
			}else{
				col++;
			}
		}

		// int line = 1;
		// for(int i=0;i<numberOfPotentialClusters;i++){
		// peekX[i] = (i+1)*distanceBetweenPeeks;
		// peekY[i] = line*distanceBetweenPeeks;
		//
		// if(((i+1)%perLine)==0){
		// line++;
		// }
		// }
	}

	@Override
	public void close() {

	}

	@Override
	public List<DataTuple> next() {
		DataTuple tuple = new DataTuple();
		int nextCluster = rand.nextInt(numberOfPotentialClusters);

		double valX = standardabweichung * rand.nextGaussian() + peekX[nextCluster];
		double valY = standardabweichung * rand.nextGaussian() + peekY[nextCluster];

		tuple.addAttribute(new Long(time));
		tuple.addAttribute(new Double(valX));
		tuple.addAttribute(new Double(valY));

		time++;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

}
