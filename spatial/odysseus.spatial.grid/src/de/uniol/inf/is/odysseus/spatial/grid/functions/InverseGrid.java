package de.uniol.inf.is.odysseus.spatial.grid.functions;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;

import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.spatial.grid.sourcedescription.sdf.schema.SDFGridDatatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class InverseGrid extends AbstractFunction<Grid> {
	/**
     * 
     */
	private static final long serialVersionUID = 6682089158563417930L;
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFGridDatatype.GRID } };

	@Override
	public int getArity() {
		return 1;
	}

	@Override
	public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > this.getArity()) {
			throw new IllegalArgumentException(this.getSymbol() + " has only "
					+ this.getArity() + " argument: A grid.");
		} else {
			return accTypes[argPos];
		}
	}

	@Override
	public String getSymbol() {
		return "InverseGrid";
	}

	@Override
	public Grid getValue() {
		final Grid grid = this.getInputValue(0);
		final Grid inverseGrid = new Grid(grid.origin, grid.width
				* grid.cellsize, grid.depth * grid.cellsize, grid.cellsize);
		IplImage image = cvCreateImage(cvSize(grid.width, grid.depth),
				IPL_DEPTH_8U, 1);

		OpenCVUtil.gridToImage(grid, image);
		opencv_core.cvNot(image, image);
		OpenCVUtil.imageToGrid(image, inverseGrid);
		opencv_core.cvReleaseImage(image);
		return inverseGrid;
	}

	@Override
	public SDFDatatype getReturnType() {
		return SDFGridDatatype.GRID;
	}

}
