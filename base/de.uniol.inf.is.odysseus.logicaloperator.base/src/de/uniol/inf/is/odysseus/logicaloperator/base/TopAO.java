package de.uniol.inf.is.odysseus.logicaloperator.base;

/** Ist nur eine Hilfsklasse um den obersten Knoten eines Plans eindeutig
 * bestimmen zu koennen.
 * @author Marco Grawunder
 *
 */
public class TopAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6533111765567598018L;

	public TopAO(TopAO po) {
		super(po);
		setPOName(po.getPOName());
	}

	public TopAO() {
		super();
		setPOName("TopAO");
	}

	public @Override
	TopAO clone() {
		return new TopAO(this);
	}

}
