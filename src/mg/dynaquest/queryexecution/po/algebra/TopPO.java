package mg.dynaquest.queryexecution.po.algebra;

/** Ist nur eine Hilfsklasse um den obersten Knoten eines Plans eindeutig
 * bestimmen zu können.
 * @author Marco Grawunder
 *
 */
public class TopPO extends UnaryAlgebraPO {

	public TopPO(TopPO po) {
		super(po);
		setPOName(po.getPOName());
	}

	public TopPO() {
		super();
		setPOName("TopPO");
	}

	public @Override
	SupportsCloneMe cloneMe() {
		return new TopPO(this);
	}

}
