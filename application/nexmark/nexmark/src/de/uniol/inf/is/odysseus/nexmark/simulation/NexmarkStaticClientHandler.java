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
package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.conversion.CSVParser;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.nexmark.NexmarkApplication;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;


/**
 * Der NexmarkClientHandler bearbeitet eingehende Verbindungen zum Nexmark
 * Benchmark Server
 * 
 * @see NexmarkServer
 */
public class NexmarkStaticClientHandler extends Thread {

	private String document;

	private NEXMarkClient client;

	/**
	 * Erzeugt einen Client Handler um eine Verbindung zum Nexmark Benchmark
	 * Server zu bearbeiten
	 * 
	 * @param connection
	 *            - Socket der Verbindung
	 */
	public NexmarkStaticClientHandler(Socket connection, NEXMarkClient client) {
		this.client = client;
	}

	/**
	 * Bearbeitet eine Anfrage an den Nexmark Benchmark Server. In den
	 * OutputStream des Socktes werden die Simulationsdaten geschrieben.
	 */
	@Override
	public void run() {

		try {
			if (client.streamType == NEXMarkStreamType.CATEGORY) {
				document = NexmarkApplication.getCategoryFile();

			}
			if (document != null) {
				
				List<SDFDatatype> categorySchema = new ArrayList<>();
				categorySchema.add(SDFDatatype.INTEGER);
				categorySchema.add(SDFDatatype.STRING);
				categorySchema.add(SDFDatatype.STRING);
				categorySchema.add(SDFDatatype.INTEGER);
				
				TupleDataHandler dh = (TupleDataHandler) new TupleDataHandler().createInstance(categorySchema);
				String[] lines = document.split("\\n");
				for (int i=0;i<lines.length;i++){
					List<String> input = CSVParser.parseCSV(lines[i], '\t', true);
					@SuppressWarnings("unchecked")
					Tuple<ITimeInterval> tuple = (Tuple<ITimeInterval>)dh.readData(input.iterator());
					client.writeObject(tuple, true);
				}
				client.writeObject(null, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}


}
