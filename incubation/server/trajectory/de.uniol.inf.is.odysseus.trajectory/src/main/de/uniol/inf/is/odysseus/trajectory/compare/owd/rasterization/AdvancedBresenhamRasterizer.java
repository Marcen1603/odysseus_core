package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;

public class AdvancedBresenhamRasterizer extends AbstractRasterizer {

	private final static Logger LOGGER = LoggerFactory.getLogger(AdvancedBresenhamRasterizer.class);
	
	@Override
	protected GridCellList getGraphPoints(
			IRawTrajectory trajectory, double cellSize) {
		
		
		final GridCellList result = new GridCellList();
		
		final Iterator<Point> it = trajectory.getPoints().iterator();
		
		Point previous = it.next();
		while(it.hasNext()) {
			final Point next = it.next();
			this.rasterize(previous.getX(), previous.getY(), next.getX(), next.getY(), cellSize, result);
			previous = next;
		}
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Rasterized trajectory with size " + trajectory.getPoints().size() + " to " + result.size() + " grid cells");
			LOGGER.debug(result + "");
		}
		return result;
	}
	
	/**
	 * TODO:
	 * Taken and modified from http://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Java
	 * 
	 * @param x1d
	 * @param y1d
	 * @param x2d
	 * @param y2d
	 * @param grid
	 * @param gridCellList
	 */
	private void rasterize(double x1d, double y1d, double x2d, double y2d, double grid, GridCellList gridCellList) {
        
        int x1 = (int)(x1d / grid);
        int y1 = (int)(y1d / grid);
        int x2 = (int)(x2d / grid);
        int y2 = (int)(y2d / grid);

        // delta of exact value and rounded value of the dependant variable
        int d = 0;

        int dy = Math.abs(y2 - y1);
        int dx = Math.abs(x2 - x1);

        int dy2 = (dy << 1); // slope scaling factors to avoid floating
        int dx2 = (dx << 1); // point

        int ix = x1 < x2 ? 1 : -1; // increment direction
        int iy = y1 < y2 ? 1 : -1;

        if (dy <= dx) {
        	
            while(true) {
            	gridCellList.addGridCell(x1, y1);
                if (x1 == x2)
                    break;
                x1 += ix;
                d += dy2;
                if (d > dx) {
                	gridCellList.addGridCell(x1, y1);
                    y1 += iy;
                    d -= dx2;
                }
            }
        } else {
            while(true) {
            	gridCellList.addGridCell(x1, y1);
                if (y1 == y2)
                    break;
                y1 += iy;
                d += dx2;
                if (d > dy) {
                	gridCellList.addGridCell(x1, y1);
                    x1 += ix;
                    d -= dy2;
                }
            }
        }
    }
}
