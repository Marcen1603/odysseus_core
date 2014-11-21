package de.uniol.inf.is.odysseus.sports.mep;

import java.awt.geom.Point2D;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.Matrix;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

public class CoordCalculationFunction extends AbstractFunction<Double> {

	private static final long serialVersionUID = -7010323893718106395L;

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(CoordCalculationFunction.class);

	protected static IDistributedDataContainer ddc;

	private static HashMap<String, Point2D> listOfAccessPoints;

	private static boolean hasBeenSetUp = false;

	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE },
			{ SDFDatatype.DOUBLE }, { SDFDatatype.STRING } };

	public CoordCalculationFunction() {
		super("calcXY", 4, accTypes, SDFDatatype.DOUBLE);
		listOfAccessPoints = new HashMap<String, Point2D>();
	}

	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		CoordCalculationFunction.ddc = ddc;
		CoordCalculationFunction.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (CoordCalculationFunction.ddc == ddc) {

			CoordCalculationFunction.ddc = null;
			CoordCalculationFunction.LOG.debug("Unbound {} as a DDC", ddc
					.getClass().getSimpleName());

		}
	}

	private static void setUpField() {
		try {

			double xmin = Double.valueOf(CoordCalculationFunction.ddc
					.getValue(new DDCKey("field,xmin")));
			double ymin = Double.valueOf(CoordCalculationFunction.ddc
					.getValue(new DDCKey("field,ymin")));
			double xmax = Double.valueOf(CoordCalculationFunction.ddc
					.getValue(new DDCKey("field,xmax")));
			double ymax = Double.valueOf(CoordCalculationFunction.ddc
					.getValue(new DDCKey("field,ymax")));

			listOfAccessPoints.put("topLeft", new Point2D.Double(xmin, ymin));
			listOfAccessPoints.put("topRight", new Point2D.Double(xmax, ymin));
			listOfAccessPoints
					.put("bottomLeft", new Point2D.Double(xmin, ymax));
			listOfAccessPoints.put("bottomRight",
					new Point2D.Double(xmax, ymax));

			hasBeenSetUp = true;

		} catch (MissingDDCEntryException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public Double getValue() {
		if (!hasBeenSetUp) {
			setUpField();
		}

		double x1 = listOfAccessPoints.get("topLeft").getX();
		double x2 = listOfAccessPoints.get("topRight").getX();
		double x3 = listOfAccessPoints.get("bottomLeft").getX();
		double y1 = listOfAccessPoints.get("topLeft").getY();
		double y2 = listOfAccessPoints.get("topRight").getY();
		double y3 = listOfAccessPoints.get("bottomLeft").getY();

		double r1 = getInputValue(0);
		double r2 = getInputValue(1);
		double r3 = getInputValue(2);

		Matrix A, b, x;

		double m[][] = { { x3 - x1, y3 - y1 }, { x3 - x2, y3 - y2 } };

		double b_temp[][] = {
				{ ((r1 * r1) - (r3 * r3)) - ((x1 * x1) - (x3 * x3))
						- ((y1 * y1) - (y3 * y3)) },
				{ ((r2 * r2) - (r3 * r3)) - ((x2 * x2) - (x3 * x3))
						- ((y2 * y2) - (y3 * y3)) } };

		b = new Matrix(b_temp);
		A = new Matrix(m);
		A = A.times(2.0);
		x = A.solve(b);

		if (getInputValue(3).toString().equals("x")) {
			return x.getArray()[0][0];
		} else {
			return x.getArray()[1][0];
		}

	}

}
