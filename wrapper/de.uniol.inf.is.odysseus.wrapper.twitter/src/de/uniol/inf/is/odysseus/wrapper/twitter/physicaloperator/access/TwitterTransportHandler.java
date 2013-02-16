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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class TwitterTransportHandler extends AbstractTransportHandler implements
		StatusListener {
	/** Logger */
	private final Logger LOG = LoggerFactory
			.getLogger(TwitterTransportHandler.class);
	private static final Charset charset = Charset.forName("UTF-8");
	private static final CharsetEncoder encoder = charset.newEncoder();
	private TwitterStream twitterStream;
	private Twitter twitter;
	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessTokenSecret;

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
		handler.consumerKey = options.get("ConsumerKey");
		handler.consumerSecret = options.get("ConsumerSecret");
		handler.accessToken = options.get("AccessToken");
		handler.accessTokenSecret = options.get("AccessTokenSecret");
		return handler;
	}

	@Override
	public String getName() {
		return "Twitter";
	}

	@Override
	public InputStream getInputStream() {
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public OutputStream getOutputStream() {
		throw new IllegalArgumentException("Currently not implemented");
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
		twitterStream.sample();
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
		try {
			this.fireProcess(encoder.encode(CharBuffer.wrap(status.getText())));
		} catch (CharacterCodingException e) {
			LOG.error(e.getMessage(), e);
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

}
