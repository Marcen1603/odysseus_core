package de.uniol.inf.is.odysseus.estream.topology.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.estream.topology.model.TopologyData;

/**
 * 
 * Parser for parsing JSON-formated Topology-Data
 * 
 * @author Marcel Hamacher
 *
 */
public class TopologyParser<T extends IStreamObject<IMetaAttribute>> extends AbstractTopologyParser<T> {

	private static final Logger log = LoggerFactory.getLogger(TopologyParser.class);

	public TopologyParser(InputStream inputStream, SDFSchema schema) {
		super(inputStream, schema);
	}

	private JsonNode root;
	private List<TopologyData> tupels;
	
	protected List createList() {
		tupels = new ArrayList<>();
		if (root.isArray()) {
			for (JsonNode element : root) {
				JsonNode smgw = element.get("smgw");
				if (smgw.isArray()) {
					for (JsonNode gateway : smgw) {
						String gateway_id = gateway.get("id").asText();
						String node_id = gateway.get("node").asText();
						String model = "none";
						String type = "producer";

						JsonNode sm_gateway = gateway.get("smartmeters");

						for (JsonNode smartmeter : sm_gateway) {
							
							JsonNode models = smartmeter.get("models");
							
							for (JsonNode model_ : models) {
								model = model_.get("name").asText();
								if (model.equalsIgnoreCase("PV")) {
									type = "consumer";
								}
							}
							
							tupels.add(new TopologyData(smartmeter.get("id").asText(), model, gateway_id, node_id, "TODO", type));
						}
					}
				}
			}
		}
		return tupels;
	}
	
	@Override
	protected T next() {
		for (int i = 0; i < tupels.size();) {
			setValue("smgw_id", tupels.get(i).getSmgw_id());
			setValue("smart_id", tupels.get(i).getSm_id());
			setValue("node", tupels.get(i).getNode());
			setValue("model", tupels.get(i).getModel());
			// setValue("transformator", tupels.get(i).getTransformator());
			setValue("type", tupels.get(i).getType());
			tupels.remove(i);
			
			if (tupels.size() < 0) {
				this.isDone = true;
			}
			return getTuple();
		}
		return null;

	}

	@Override
	protected void init(InputStream inputStream) {
		super.inputStream = inputStream;
		try {
			root = new ObjectMapper().readTree(new JsonFactory().createParser(new InputStreamReader(inputStream)));
			
			createList();
		} catch (JsonParseException e) {
			log.debug(e.getMessage());
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage());
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

}
