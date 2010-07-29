package de.uniol.inf.is.odysseus.scars.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class SchemaIterator {

	private Stack<SchemaIndex> schemaIndices = new Stack<SchemaIndex>();
	private Stack<Boolean> insideList = new Stack<Boolean>();
	private Stack<Integer> counter = new Stack<Integer>();
	private int maxLevels;
	private SchemaIndexPath start;

	public SchemaIterator(SchemaIndexPath start) {
		this(start, Integer.MAX_VALUE);
	}

	public SchemaIterator(SchemaIndexPath start, int maxLevels) {
		this.maxLevels = maxLevels;
		reset(start);
	}
	
	public SchemaIterator( SDFAttributeList completeSchema ) {
		this( completeSchema, Integer.MAX_VALUE);
	}
	
	public SchemaIterator( SDFAttributeList completeSchema, int maxLevels ) {
		this.maxLevels = maxLevels;
		
		SDFAttribute attr = completeSchema.get(0);
		String name = attr.getAttributeName();
		
		SchemaHelper hlp = new SchemaHelper(completeSchema);
		SchemaIndexPath path = hlp.getSchemaIndexPath(name);
		
		reset(path);
	}

	public void reset(SchemaIndexPath start) {
		schemaIndices.clear();
		insideList.clear();

		schemaIndices.push(start.getSchemaIndex(start.getLength() - 1 ));
		insideList.push(schemaIndices.peek().isList());
		counter.push(0);
		
		this.start = start;
	}

	public void next() {
		if (schemaIndices.isEmpty())
			return;

		SchemaIndex index = schemaIndices.peek();

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
	}

	public SchemaIndexPath getSchemaIndexPath() {
		List<SchemaIndex> list = new ArrayList<SchemaIndex>();
		for( int i = 0; i < start.getSchemaIndices().size() - 1; i++ ) {
			list.add(new SchemaIndex(start.getSchemaIndex(i)));
		}
		
		List<SchemaIndex> subList = schemaIndices.subList(0, schemaIndices.size());
		for( int i = 0; i < subList.size(); i++ ) {
			list.add(new SchemaIndex(subList.get(i)));
		}
		
		return new SchemaIndexPath(list, schemaIndices.peek().getAttribute());
	}

	public SDFAttribute getAttribute() {
		return schemaIndices.peek().getAttribute();
	}

	public boolean isInList() {
		return insideList.peek();
	}

	public boolean isFinished() {
		return schemaIndices.isEmpty();
	}

	public int getLevel() {
		return schemaIndices.size() - 1;
	}
}
