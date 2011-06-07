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
import de.uniol.inf.is.odysseus.wrapper.base.model.Source;

public class TwitterSourceAdapter extends AbstractPushingSourceAdapter implements SourceAdapter {
    private static Logger LOG = LoggerFactory.getLogger(TwitterSourceAdapter.class);

    private final Map<Source, TwitterStream> twitterStreams = new HashMap<Source, TwitterStream>();

    public TwitterSourceAdapter() {

    }

    @Override
    protected void doInit(final Source source) {
        final ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(source.getConfiguration().get("OAuthConsumerKey").toString())
                .setOAuthConsumerSecret(
                        source.getConfiguration().get("OAuthConsumerSecret").toString())
                .setOAuthAccessToken(source.getConfiguration().get("OAuthAccessToken").toString())
                .setOAuthAccessTokenSecret(
                        source.getConfiguration().get("OAuthAccessTokenSecret").toString());
        final TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        this.twitterStreams.put(source, twitterStream);

        final StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(final Status status) {
                final Calendar timestamp = Calendar.getInstance();
                timestamp.setTime(status.getCreatedAt());
                final Object[] result = new Object[] {
                        status.getUser().getName(), status.getUser().getScreenName(),
                        status.getText(), status.getInReplyToUserId(), status.getInReplyToUserId(),
                        status.getUser().getLocation()
                };
                TwitterSourceAdapter.this.transfer(source.getName(), System.currentTimeMillis(),
                        result);
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
                TwitterSourceAdapter.LOG.error(e.getMessage(), e);
            }
        };

        twitterStream.addListener(listener);
        twitterStream.sample();
    }

    @Override
    protected void doDestroy(final Source source) {
        this.twitterStreams.get(source).shutdown();
        this.twitterStreams.remove(source);
    }

    @Override
    public String getName() {
        return "Twitter";
    }

}
