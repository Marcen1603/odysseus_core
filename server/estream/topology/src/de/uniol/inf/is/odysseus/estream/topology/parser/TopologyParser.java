package de.uniol.inf.is.odysseus.estream.topology.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.estream.topology.model.LinkTopologyModel;
import de.uniol.inf.is.odysseus.estream.topology.model.SmartmeterTopologyModel;

/**
 * 
 * Parser for parsing JSON-formated Topology-Data
 * 
 * @author Marcel Hamacher
 *
 */
public class TopologyParser<T extends IStreamObject<IMetaAttribute>> extends AbstractTopologyParser<T> {

	private static final Logger log = LoggerFactory.getLogger(TopologyParser.class);
	private boolean err;

	public TopologyParser(InputStream inputStream, SDFSchema schema, String type) {
		super(inputStream, schema, type);
	}

	private JsonNode root;
	private List<SmartmeterTopologyModel> tupels;
	private List<LinkTopologyModel> links;

	@Override
	protected T next() {
		if (this.type.equalsIgnoreCase("smartmeters")) {
			for (int i = 0; i < tupels.size();) {
				if (schema.size() > 0) {
					for (int j = 0; j < schema.size(); j++) {
						String attr = schema.get(j).getAttributeName();
						if (!(tupels.get(i).getByName(attr.toLowerCase()) != null) && !err) {
							log.error("Attribute " + schema.get(j).getAttributeName()
									+ " not part of topology or not implemented!");
							err = true;
						}
						setValue(attr, tupels.get(i).getByName(attr.toLowerCase()));
					}
				} else {
					log.error("Schema not given!");
				}

				tupels.remove(i);

				if (tupels.size() < 0) {
					this.isDone = true;
				}
				return getTuple();
			}
		} else if (this.type.equalsIgnoreCase("links")) {
			for (int i = 0; i < links.size();) {
				if (schema.size() > 0) {
					for (int j = 0; j < schema.size(); j++) {
						String attr = schema.get(j).getAttributeName();
						if (!(links.get(i).getByName(attr.toLowerCase()) != null) && !err) {
							log.error("Attribute " + schema.get(j).getAttributeName()
									+ " not part of topology or not implemented!");
							err = true;
						}
						setValue(attr, links.get(i).getByName(attr.toLowerCase()));
					}
				} else {
					log.error("Schema not given!");
				}

				links.remove(i);

				if (links.size() < 0) {
					this.isDone = true;
				}
				return getTuple();
			}
		}
		return null;
	}

	@Override
	protected void init(InputStream inputStream) {
		super.inputStream = inputStream;
		try {
			root = new ObjectMapper().readTree(new JsonFactory().createParser(new InputStreamReader(inputStream)));

			err = false;
			if (this.type.equals("smartmeters")) {
				createSmartmeters();
			} else if (this.type.equals("links")) {
				createLinks();
			} else {
				throw new IllegalArgumentException("No implementation for type: " + type + "!");
			}
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
	}

	protected List<SmartmeterTopologyModel> createSmartmeters() {
		tupels = new ArrayList<>();
		if (root.isArray()) {
			for (JsonNode element : root) {
				JsonNode smgw = element.get("smgw");
				if (smgw.isArray()) {
					for (JsonNode gateway_ : smgw) {
						String gateway = gateway_.get("id").asText();
						String node = gateway_.get("node").asText();
						String smartmeter = null;
						String model = null;
						String smtype = null;
						String country = "none";
						String city = "none";
						String postcode = "none";
						String coordinate = null;

						JsonNode sm_gateway = gateway_.get("smartmeters");
						for (JsonNode smartmeter_ : sm_gateway) {
							smartmeter = smartmeter_.get("id").asText();
							JsonNode models = smartmeter_.get("models");

							for (JsonNode model_ : models) {
								model = model_.get("name").asText();
								if (!model.equalsIgnoreCase("PV")) {
									smtype = "consumer";
								} else {
									smtype = "producer";
								}
							}
						}

						if (gateway_.get("tags").get("addr:country") != null) {
							country = gateway_.get("tags").get("addr:country").asText();
						}
						if (gateway_.get("tags").get("addr:city") != null) {
							city = gateway_.get("tags").get("addr:city").asText();
						}
						if (gateway_.get("tags").get("addr:postcode") != null) {
							postcode = gateway_.get("tags").get("addr:postcode").asText();
						}
						coordinate = (gateway_.get("coordinates").get("lat").asText() + ", "
								+ gateway_.get("coordinates").get("lon").asText());

						tupels.add(new SmartmeterTopologyModel(smartmeter, model, gateway, node, smtype, country, city,
								postcode, coordinate));
					}
				}
			}
		}
		return tupels;
	}

	protected List<LinkTopologyModel> createLinks() {
		links = new ArrayList<>();
		if (root.isArray()) {
			for (JsonNode element : root) {
				JsonNode links_ = element.get("links");
				for (JsonNode singleLink : links_) {
					String source = singleLink.get("source").asText();
					String target = singleLink.get("target").asText();

					links.add(new LinkTopologyModel(source, target));
				}
			}
		}
		return links;
	}

}
