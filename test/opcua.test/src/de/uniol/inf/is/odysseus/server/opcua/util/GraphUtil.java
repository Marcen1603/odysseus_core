package de.uniol.inf.is.odysseus.server.opcua.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

public final class GraphUtil {

	private GraphUtil() {
	}

	public static String toStr(NodeId id) {
		return id.toParseableString().replace(';', '_').replace('=', '_').replace('-', '_');
	}

	public static String getDisplayName(INodeModel model, NodeId dest) {
		DataValue attr = model.read(dest, AttributeIds.DisplayName);
		Variant var = attr.getValue();
		Object val = var.getValue();
		LocalizedText txt = (LocalizedText) val;
		return txt.getText();
	}

	public static String toAttributeStr(Map<?, ?> map) {
		StringBuilder bld = new StringBuilder();
		for (Entry<?, ?> e : map.entrySet()) {
			if (bld.length() > 0)
				bld.append(',');
			Object key = e.getKey();
			Object value = e.getValue();
			if (value instanceof String)
				value = "\"" + value + "\"";
			bld.append(key + "=" + value);
		}
		return "[" + bld + "]";
	}

	public static Field findFieldByValue(Class<?> type, Object value) {
		for (Field field : type.getFields())
			try {
				if (field.get(null).equals(value))
					return field;
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		return null;
	}

	public static FluentMap<String, Object> newMap() {
		return new FluentMap<String, Object>();
	}
}