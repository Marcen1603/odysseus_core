package de.uniol.inf.is.odysseus.salsa.physicaloperator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.salsa.model.Grid;
import de.uniol.inf.is.odysseus.salsa.ui.GridScreen;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualGridSinkPO extends AbstractSink<Object> {
    private final BlockingQueue<Grid> grids = new LinkedBlockingQueue<Grid>();
    private GridScreen screen = new GridScreen();
    private final SDFAttributeList schema;
    private final Thread painter = new Thread() {

        // 100% CPU
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Grid grid;
                try {
                    while ((grid = VisualGridSinkPO.this.grids.take()) != null) {
                        VisualGridSinkPO.this.screen.onGrid(grid);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public VisualGridSinkPO(final SDFAttributeList schema) {
        this.schema = schema;
        this.screen.repaint();
        this.painter.start();
    }

    public VisualGridSinkPO(final VisualGridSinkPO po) {
        this.schema = po.schema;
        this.screen.repaint();
        this.painter.start();
    }

    @Override
    public VisualGridSinkPO clone() {
        return new VisualGridSinkPO(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Object object, final int port, final boolean isReadOnly) {
        this.grids.offer((Grid) ((RelationalTuple<TimeInterval>) object).getAttribute(0));
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

}
