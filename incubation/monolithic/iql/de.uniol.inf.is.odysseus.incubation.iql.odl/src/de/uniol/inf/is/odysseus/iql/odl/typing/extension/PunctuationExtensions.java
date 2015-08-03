package de.uniol.inf.is.odysseus.iql.odl.typing.extension;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class PunctuationExtensions implements IIQLTypeExtensions {

	@Override
	public Class<?> getType() {
		return IPunctuation.class;
	}
	
	public static IPunctuation plus(IPunctuation punctuation, long time) {
		return punctuation.clone(punctuation.getTime().plus(new PointInTime(time)));
	}
	
	public static IPunctuation minus(IPunctuation punctuation, long time) {
		return punctuation.clone(punctuation.getTime().minus(new PointInTime(time)));
	}
	
	public static IPunctuation multiply(IPunctuation punctuation, long time) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()*time));
	}
	
	public static IPunctuation divide(IPunctuation punctuation, long time) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()/time));
	}
	
	public static IPunctuation modulo(IPunctuation punctuation, long time) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()%time));
	}
	
	public static IPunctuation plusPrefix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()+1));
	}
	
	public static IPunctuation minusPrefix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()-1));
	}
	
	public static IPunctuation plusPostfix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()+1));
	}
	
	public static IPunctuation minusPostfix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(punctuation.getTime().getMainPoint()-1));
	}

}
