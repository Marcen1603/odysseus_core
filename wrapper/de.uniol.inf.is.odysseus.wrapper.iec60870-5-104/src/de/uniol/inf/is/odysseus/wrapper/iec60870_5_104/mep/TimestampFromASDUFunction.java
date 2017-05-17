package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.mep;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openmuc.j60870.IeTime16;
import org.openmuc.j60870.IeTime24;
import org.openmuc.j60870.IeTime56;
import org.openmuc.j60870.InformationElement;
import org.openmuc.j60870.InformationObject;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// TODO javaDoc
public class TimestampFromASDUFunction extends AbstractFunction<Long> {

	private static final long serialVersionUID = -6792142155452423063L;

	private static final String name = "TimestampFromASDU";

	private static final int numInputs = 1;

	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.LIST } };

	private static final SDFDatatype outputType = SDFDatatype.LONG;

	public TimestampFromASDUFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Long getValue() {
		List<InformationObject> infoObjects = getInputValue(0);
		Optional<InformationElement> optTimeInfoElem = infoObjects.stream()
				.flatMap(infoObject -> Arrays.asList(infoObject.getInformationElements()).stream())
				.flatMap(infoElemRow -> Arrays.asList(infoElemRow).stream())
				.filter(infoElem -> infoElem instanceof IeTime16 || infoElem instanceof IeTime24
						|| infoElem instanceof IeTime56)
				.findFirst();
		if (!optTimeInfoElem.isPresent()) {
			return null;
		}
		InformationElement timeInfoElem = optTimeInfoElem.get();
		if (timeInfoElem instanceof IeTime16) {
			return (long) ((IeTime16) timeInfoElem).getTimeInMs();
		} else if (timeInfoElem instanceof IeTime24) {
			return (long) ((IeTime24) timeInfoElem).getTimeInMs();
		} else if (timeInfoElem instanceof IeTime56) {
			return ((IeTime56) timeInfoElem).getTimestamp();
		}
		// unknown time data type
		return null;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}