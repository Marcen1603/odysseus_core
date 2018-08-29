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
package de.uniol.inf.is.odysseus.wrapper.mosaik.logicaloperator;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "MOSAIK", doc="operator to connect to mosaik.", category={LogicalOperatorCategory.SOURCE})
public class MosaikAccessAO extends AbstractAccessAO {
	private static final long serialVersionUID = -6949561307060489773L;
	
	private String type;

	public MosaikAccessAO() {
		super();
		this.init();
	}

	public MosaikAccessAO(MosaikAccessAO other){
		super(other);
	}
	
	public MosaikAccessAO(Resource resource, String wrapper, String transport,
			String protocol, String datahandler, OptionMap options) {
		super(resource, wrapper, transport, protocol, datahandler, options);
		this.init();
	}
	
	@Parameter(type = StringParameter.class, name = "TYPE", optional = true, isList = false, doc = "use zeromq or simapi")
	public void setType(String apiType) {
		this.type = apiType;
		this.init();
	}

	@GetParameter(name = "TYPE")
	public String getType() {
		return this.type;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new MosaikAccessAO(this);
	}
	
	private void init() {
		OptionMap options = new OptionMap();
		if(this.type != null && this.type.equalsIgnoreCase("simapi")) {
			this.setWrapper("GenericPush");
			this.setTransportHandler("TCPServer");
			this.setProtocolHandler("Mosaik");
			this.setDataHandler("KeyValueObject");
			
			if(!options.containsKey("port")) {
				options.setOption("port", "5553");
			}
			if(!options.containsKey("mosaikPort")) {
				options.setOption("mosaikPort", "5554");
			}
			if(!options.containsKey("host")) {
				options.setOption("host","127.0.0.1");
			}
		} else if(this.type != null && this.type.equalsIgnoreCase("zeromq")) {
			this.setWrapper("GenericPush");
			this.setTransportHandler("ZeroMQ");
			this.setProtocolHandler("JSON");
			this.setDataHandler("KeyValueObject");

			if(!options.containsKey("host")) {
				options.setOption("host","127.0.0.1");
			}
			if(!options.containsKey("readport")) {
				options.setOption("readport", "5558");
			}
			if(!options.containsKey("writeport")) {
				options.setOption("writeport", "5559");
			}
			if(!options.containsKey("delayofmsg")) {
				options.setOption("delayofmsg", "0");
			}
			if(!options.containsKey("threads")) {
				options.setOption("threads", "1");
			}
			if(!options.containsKey("subscriptionfilter")) {
				options.setOption("subscriptionfilter", "");
			}
			if(!options.containsKey("basetimeunit")) {
				options.setOption("basetimeunit", "SECONDS");
			}
		} 
		if(!options.containsKey("byteorder")) {
			options.setOption("byteorder", "LittleEndian");		
		}
		this.setOptionMap(options);
		
//		List<SDFAttribute> schemaAttributes = new ArrayList<>();
//		schemaAttributes.add(new SDFAttribute("timestamp", "timestamp", SDFDatatype.START_TIMESTAMP));
//		this.setOutputSchema(SDFSchemaFactory.createNewSchema("", (Class<? extends IStreamObject<?>>) KeyValueObject.class, schemaAttributes));
	}

}
