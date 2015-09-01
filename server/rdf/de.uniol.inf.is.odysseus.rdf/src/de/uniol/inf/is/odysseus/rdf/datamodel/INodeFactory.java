package de.uniol.inf.is.odysseus.rdf.datamodel;

public class INodeFactory {

	public static INode createNode(String name) {
		if (name.startsWith("?") || name.startsWith("$")){
			return new Variable(name);
		}
		if (name.isEmpty()){
			return new BlankNode(name);
		}
		// TODO: What about iris?
		return new Literal(name);
	}

}
