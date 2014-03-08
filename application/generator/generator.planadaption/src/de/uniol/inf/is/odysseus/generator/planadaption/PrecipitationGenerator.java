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
package de.uniol.inf.is.odysseus.generator.planadaption;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.error.ContinuousErrorModel;
import de.uniol.inf.is.odysseus.generator.error.NoError;
import de.uniol.inf.is.odysseus.generator.noise.DuplicateNoise;
import de.uniol.inf.is.odysseus.generator.valuegenerator.ISingleValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.PredifinedValueGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.distribution.UniformDistributionGenerator;
import de.uniol.inf.is.odysseus.generator.valuegenerator.evolve.IncreaseGenerator;

/**
 * 
 * Niederschlag
 * 
 * @author Merlin Wasmann
 *
 */
public class PrecipitationGenerator extends AbstractDataGenerator {

	private final static int CHANGE_MARKER = 5;
	@SuppressWarnings("unused")
	private final static int MAX_ELEMENTS = 40;
	private final static int SLEEP = 5000;
	private int tupleCounter = 0;
	private boolean changed = false;
	
	// ValueGenerators
	private ISingleValueGenerator time;
	private ISingleValueGenerator precipitation;
	private ISingleValueGenerator location;
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.generator.StreamClientHandler#init()
	 */
	@Override
	public void process_init() {
		// CREATE STREAM precipitation (timestamp LONG, precipitation INTEGER, name STRING, location INTEGER) CHANNEL localhost : 57005;
				/**
				 * precipitation ::= ACCESS({
				 * 		source='precipitation', 
				 * 		wrapper='GenericPush',
				 * 		transport='NonBlockingTCP', 
				 * 		protocol='SizeByteBuffer',
				 * 		dataHandler='Tuple', 
				 * 		options=[
				 * 			['host', 'localhost'], 
				 * 			['port', '57005'],
				 * 			['ByteOrder', 'LittleEndian']
				 * 			], 
				 * 		schema=[
				 * 			['timestamp', 'STARTTIMESTAMP'], 
				 * 			['precipitation', 'INTEGER'], 
				 * 			['name', 'STRING'],
				 * 			['location', 'INTEGER']
				 * 			]})
				 **/

				// Time
				time = new IncreaseGenerator(new NoError(), 0, SLEEP);
				time.init();

				// Temperature
				precipitation = new UniformDistributionGenerator(
						new ContinuousErrorModel(new DuplicateNoise(), 3), 0, 30);
				precipitation.init();

				location = new PredifinedValueGenerator(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
				location.init();
				
				getRunner().setDelay(SLEEP);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.generator.StreamClientHandler#close()
	 */
	@Override
	public void close() {

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.generator.StreamClientHandler#next()
	 */
	@Override
	public List<DataTuple> next() throws InterruptedException {
//		if(tupleCounter >= MAX_ELEMENTS) {
//			System.out.println("MAX_ELEMENTS reached for " + this.getClass().getSimpleName() + ". Stopping Service.");
//			return null;
//		}
		
		// change value range of temperature when tupleCounter hits
		// CHANGE_MARKER
		if (tupleCounter >= CHANGE_MARKER && !changed) {
			System.out.println("CHANGE_MARKER reached for " + this.getClass().getSimpleName() + ". Changing value range.");
			precipitation = new UniformDistributionGenerator(
					new ContinuousErrorModel(new DuplicateNoise(), 3), 25, 50);
			precipitation.init();

			changed = true;
			// TODO (Merlin): vll noch den Sleep des Threads ändern
		}

		DataTuple tuple = new DataTuple();

		tuple.addLong(time.nextValue());
		tuple.addInteger(precipitation.nextValue());
		tuple.addString("precipitation");
		tuple.addInteger(location.nextValue());

//		try {
//			Thread.sleep(SLEEP);
//		} catch (InterruptedException ex) {
//			ex.printStackTrace();
//		}

		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);

		tupleCounter++;

		return list;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.generator.StreamClientHandler#clone()
	 */
	@Override
	public PrecipitationGenerator newCleanInstance() {
		PrecipitationGenerator clone = new PrecipitationGenerator();
		clone.tupleCounter = this.tupleCounter;
		clone.changed = this.changed;
		clone.precipitation = this.precipitation;
		clone.location = this.location;
		clone.time = this.time;
		return clone;
	}

}
