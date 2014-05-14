package de.uniol.inf.is.odysseus.wrapper.haggis.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.wrapper.haggis.physicaloperator.access.HaggisTransportHandler;
import Ice.Current;
import Ice.ObjectPrx;
import TelemetriePublishSubscribe.Pose;
import TelemetriePublishSubscribe.TelemetriePublisherPrx;
import TelemetriePublishSubscribe.TelemetriePublisherPrxHelper;
import TelemetriePublishSubscribe.TelemetrieSubscriberPrx;
import TelemetriePublishSubscribe.TelemetrieSubscriberPrxHelper;
import TelemetriePublishSubscribe._TelemetrieSubscriberDisp;

/**
 * Ice subscriber for the HaggisTransportHandler.
 * 
 * @author Christian Kuka <christian@kuka.cc> edited by jbmzh <jan.meyer.zu.holte@uni-oldenburg.de>
 * 
 */
public class HaggisTelemetrieSubscriber extends _TelemetrieSubscriberDisp {
    private final HaggisConsumer handler;
    private TelemetriePublisherPrx publisher;
    
    private final Logger LOG = LoggerFactory.getLogger(HaggisTransportHandler.class);
    
    private static final long serialVersionUID = -5341086082699034964L;

    /**
     * @param handler
     */
    public HaggisTelemetrieSubscriber(HaggisConsumer handler) {
        this.handler = handler;
    }

    @Override
    public void _notify(Pose p, Current __current) {
        // System.out.println(p.X + "; " + p.Y + "; "+ p.Z + "; " +
        // p.orientation);
        try {
            handler.getHTH().process(new Object[] { p.timestamp, p.X, p.Y, p.Z, p.orientation });
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * @param base
     * @param subscriber
     * 
     */
    public void open(ObjectPrx base, ObjectPrx subscriber) {
        try {
            publisher = TelemetriePublisherPrxHelper.checkedCast(base);
            TelemetrieSubscriberPrx subscriberPrx = TelemetrieSubscriberPrxHelper.checkedCast(subscriber);
            publisher.subscribe(subscriberPrx);
        }
        catch (Exception e) {
        	LOG.error(e.getMessage(), e);
            throw e;
        }
    }

    public void close() {
        if (this.publisher != null) {
            this.publisher = null;
        }
    }
}