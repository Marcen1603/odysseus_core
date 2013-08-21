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
import java.util.Map;
import java.util.Scanner;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class TwitterTransportHandler extends AbstractPushTransportHandler
		implements StatusListener {
	/** Logger */
	private final Logger LOG = LoggerFactory
			.getLogger(TwitterTransportHandler.class);
	private TwitterStream twitterStream;
	private Twitter twitter;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;
	private String[] searchKeys;
	private double[][] locations;

	public TwitterTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public TwitterTransportHandler(final IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(final byte[] message) throws IOException {
		try {
			twitter.updateStatus(ByteBuffer.wrap(message).asCharBuffer()
					.toString());
		} catch (TwitterException e) {
			throw new IOException(e);
		}
	}
	
	
	@Override
	public ITransportHandler createInstance(
			final IProtocolHandler<?> protocolHandler,
			final Map<String, String> options) {
		final TwitterTransportHandler handler = new TwitterTransportHandler(
				protocolHandler);
		handler.init(options);
		return handler;
	}

	private void init(final Map<String, String> options) {
		if (options.containsKey("consumerkey")) {
			setConsumerKey(options.get("consumerkey"));
		}
		if (options.containsKey("consumersecret")) {
			setConsumerSecret(options.get("consumersecret"));
		}
		if (options.containsKey("accesstoken")) {
			setAccessToken(options.get("accesstoken"));
		}
		if (options.containsKey("accesstokensecret")) {
			setAccessTokenSecret(options.get("accesstokensecret"));
		}
		if (options.containsKey("searchkeys")){
			setSearchKeys(options.get("searchkeys"));
		}
		if (options.containsKey("locations")){
			setLocations(options.get("locations"));
		}
	}

	@Override
	public String getName() {
		return "Twitter";
	}

	public String getConsumerKey() {
		return this.consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return this.consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessTokenSecret() {
		return this.accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}
	
	public void setSearchKeys(String searchKeys){
		this.searchKeys = searchKeys.split(",");
	}
	
	public void setLocations(String locations){
		this.locations = convertStringTo2DArray(locations); 
	}
	
	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(LOG.isDebugEnabled())
				.setOAuthConsumerKey(this.consumerKey)
				.setOAuthConsumerSecret(this.consumerSecret)
				.setOAuthAccessToken(this.accessToken)
				.setOAuthAccessTokenSecret(this.accessTokenSecret);
		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(this);
		
		/*
		 * for twitter statuses/filter one predicate 
		 * parameter (follow, locations, or track) 
		 * must be specified 
		 * https://dev.twitter.com/docs/api/1.1/post/statuses/filter
		 */
		if(searchKeys.length > 0 || locations.length > 0){
			//set filter
			FilterQuery fq = new FilterQuery();
			if(searchKeys.length > 0){
				fq.track(searchKeys);
			}
			if(locations != null){
				fq.locations(locations);
			}
			twitterStream.filter(fq);
		}else{
			twitterStream.sample();
		}
	
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(LOG.isDebugEnabled())
				.setOAuthConsumerKey(this.consumerKey)
				.setOAuthConsumerSecret(this.consumerSecret)
				.setOAuthAccessToken(this.accessToken)
				.setOAuthAccessTokenSecret(this.accessTokenSecret);

		twitter = new TwitterFactory().getInstance();

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
		
		String geoData;
		if(statusLocation == null){
			 geoData = "null";
		}else{
			 geoData = statusLocation.toString();
		}
	
		List<String> tweetlist = new ArrayList<>();
		tweetlist.add(Long.toString(status.getId()));
		tweetlist.add(status.getCreatedAt().toString());
		tweetlist.add(status.getText());
		tweetlist.add(geoData);

		//parse to string[]
		String [] tweet = tweetlist.toArray(new String[tweetlist.size()]);
		
		this.fireProcess(tweet);
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
	 * @param data
	 * @return
	 */
	private double[][] convertStringTo2DArray(String data){
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(data).useDelimiter("[,|]");
		String[] ctr = data.split(","); 
		double[][] matrix = new double[ctr.length/2][2];
		    for (int r = 0; r < ctr.length/2; r++) {
		        for (int c = 0; c < 2; c++) {
		            matrix[r][c] = Double.parseDouble(sc.next());
		        }
		 }
		   
		return matrix;    
	}

}
