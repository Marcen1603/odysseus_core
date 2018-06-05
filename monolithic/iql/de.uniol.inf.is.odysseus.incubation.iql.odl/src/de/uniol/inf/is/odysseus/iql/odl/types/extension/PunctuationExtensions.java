package de.uniol.inf.is.odysseus.iql.odl.types.extension;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.ExtensionMethod;
import de.uniol.inf.is.odysseus.iql.basic.typing.extension.IIQLTypeExtensions;

public class PunctuationExtensions implements IIQLTypeExtensions {

	@Override
	public Class<?> getType() {
		return IPunctuation.class;
	}
	
	@ExtensionMethod(ignoreFirstParameter=false)
	public static IPunctuation intToType(int value) {
		return Heartbeat.createNewHeartbeat(value);
	}
	
	public static IPunctuation plus(IPunctuation left, IPunctuation right) {
		return left.clone(left.getTime().plus(right.getTime()));
	}
	
	public static IPunctuation minus(IPunctuation left, IPunctuation right) {
		return left.clone(left.getTime().minus(right.getTime()));
	}
	
	public static IPunctuation multiply(IPunctuation left, IPunctuation right) {
		return left.clone(new PointInTime(left.getTime().getMainPoint()*right.getTime().getMainPoint()));
	}
	
	public static IPunctuation divide(IPunctuation left, IPunctuation right) {
		return left.clone(new PointInTime(left.getTime().getMainPoint()/right.getTime().getMainPoint()));
	}
	
	public static IPunctuation modulo(IPunctuation left, IPunctuation right) {
		return left.clone(new PointInTime(left.getTime().getMainPoint()%right.getTime().getMainPoint()));
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
	
	public static boolean greaterThan(IPunctuation left, IPunctuation right) {
		return left.getTime().getMainPoint()>right.getTime().getMainPoint();
	}
	
	public static boolean lessThan(IPunctuation left, IPunctuation right) {
		return left.getTime().getMainPoint()<right.getTime().getMainPoint();
	}
	
	public static boolean greaterOrEqualsThan(IPunctuation left, IPunctuation right) {
		return left.getTime().getMainPoint()>=right.getTime().getMainPoint();
	}
	
	public static boolean lessOrEqualsThan(IPunctuation left, IPunctuation right) {
		return left.getTime().getMainPoint()>=right.getTime().getMainPoint();
	}
	
	public static boolean equals(IPunctuation left, IPunctuation right) {
		return left.getTime().getMainPoint()==right.getTime().getMainPoint();
	}
	
	public static boolean equalsNot(IPunctuation left, IPunctuation right) {
		return left.getTime().getMainPoint()!=right.getTime().getMainPoint();
	}
	
	
	public static IPunctuation simplePlusPrefix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(+punctuation.getTime().getMainPoint()));
	}
	
	public static IPunctuation simpleMinusPrefix(IPunctuation punctuation) {
		return punctuation.clone(new PointInTime(-punctuation.getTime().getMainPoint()));
	}
	
}
