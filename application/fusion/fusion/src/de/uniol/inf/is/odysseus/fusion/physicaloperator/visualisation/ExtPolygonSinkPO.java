package de.uniol.inf.is.odysseus.fusion.physicaloperator.visualisation;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.fusion.ui.ExtPolygonScreen;
import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class ExtPolygonSinkPO extends AbstractSink<Tuple<? extends IMetaAttribute>> {

    private final BlockingQueue<Tuple<? extends IMetaAttribute>> segments = new LinkedBlockingQueue<Tuple<? extends IMetaAttribute>>();

    private ExtPolygonScreen screen = new ExtPolygonScreen();
    private final SDFSchema schema;

    public ExtPolygonSinkPO(final SDFSchema schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public ExtPolygonSinkPO(final ExtPolygonSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }
    
    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Tuple<? extends IMetaAttribute> segment;
                try {
                    while ((segment = ExtPolygonSinkPO.this.segments.take()) != null) {
                    	 //this.segments.offer((Geometry) ((Tuple<TimeInterval>) object).getAttribute(0));
                    	ExtPolygonSinkPO.this.screen.onFeature(segment);
                    }
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    };
    
    @Override
    public ExtPolygonSinkPO clone() {
        return new ExtPolygonSinkPO(this);
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

	@Override
	protected void process_next(
			Tuple<? extends IMetaAttribute> object, int port) {
		 this.segments.offer(object);
		
	}


}
