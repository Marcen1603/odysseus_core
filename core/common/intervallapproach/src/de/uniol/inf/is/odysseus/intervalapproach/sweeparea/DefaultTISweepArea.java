package de.uniol.inf.is.odysseus.intervalapproach.sweeparea;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.sweeparea.IFastList;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;

public class DefaultTISweepArea<T extends IStreamObject<? extends ITimeInterval>> extends AbstractTISweepArea<T> {
	
	private static final long serialVersionUID = -7694327254911041477L;
	public static final String NAME = "DefaultTISweepArea";

	public DefaultTISweepArea(DefaultTISweepArea<T> defaultTISweepArea) throws InstantiationException, IllegalAccessException {
		super(defaultTISweepArea);
	}

	public DefaultTISweepArea() {
		super();
	}
	
	public DefaultTISweepArea(IFastList<T> list){
		super(list);
	}

	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		return new DefaultTISweepArea<T>();
	};
	
	@Override
	public DefaultTISweepArea<T> clone() {
		try {
			return new DefaultTISweepArea<T>(this);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Clone error");
		}
	}
	
	@Override
	public String getName() {
		return NAME;
	}
}
