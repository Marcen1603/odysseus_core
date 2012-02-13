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
package de.uniol.inf.is.odysseus.broker.console.client;

import net.java.dev.jaxb.array.StringArray;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.broker.console.ConsoleServer;
import de.uniol.inf.is.odysseus.broker.console.ConsoleServerService;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;

public class EclipseConsoleSink extends AbstractSink<Object> {

	private int port;
	private ConsoleServerService service;
	private ConsoleServer server;
	private boolean isConnected = false;

	public EclipseConsoleSink() {

	}

	@Override
	public void setOutputSchema(SDFSchema outputSchema) {
		super.setOutputSchema(outputSchema);
		String[] attrNames = new String[outputSchema.size()];
		for (int i = 0; i < outputSchema.size(); i++) {
			attrNames[i] = outputSchema.getAttribute(i).getAttributeName();
		}
		if(!isConnected){
			connect(attrNames);
		}
	}

	private void connect(String[] attributes) {
		try {			
			this.service = new ConsoleServerService();
			this.server = service.getConsoleServerPort();
			StringArray attArr = new StringArray();
			for (int i = 0; i < attributes.length; i++) {
				attArr.getItem().add(attributes[i]);
			}
			this.port = server.registerView(attArr);
			this.isConnected = true;

		} catch (Exception e) {
			System.err
					.println("The Console Output can not be reached. May be the plugin is missing or view is not opened?");	
			System.err.println("Caused by: "+e.getMessage());
			e.printStackTrace();
			this.isConnected = false;
		}
	}

	@Override
	protected void process_next(Object object, int port, boolean isReadOnly) {		
		if (isConnected) {
			StringArray values = new StringArray();
			// hack because unable to access to relationaltuple...
			String objStr = object.toString();
			String[] parts = objStr.split("\\|");

			try {
				for (String part : parts) {
					values.getItem().add(part);
				}
				server.sendTuple(this.port, values);

			} catch (Exception e) {
				LoggerFactory.getLogger(EclipseConsoleSink.class).warn(
						"Output Console closed");				
				this.isConnected = false;
			}
		}

	}

	@Override
	public void subscribeToSource(ISource<? extends Object> source,
			int sinkInPort, int sourceOutPort, SDFSchema schema) {
		super.subscribeToSource(source, sinkInPort, sourceOutPort, schema);
		this.setOutputSchema(schema);
	}

	@Override
	public EclipseConsoleSink clone()  {
		EclipseConsoleSink newSink = new EclipseConsoleSink();
		return newSink;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		// Nothing to do
	}

}
