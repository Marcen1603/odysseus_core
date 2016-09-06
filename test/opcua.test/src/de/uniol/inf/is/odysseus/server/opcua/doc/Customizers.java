package de.uniol.inf.is.odysseus.server.opcua.doc;

import static de.uniol.inf.is.odysseus.server.opcua.util.GraphUtil.findFieldByValue;
import static de.uniol.inf.is.odysseus.server.opcua.util.GraphUtil.getDisplayName;
import static de.uniol.inf.is.odysseus.server.opcua.util.GraphUtil.newMap;

import java.util.Map;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

import de.uniol.inf.is.odysseus.server.opcua.util.FluentMap;

public class Customizers {

	public static interface IEdgeCustomizer {
		FluentMap<String, Object> customize(NodeId ref, BrowseDirection dir);
	}

	public static interface INodeCustomizer {
		FluentMap<String, Object> customize(INodeModel model, NodeId node);
	}

	public static FluentMap<String, Object> defEdger(NodeId r, BrowseDirection d) {
		return newMap().set("label", findFieldByValue(Identifiers.class, r).getName()).set("color",
				d == BrowseDirection.Forward ? "green" : "red");
	}

	public static FluentMap<String, Object> defNoder(INodeModel m, NodeId n) {
		return newMap().set("label", getDisplayName(m, n));
	}

	public static IEdgeCustomizer newEdgerByMap(Map<NodeId, Colors> colors) {
		return (m, n) -> newMap().set("color", colors.get(m));
	}
}