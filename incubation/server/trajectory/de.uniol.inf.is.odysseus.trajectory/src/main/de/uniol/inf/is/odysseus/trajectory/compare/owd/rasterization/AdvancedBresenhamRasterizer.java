/*
 *Copyright 2015 Marcus Behrendt
 * 
 \* Licensed under the Apache License, Version 2.0 (the "License");
 \* you may not use this file except in compliance with the License.
 *You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/package de.uniol.inf.is.odysseus.trajectory.compare.owd.rasterization;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Point;

import de.uniol.inf.is.odysseus.trajectory.compare.data.IRawTrajectory;
import de.uniol.inf.is.odysseus.trajectory.compare.owd.data.OwdData.GridCellList;

/**
 * An implementation of <tt>AbstractRasterizer</tt>. The rasterization is done 
 * by a modified <i>Bresenham algorithm</i>. The modification causes each 
 * <tt>GridCell</tt> to be adjacent.
 * 
 * @author marcus
 *
 */
public class AdvancedBresenhamRasterizer extends AbstractRasterizer {

	/** Logger for debugging purposes */
	private final static Logger LOGGER = LoggerFactory.getLogger(AdvancedBresenhamRasterizer.class);
	
	/**
	 * {@inheritDoc}
	 * The rasterization is done by a modified <i>Bresenham algorithm</i>. The modification
	 * causes each <tt>GridCell</tt> to be adjacent.
	 * 
	 */
	@Override
	protected GridCellList getGraphPoints(final IRawTrajectory trajectory, final double cellSize) throws IllegalArgumentException {
		
		if(trajectory == null) {
			throw new IllegalArgumentException("trajectory is null");
		}
		if(cellSize <= 0) {
			throw new IllegalArgumentException("cellSize is less or equal 0");
		}
		
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
		}
		return result;
	}
	
	/**
	 * Method for the modified <i>Bresenham algorithm</i>
	 * 
	 * @param x1d the x start point
	 * @param y1d the y start point
	 * @param x2d the x end point
	 * @param y2d the y end point
	 * @param cellSize the width and height of a cell
	 * @param gridCellList the <tt>GridCellList</tt> where <tt>GridCells</tt> will be stored
	 */
	private void rasterize(final double x1d, final double y1d, final double x2d, final double y2d, final double cellSize, final GridCellList gridCellList) {
        
        int x1 = (int)(x1d / cellSize);
        int y1 = (int)(y1d / cellSize);
        final int x2 = (int)(x2d / cellSize);
        final int y2 = (int)(y2d / cellSize);

        int d = 0;

        final int dy = Math.abs(y2 - y1);
        final int dx = Math.abs(x2 - x1);

        final int dy2 = (dy << 1);
        final int dx2 = (dx << 1);

        final int ix = x1 < x2 ? 1 : -1;
        final int iy = y1 < y2 ? 1 : -1;

        if (dy <= dx) {
        	
            while(true) {
            	gridCellList.addGridCell(x1, y1);
                if (x1 == x2) {
					break;
				}
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
                if (y1 == y2) {
					break;
				}
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
