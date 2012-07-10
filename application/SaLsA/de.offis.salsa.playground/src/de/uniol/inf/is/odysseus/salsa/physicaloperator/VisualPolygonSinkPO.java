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
package de.uniol.inf.is.odysseus.salsa.physicaloperator;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.salsa.ui.PolygonScreen;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualPolygonSinkPO extends AbstractSink<Object> {

    private final BlockingQueue<Geometry> segments = new LinkedBlockingQueue<Geometry>();

    private PolygonScreen screen = new PolygonScreen();
    private final SDFSchema schema;

    private final Thread painter = new Thread() {

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Geometry segment;
                try {
                    while ((segment = VisualPolygonSinkPO.this.segments.take()) != null) {
                        VisualPolygonSinkPO.this.screen.onFeature(segment);
                    }
                }
                catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }
    };

    public VisualPolygonSinkPO(final SDFSchema schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public VisualPolygonSinkPO(final VisualPolygonSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }

    @Override
    public VisualPolygonSinkPO clone() {
        return new VisualPolygonSinkPO(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Object object, final int port) {
        this.segments.offer((Geometry) ((Tuple<TimeInterval>) object).getAttribute(0));
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

}
