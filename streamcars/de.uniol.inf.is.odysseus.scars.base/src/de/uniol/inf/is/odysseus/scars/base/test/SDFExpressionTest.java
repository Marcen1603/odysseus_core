package de.uniol.inf.is.odysseus.scars.base.test;

import java.util.Map;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.scars.base.SDFObjectRelationalExpression;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

class ObjectRelationalAttributeResolver implements IAttributeResolver {

	private static final long serialVersionUID = 7707175311447087244L;
	private SDFAttributeList schema;
	
	public ObjectRelationalAttributeResolver( SDFAttributeList schema ) {
		this.schema = schema;
	}
	
	@Override
	public SDFAttribute getAttribute(String name) {
		String[] parts = name.split("\\.");
		
		SDFAttribute attr = findAttribute(schema, parts, 0);
		return attr;
	}
	
	private SDFAttribute findAttribute( SDFAttributeList base, String[] parts, int index ) {
		String partToFind = parts[index];
		for( SDFAttribute attr : base ) {
			if( attr.getAttributeName().equals(partToFind)) {
				if( attr.getSubattributeCount() > 0 ) {
					if( index < parts.length - 1) 
						return findAttribute(attr.getSubattributes(), parts, index + 1);
					else
						return attr;
				} else {
					return attr;
				}
			}
		}
		return null;
	}

	@Override
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated) {
		// TODO Auto-generated method stub
		
	}
	
	public ObjectRelationalAttributeResolver clone() {
		return new ObjectRelationalAttributeResolver(schema);
	}
}

public class SDFExpressionTest {

	public static void main(String[] args ) {
		
		SDFAttributeList scan = new SDFAttributeList("scan");
		
		SDFAttribute root = new SDFAttribute("root");
		
		SDFAttribute cars = new SDFAttribute("cars");
		SDFAttribute timestamp = new SDFAttribute("timestamp");
	
		SDFAttribute pos = new SDFAttribute("pos");
		SDFAttribute x = new SDFAttribute("x");
		SDFAttribute y = new SDFAttribute("y");

		scan.add(root);
		root.addSubattribute(cars);
		root.addSubattribute(timestamp);
		
		cars.addSubattribute(pos);
		pos.addSubattribute(x);
		pos.addSubattribute(y);
		
		ObjectRelationalAttributeResolver resolver = new ObjectRelationalAttributeResolver(scan);
		SDFObjectRelationalExpression expr = new SDFObjectRelationalExpression("", "root.cars.pos.x + root.cars.pos.y + root.timestamp", resolver);
		
		expr.initAttributePaths(scan);
		
		int[][] posi = expr.getAttributePaths();
		for( int[] i : posi ) {
			for( int j : i)
				System.out.print(j);
			System.out.println();
		}
	}
}
