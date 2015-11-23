package de.uniol.inf.is.odysseus.server.opcua.func;

import java.lang.reflect.Field;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.inductiveautomation.opcua.stack.core.StatusCodes;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public final class StatusFunctions {

	public static enum Kind {
		Bad, Good, Uncertain
	}

	private static final Map<Kind, Map<String, Long>> nameToLong;
	private static final Map<Long, Entry<Kind, String>> longToName;

	static {
		nameToLong = new HashMap<>();
		longToName = new HashMap<>();
		Field[] fields = StatusCodes.class.getFields();
		for (Field field : fields) {
			try {
				String[] parts = field.getName().split("_", 2);
				Kind kind = Kind.valueOf(parts[0]);
				String name = parts[1];
				long value = field.getLong(null);
				Map<String, Long> map;
				if (nameToLong.containsKey(kind))
					map = nameToLong.get(kind);
				else
					nameToLong.put(kind, map = new HashMap<>());
				map.put(name, value);
				longToName.put(value, new SimpleImmutableEntry<>(kind, name));
			} catch (IllegalAccessException e) {
				// Nothing here.
			}
		}
	}

	public static class ToErrorStrFunction extends AbstractFunction<String>
			implements IFunction<String> {

		private static final long serialVersionUID = -1144254816036709722L;

		private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.LONG } };

		public ToErrorStrFunction() {
			super("ToErrorStr", 1, accTypes, SDFDatatype.STRING);
		}

		@Override
		public String getValue() {
			Number value = getInputValue(0);
			long code = value.longValue();
			Entry<Kind, String> e = longToName.get(code);
			return e.getKey() + "_" + e.getValue();
		}
	}

	public static class ToErrorValFunction extends AbstractFunction<Long>
			implements IFunction<Long> {

		private static final long serialVersionUID = 628688567636378307L;

		private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.STRING } };

		public ToErrorValFunction() {
			super("ToErrorVal", 1, accTypes, SDFDatatype.LONG);
		}

		@Override
		public Long getValue() {
			String value = getInputValue(0);
			String[] parts = value.split("_", 2);
			Kind kind = Kind.valueOf(parts[0]);
			String name = parts[1];
			return nameToLong.get(kind).get(name);
		}
	}
}