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
package de.uniol.inf.is.odysseus.wrapper.twitter.physicaloperator.access;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class TwitterTransportHandler extends AbstractPushTransportHandler implements StatusListener {
	public static final String NAME = "Twitter";

	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(TwitterTransportHandler.class);

	public static final String LOCATIONS = "locations";
	public static final String SEARCHKEYS = "searchkeys";
	public static final String ACCESSTOKENSECRET = "accesstokensecret";
	public static final String ACCESSTOKEN = "accesstoken";
	public static final String CONSUMERSECRET = "consumersecret";
	public static final String CONSUMERKEY = "consumerkey";
	public static final String OUTPUT_JSON = "output_json";
	public static final String LANGUAGEKEYS = "languages";

	private TwitterStream twitterStream;
	private Twitter twitter;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private String[] searchKeys;
	private double[][] locations;
	private String[] languageKeys;
	private boolean outputJson;

	public TwitterTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public TwitterTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	@Override
	public void send(final byte[] message) throws IOException {
		// LOG.info("SEND SEND TWITTER");
		try {
			twitter.updateStatus(ByteBuffer.wrap(message).asCharBuffer().toString());
		} catch (TwitterException e) {
			throw new IOException(e);
		}
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final TwitterTransportHandler handler = new TwitterTransportHandler(protocolHandler, options);
		return handler;
	}

	private void init(final OptionMap options) {
		options.checkRequiredException(CONSUMERKEY, CONSUMERSECRET, ACCESSTOKEN, ACCESSTOKENSECRET);
		this.consumerKey = options.get(CONSUMERKEY);
		this.consumerSecret = options.get(CONSUMERSECRET);
		this.accessToken = options.get(ACCESSTOKEN);
		this.accessTokenSecret = options.get(ACCESSTOKENSECRET);
	
		if (options.containsKey(SEARCHKEYS)) {
			this.searchKeys = options.get(SEARCHKEYS).split(",");
		}
		
		if (options.containsKey(LOCATIONS)) {
			this.locations = convertStringTo2DArray(options.get(LOCATIONS));
		}
		if (options.containsKey(LANGUAGEKEYS)) {
			this.languageKeys = options.get(LANGUAGEKEYS).split(",");
		}
		outputJson = options.getBoolean(OUTPUT_JSON, false);

	}

	@Override
	public String getName() {
		return NAME;
	}


	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(LOG.isDebugEnabled()).setOAuthConsumerKey(this.consumerKey)
				.setOAuthConsumerSecret(this.consumerSecret).setOAuthAccessToken(this.accessToken)
				.setOAuthAccessTokenSecret(this.accessTokenSecret).setJSONStoreEnabled(outputJson);
		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		twitterStream.addListener(this);

		// LOG.info("PROCESS IN OPEN TWITTER");

		/*
		 * for twitter statuses/filter one predicate parameter (follow, locations, or
		 * track) must be specified
		 * https://dev.twitter.com/docs/api/1.1/post/statuses/filter
		 */
		if (searchKeys != null || locations != null || languageKeys != null) {
			// set filter
			FilterQuery fq = new FilterQuery();
			if (searchKeys != null && searchKeys.length > 0) {
				fq.track(searchKeys);
			}
			if (locations != null && locations.length > 0) {
				fq.locations(locations);
			}
			if (languageKeys != null && languageKeys.length > 0) {
				fq.language(languageKeys);
			}

			twitterStream.filter(fq);
		} else {
			twitterStream.sample();
		}

	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(LOG.isDebugEnabled()).setOAuthConsumerKey(this.consumerKey)
				.setOAuthConsumerSecret(this.consumerSecret).setOAuthAccessToken(this.accessToken)
				.setOAuthAccessTokenSecret(this.accessTokenSecret);

		twitter = new TwitterFactory().getInstance();

		// LOG.info("PROCESS-OUT OPEN TWITTER");

	}

	@Override
	public void processInClose() throws IOException {
		twitterStream.shutdown();
		this.fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		twitterStream.shutdown();
		this.fireOnDisconnect();
	}

	@Override
	public void onStatus(final Status status) {
		GeoLocation statusLocation = status.getGeoLocation();

		// LOG.info("ON STATUS TWITTER "+status);
		if (outputJson) {
			String json = TwitterObjectFactory.getRawJSON(status);
			this.fireProcess(json);
		} else {
			String geoData;
			if (statusLocation == null) {
				geoData = "null";
			} else {
				geoData = statusLocation.toString();
			}

			List<String> tweetlist = new ArrayList<>();
			tweetlist.add(Long.toString(status.getId()));
			tweetlist.add(status.getUser().getName());
			tweetlist.add(status.getUser().getScreenName());
			tweetlist.add(status.getCreatedAt().toString());
			tweetlist.add(status.getText());
			tweetlist.add(geoData);

			// parse to string[]
			String[] tweet = tweetlist.toArray(new String[tweetlist.size()]);

			this.fireProcess(tweet);
		}
	}

	@Override
	public void onDeletionNotice(final StatusDeletionNotice statusDeletionNotice) {
	}

	@Override
	public void onTrackLimitationNotice(final int numberOfLimitedStatuses) {
	}

	@Override
	public void onScrubGeo(final long userId, final long upToStatusId) {

	}

	@Override
	public void onException(final Exception e) {
		LOG.error(e.getMessage(), e);
	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * convert a string to a 2DArray of geo-coordinates
	 * 
	 * @param data
	 * @return
	 */
	private double[][] convertStringTo2DArray(String data) {
		Scanner sc = new Scanner(data);
		sc.useDelimiter("[,|]");
		String[] ctr = data.split(",");
		double[][] matrix = new double[ctr.length / 2][2];
		for (int r = 0; r < ctr.length / 2; r++) {
			for (int c = 0; c < 2; c++) {
				matrix[r][c] = Double.parseDouble(sc.next());
			}
		}
		sc.close();
		return matrix;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof TwitterTransportHandler)) {
			return false;
		}
		TwitterTransportHandler other = (TwitterTransportHandler) o;
		if (!this.consumerKey.equals(other.consumerKey)) {
			return false;
		} else if (!this.consumerSecret.equals(other.consumerSecret)) {
			return false;
		} else if (!this.accessToken.equals(other.accessToken)) {
			return false;
		} else if (!this.accessTokenSecret.equals(other.accessTokenSecret)) {
			return false;
		} else if ((this.searchKeys == null && other.searchKeys != null)
				|| (this.searchKeys != null && other.searchKeys == null)) {
			return false;
		} else if ((this.locations == null && other.locations != null)
				|| (this.locations != null && other.locations == null)) {
			return false;
		} else if ((this.languageKeys == null && other.languageKeys != null)
				|| (this.languageKeys != null && other.languageKeys == null)) {
			return false;
		}
		if (this.searchKeys != null) {
			for (int i = 0; i < this.searchKeys.length; i++) {
				if (!searchKeys[i].equals(other.searchKeys[i])) {
					return false;
				}
			}
		}

		if (this.languageKeys != null) {
			for (int i = 0; i < this.languageKeys.length; i++) {
				if (!languageKeys[i].equals(other.languageKeys[i])) {
					return false;
				}
			}
		}

		if (this.locations != null) {
			for (int i = 0; i < this.locations.length; i++) {
				for (int j = 0; i < this.locations[i].length; j++) {
					if (locations[i][j] != other.locations[i][j]) {
						return false;
					}
				}

			}
		}
		
		if (this.outputJson != other.outputJson) {
			return false;
		}
		
		return true;
	}

	public String[] getLanguageKeys() {
		return languageKeys;
	}


}
