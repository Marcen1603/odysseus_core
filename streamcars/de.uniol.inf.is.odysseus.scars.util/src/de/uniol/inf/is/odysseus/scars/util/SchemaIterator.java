package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Hilfsklasse, um durch ein Schema zu iterieren. Das Schema kann
 * objektrelational sein. Diese Klasse kann in einem <code>foreach</code>
 * -Konstrukt verwendet werden. Als Typ der Laufvariable wird
 * <code>SchemaInfo</code> verwendet. Über die verschiedenen Konstruktoren kann
 * bestimmt werden, wie sich der Iterator verhalten soll.
 * <p>
 * <b>Beispielanwendung</b><br>
 * <code>for( SchemaInfo schemaInfo : new SchemaIterator(schema)) {<br>
 * } </code> <br>
 * Es sollte vermieden werden, den Iterator nach einer Iteration erneut zu
 * verwenden.
 * 
 * @author Timo Michelsen
 * 
 */
public class SchemaIterator implements Iterable<SchemaInfo>, Iterator<SchemaInfo> {

	private Stack<SchemaIndex> schemaIndices = new Stack<SchemaIndex>();
	private Stack<Boolean> insideList = new Stack<Boolean>();
	private Stack<Integer> counter = new Stack<Integer>();
	private int maxLevels;
	private SchemaIndexPath start;

	/**
	 * Konstruktor. Erstellt eine neue SchemaIterator-Instanz. Der Iterator
	 * beginnt an dem SDFAttribute, welcher durch den SchemaIndexPath angegeben
	 * wurde. Die Tiefe wird ignoriert, d.h. die Iteration geht so tief wie
	 * möglich.
	 * 
	 * @param start
	 *            SchemaIndexPath zum SDFAttribute, wo die Iteration gestartet
	 *            werden. Darf nicht <code>null</code> sein.
	 */
	public SchemaIterator(SchemaIndexPath start) {
		this(start, Integer.MAX_VALUE);
	}

	/**
	 * Konstruktor. Erstellt eine neue SchemaIterator-Instanz. Der Iterator
	 * beginnt an dem SDFAttribute, welcher durch den SchemaIndexPath angegeben
	 * wurde. Die maximale Tiefe wird durch <code>maxLevels</code> begrenzt,
	 * d.h. die Iteration geht nicht weiter als die angegebene Tiefe. Die Tiefe
	 * wird relativ zum SchemaIndexPath angegeben.
	 * 
	 * @param start
	 *            SchemaIndexPath zum SDFAttribute, wo die Iteration gestartet
	 *            werden. Darf nicht <code>null</code> sein.
	 * @param maxLevels
	 *            Maximale Iterationstiefe. Muss 0 oder positiv sein. Bei 0 wird
	 *            nicht in die Tiefe gegangen. Bei 1 nur die unmittelbaren
	 *            Subattribute und nicht weiter.
	 */
	public SchemaIterator(SchemaIndexPath start, int maxLevels) {
		this.maxLevels = maxLevels;
		reset(start);
	}

	/**
	 * Konstruktor. Erstellt eine neue SchemaIterator-Instanz. Der Iterator
	 * durchläuft das komplette angegebene Schema. Die Tiefe wird ignoriert,
	 * d.h. die Iteration geht so tief wie möglich.
	 * 
	 * @param completeSchema
	 *            Schema, welches vollständig durchlaufen werden soll. Darf
	 *            nicht <code>null</code> sein.
	 */
	public SchemaIterator(SDFAttributeList completeSchema) {
		this(completeSchema, Integer.MAX_VALUE);
	}

	/**
	 * Konstruktor. Erstellt eine neue SchemaIterator-Instanz. Der Iterator
	 * durchläuft das komplette angegebene Schema. Die maximale Tiefe wird durch
	 * <code>maxLevels</code> begrenzt, d.h. die Iteration geht nicht weiter als
	 * die angegebene Tiefe.
	 * 
	 * @param completeSchema
	 *            Schema, welches vollständig durchlaufen werden soll. Darf
	 *            nicht <code>null</code> sein.
	 * @param maxLevels
	 *            Maximale Iterationstiefe. Muss 0 oder positiv sein. Bei 0 wird
	 *            nicht in die Tiefe gegangen. Bei 1 nur die unmittelbaren
	 *            Subattribute und nicht weiter.
	 */
	public SchemaIterator(SDFAttributeList completeSchema, int maxLevels) {
		this.maxLevels = maxLevels;

		SDFAttribute attr = completeSchema.get(0);
		String name = attr.getAttributeName();

		SchemaHelper hlp = new SchemaHelper(completeSchema);
		SchemaIndexPath path = hlp.getSchemaIndexPath(name);

		reset(path);
	}

	private void reset() {
		reset(this.start);
	}

	private void reset(SchemaIndexPath start) {
		schemaIndices.clear();
		insideList.clear();

		schemaIndices.push(start.getSchemaIndex(start.getLength() - 1));
		insideList.push(schemaIndices.peek().isList());
		counter.push(0);

		this.start = start;
	}

	@Override
	public SchemaInfo next() {
		if (schemaIndices.isEmpty())
			return null;

		SchemaIndex index = schemaIndices.peek();

		SchemaInfo info = new SchemaInfo();
		info.attribute = getAttribute();
		info.schemaIndexPath = getSchemaIndexPath();
		info.isInList = isInList();
		info.level = getLevel();

		if (schemaIndices.size() <= maxLevels) {
			SDFAttribute attribute = index.getAttribute();
			if (counter.peek() == attribute.getSubattributeCount()) {
				// Listenende erreicht
				counter.pop();
				schemaIndices.pop();
				insideList.pop();
				next(); // nächstes ausprobieren
			} else {

				if (index.getAttribute().getDatatype().getQualName().equals("List")) {
					schemaIndices.push(new SchemaIndex(0, index.getAttribute().getSubattribute(0)));
					insideList.push(true);
				} else {
					schemaIndices.push(new SchemaIndex(counter.peek(), index.getAttribute().getSubattribute(counter.peek())));
					insideList.push(insideList.peek());
				}
				Integer c = counter.pop();
				c = c + 1;
				counter.push(c);
				counter.push(0);
			}
		} else {
			schemaIndices.pop();
			insideList.pop();
			counter.pop();
			next();
		}

		return info;
	}

	private SchemaIndexPath getSchemaIndexPath() {
		List<SchemaIndex> list = new ArrayList<SchemaIndex>();
		for (int i = 0; i < start.getSchemaIndices().size() - 1; i++) {
			list.add(start.getSchemaIndex(i).clone());
		}

		List<SchemaIndex> subList = schemaIndices.subList(0, schemaIndices.size());
		for (int i = 0; i < subList.size(); i++) {
			list.add(subList.get(i).clone());
		}

		return new SchemaIndexPath(list, schemaIndices.peek().getAttribute());
	}

	private SDFAttribute getAttribute() {
		return schemaIndices.peek().getAttribute();
	}

	private boolean isInList() {
		return insideList.peek();
	}

	private boolean isFinished() {
		return schemaIndices.isEmpty();
	}

	private int getLevel() {
		return schemaIndices.size() - 1;
	}

	@Override
	public boolean hasNext() {
		if (isFinished()) {
			reset();
			return false;
		}
		return true;
	}

	@Override
	public void remove() {
	}

	@Override
	public Iterator<SchemaInfo> iterator() {
		return this;
	}
}
