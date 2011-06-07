package de.uniol.inf.is.odysseus.wrapper.twitter.impl;

import java.util.Calendar;

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
    private TwitterStream twitterStream;

    public TwitterSourceAdapter() {

    }

    @Override
    protected void doInit(final Source source) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(source.getConfiguration().get("OAuthConsumerKey").toString())
                .setOAuthConsumerSecret(
                        source.getConfiguration().get("OAuthConsumerSecret").toString())
                .setOAuthAccessToken(source.getConfiguration().get("OAuthAccessToken").toString())
                .setOAuthAccessTokenSecret(
                        source.getConfiguration().get("OAuthAccessTokenSecret").toString());
        this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener() {
            public void onStatus(Status status) {
                Calendar timestamp = Calendar.getInstance();
                timestamp.setTime(status.getCreatedAt());
                Object[] result = new Object[] {
                        status.getUser().getName(), status.getUser().getScreenName(),
                        status.getText(), status.getInReplyToUserId(), status.getInReplyToUserId(),
                        status.getUser().getLocation()
                };
                transfer(source.getName(), System.currentTimeMillis(), result);
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            public void onScrubGeo(long userId, long upToStatusId) {

            }

            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        twitterStream.addListener(listener);
        twitterStream.sample();
    }

    @Override
    protected void doDestroy(Source source) {
        twitterStream.shutdown();
    }

    @Override
    public String getName() {
        return "Twitter";
    }

}
