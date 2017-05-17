package de.uniol.inf.is.odysseus.wrapper.iec60870_5_104.mep;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openmuc.j60870.IeTime16;
import org.openmuc.j60870.IeTime24;
import org.openmuc.j60870.IeTime56;
import org.openmuc.j60870.InformationElement;
import org.openmuc.j60870.InformationObject;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

//TODO javaDoc
public class TimestampToASDUFunction extends AbstractFunction<List<InformationObject>> {

	private static final long serialVersionUID = -571136363649697630L;

	private static final String name = "TimestampToASDU";

	private static final int numInputs = 2;

	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.LIST },
			{ SDFDatatype.LONG } };

	private static final SDFDatatype outputType = SDFDatatype.LIST;

	public TimestampToASDUFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public List<InformationObject> getValue() {
		List<InformationObject> infoObjects = getInputValue(0);
		long ts = getInputValue(1);
		List<InformationObject> newInfoObjects = infoObjects.stream()
				.map(infoObj -> handleInformationObject(infoObj, ts)).collect(Collectors.toList());
		return newInfoObjects;
	}

	private InformationObject handleInformationObject(InformationObject infoObj, long ts) {
		InformationElement[][] infoElems = Arrays.asList(infoObj.getInformationElements()).stream()
				.map(infoElemRow -> handleInformationElements(infoElemRow, ts)).toArray(InformationElement[][]::new);
		return new InformationObject(infoObj.getInformationObjectAddress(), infoElems);
	}

	private InformationElement[] handleInformationElements(InformationElement[] infoElemRow, long ts) {
		return Arrays.asList(infoElemRow).stream().map(infoElem -> handleInformationElement(infoElem, ts))
				.toArray(InformationElement[]::new);
	}

	private InformationElement handleInformationElement(InformationElement infoElem, long ts) {
		if (infoElem instanceof IeTime16) {
			return new IeTime16(ts);
		} else if (infoElem instanceof IeTime24) {
			return new IeTime24(ts);
		} else if (infoElem instanceof IeTime56) {
			return new IeTime56(ts);
		}
		return infoElem;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}