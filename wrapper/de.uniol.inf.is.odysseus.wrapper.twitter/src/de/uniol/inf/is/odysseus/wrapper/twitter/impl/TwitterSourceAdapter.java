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
package de.uniol.inf.is.odysseus.wrapper.twitter.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import de.uniol.inf.is.odysseus.wrapper.base.AbstractPushingSourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.SourceAdapter;
import de.uniol.inf.is.odysseus.wrapper.base.model.SourceSpec;

public class TwitterSourceAdapter extends AbstractPushingSourceAdapter
		implements SourceAdapter {
	private static Logger LOG = LoggerFactory
			.getLogger(TwitterSourceAdapter.class);

	private final Map<SourceSpec, TwitterStream> twitterStreams = new HashMap<SourceSpec, TwitterStream>();

	public TwitterSourceAdapter() {

	}

	@Override
	protected void doInit(final SourceSpec source) {
		final ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
				.setOAuthConsumerKey(
						source.getConfiguration().get("OAuthConsumerKey")
								.toString())
				.setOAuthConsumerSecret(
						source.getConfiguration().get("OAuthConsumerSecret")
								.toString())
				.setOAuthAccessToken(
						source.getConfiguration().get("OAuthAccessToken")
								.toString())
				.setOAuthAccessTokenSecret(
						source.getConfiguration().get("OAuthAccessTokenSecret")
								.toString());
		final TwitterStream twitterStream = new TwitterStreamFactory(cb.build())
				.getInstance();
		this.twitterStreams.put(source, twitterStream);

		final StatusListener listener = new StatusListener() {
			@Override
			public void onStatus(final Status status) {
				final Calendar timestamp = Calendar.getInstance();
				timestamp.setTime(status.getCreatedAt());
				final Object[] result = new Object[] {
						status.getUser().getName(),
						status.getUser().getScreenName(), status.getText(),
						status.getInReplyToUserId(),
						status.getUser().getLocation() };
				TwitterSourceAdapter.this.transfer(source,
						System.currentTimeMillis(), result);
			}

			@Override
			public void onDeletionNotice(
					final StatusDeletionNotice statusDeletionNotice) {
			}

			@Override
			public void onTrackLimitationNotice(
					final int numberOfLimitedStatuses) {
			}

			@Override
			public void onScrubGeo(final long userId, final long upToStatusId) {

			}

			@Override
			public void onException(final Exception e) {
				TwitterSourceAdapter.LOG.error(e.getMessage(), e);
			}
		};

		twitterStream.addListener(listener);
		twitterStream.sample();
	}

	@Override
	protected void doDestroy(final SourceSpec source) {
		this.twitterStreams.get(source).shutdown();
		this.twitterStreams.remove(source);
	}

	@Override
	public String getName() {
		return "Twitter";
	}

}
