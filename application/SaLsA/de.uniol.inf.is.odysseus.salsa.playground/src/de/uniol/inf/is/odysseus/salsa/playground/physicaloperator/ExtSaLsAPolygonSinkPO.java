package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.playground.ui.ExtPolygonScreen;

public class ExtSaLsAPolygonSinkPO extends AbstractSink<RelationalTuple<? extends IMetaAttribute>> {

    private final BlockingQueue<RelationalTuple<? extends IMetaAttribute>> segments = new LinkedBlockingQueue<RelationalTuple<? extends IMetaAttribute>>();

    private ExtPolygonScreen screen = new ExtPolygonScreen();
    private final SDFSchema schema;

    public ExtSaLsAPolygonSinkPO(final SDFSchema schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public ExtSaLsAPolygonSinkPO(final ExtSaLsAPolygonSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }
    
    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                RelationalTuple<? extends IMetaAttribute> segment;
                try {
                    while ((segment = ExtSaLsAPolygonSinkPO.this.segments.take()) != null) {
                    	 //this.segments.offer((Geometry) ((RelationalTuple<TimeInterval>) object).getAttribute(0));
                    	ExtSaLsAPolygonSinkPO.this.screen.onFeature(segment);
                    }
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    };
    
    @Override
    public ExtSaLsAPolygonSinkPO clone() {
        return new ExtSaLsAPolygonSinkPO(this);
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

	@Override
	protected void process_next(
			RelationalTuple<? extends IMetaAttribute> object, int port,
			boolean isReadOnly) {
		 this.segments.offer((RelationalTuple<? extends IMetaAttribute>)object);
		
	}


}
