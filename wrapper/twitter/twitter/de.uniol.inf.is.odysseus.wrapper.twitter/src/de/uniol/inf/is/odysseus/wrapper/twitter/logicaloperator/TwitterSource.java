package de.uniol.inf.is.odysseus.wrapper.twitter.logicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.twitter.physicaloperator.access.TwitterTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "TwitterSource", category = { LogicalOperatorCategory.SOURCE }, doc = "Allows to read input from twitter.")
public class TwitterSource extends AbstractAccessAO {

	private static final long serialVersionUID = 6153312436167941743L;

	public TwitterSource() {
		setTransportHandler(TwitterTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
		List<SDFAttribute> schema = new LinkedList<>();
		schema.add(new SDFAttribute(null, "id", SDFDatatype.STRING, null, null, null));
		schema.add(new SDFAttribute(null, "username", SDFDatatype.STRING, null, null, null));
		schema.add(new SDFAttribute(null, "screenname", SDFDatatype.STRING, null, null, null));
		schema.add(new SDFAttribute(null, "date", SDFDatatype.STRING, null, null, null));
		schema.add(new SDFAttribute(null, "message", SDFDatatype.STRING, null, null, null));
		schema.add(new SDFAttribute(null, "geolocation", SDFDatatype.STRING, null, null, null));
		setAttributes(schema);
	}

	public TwitterSource(TwitterSource po) {
		super(po);
	}

	public TwitterSource(Resource name, String wrapper,
			String transportHandler, String protocolHandler,
			String dataHandler, OptionMap optionsMap) {
		super(name, wrapper, transportHandler, protocolHandler, dataHandler,
				optionsMap);
	}

	@Parameter(type = StringParameter.class, name = TwitterTransportHandler.CONSUMERKEY, optional = false, doc = "Twitter consumer key. See documentation.")
	public void setConsumerKey(String consumerKey) {
		addOption(TwitterTransportHandler.CONSUMERKEY, consumerKey);
	}

	@Parameter(type = StringParameter.class, name = TwitterTransportHandler.CONSUMERSECRET, optional = false, doc = "Twitter consumer secret. See documentation.")
	public void setConsumerSecret(String consumerKeySecret) {
		addOption(TwitterTransportHandler.CONSUMERSECRET, consumerKeySecret);
	}

	@Parameter(type = StringParameter.class, name = TwitterTransportHandler.ACCESSTOKEN, optional = false, doc = "Twitter access token. See documentation.")
	public void setAccessToken(String accessToken) {
		addOption(TwitterTransportHandler.ACCESSTOKEN, accessToken);
	}

	@Parameter(type = StringParameter.class, name = TwitterTransportHandler.ACCESSTOKENSECRET, optional = false, doc = "Twitter access token secret. See documentation.")
	public void setAccessTokenSecret(String accessTokenSecret) {
		addOption(TwitterTransportHandler.ACCESSTOKENSECRET, accessTokenSecret);
	}

	@Parameter(type = StringParameter.class, isList = true, name = TwitterTransportHandler.SEARCHKEYS, optional = true, doc = "Twitter search keys. See documentation.")
	public void setSearchKeys(List<String> searchKeys) {
		addOption(TwitterTransportHandler.SEARCHKEYS,
				buildString(searchKeys, ","));
	}
	
	@Parameter(type = StringParameter.class, isList = true, name = TwitterTransportHandler.LANGUAGEKEYS, optional = true, doc = "Twitter language keys. Note that at least one additional filter is required. See documentation.")
	public void setLanguageKeys(List<String> languages) {
		addOption(TwitterTransportHandler.LANGUAGEKEYS,
				buildString(languages, ","));
	}
	
	

	@Override
	public TwitterSource clone() {
		return new TwitterSource(this);
	}

}
