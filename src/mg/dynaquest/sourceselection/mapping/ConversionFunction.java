package mg.dynaquest.sourceselection.mapping;

import java.util.List;

public abstract class ConversionFunction {
	public abstract List<Object> process(List<Object> in);

	protected ConversionFunction(boolean egal) {
	};
}