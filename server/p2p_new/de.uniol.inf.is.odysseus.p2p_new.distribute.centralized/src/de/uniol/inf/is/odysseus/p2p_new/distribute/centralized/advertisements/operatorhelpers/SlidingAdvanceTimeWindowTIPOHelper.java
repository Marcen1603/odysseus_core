package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Enumeration;

import net.jxta.document.Element;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

@SuppressWarnings("rawtypes")
public class SlidingAdvanceTimeWindowTIPOHelper extends AbstractPhysicalOperatorHelper<SlidingAdvanceTimeWindowTIPO> {
	private final static String OUTPUTMODE_TAG = "outputmode";
	private final static String WINDOWSIZE_TAG = "windowsize";
	private final static String WINDOWADVANCE_TAG = "windowadvance";
	private final static String USED_TIMEUNIT_TAG = "usedUnit";
	private final static String WINDOWTYPE_TAG = "windowType";
	private final static String WINDOW_IS_SLIDING_TAG = "windowIsSliding";
	@Override
	public Class<SlidingAdvanceTimeWindowTIPO> getOperatorClass() {
		return SlidingAdvanceTimeWindowTIPO.class;
	}

	@Override@SuppressWarnings("unchecked")
	public StructuredDocument createOperatorSpecificStatement(IPhysicalOperator o, MimeMediaType mimeType, StructuredDocument rootDoc, Element toAppendTo) {
		SlidingAdvanceTimeWindowTIPO<?> satw = (SlidingAdvanceTimeWindowTIPO<?>)o;
		toAppendTo.appendChild(rootDoc.createElement(OUTPUTMODE_TAG,satw.getOutputMode().toString()));
		toAppendTo.appendChild(rootDoc.createElement(WINDOWSIZE_TAG,Long.toString(satw.getWindowSize())));
		toAppendTo.appendChild(rootDoc.createElement(WINDOWADVANCE_TAG,Long.toString(satw.getWindowAdvance())));
		toAppendTo.appendChild(rootDoc.createElement(WINDOWTYPE_TAG,satw.getWindowType().toString()));
		toAppendTo.appendChild(rootDoc.createElement(USED_TIMEUNIT_TAG,satw.getParameterInfos().get("used unit").toString()));
		boolean usesSlide;
		if(satw.getParameterInfos().get("unit-based slide") != null) {
			usesSlide = true;
		} else {
			usesSlide = false;
		}
		toAppendTo.appendChild(rootDoc.createElement(WINDOW_IS_SLIDING_TAG,Boolean.toString(usesSlide)));
		return rootDoc;
	}

	@Override
	SimpleImmutableEntry<Integer, SlidingAdvanceTimeWindowTIPO> createSpecificOperatorFromStatement(TextElement<?> contentElement, int operatorId) {
		Enumeration<? extends TextElement<?>> elements = contentElement.getChildren();
		long windowSize = 0;
		long windowAdvance = 0;
		String usedTimeUnit = "";
		WindowType windowType = WindowType.TIME;
		boolean windowIsSliding = false;
		
		while(elements.hasMoreElements()) {
			TextElement<?> elem = elements.nextElement();
			if(elem.getName().equals(WINDOWSIZE_TAG)) {
				windowSize = Long.parseLong(elem.getTextValue());
			} else if(elem.getName().equals(WINDOWADVANCE_TAG)) {
				windowAdvance = Long.parseLong(elem.getTextValue());
			} else if(elem.getName().equals(USED_TIMEUNIT_TAG)) {
				usedTimeUnit = elem.getTextValue();
			} else if(elem.getName().equals(WINDOWTYPE_TAG)) {
				windowType = WindowType.valueOf(elem.getTextValue());
			} else if(elem.getName().equals(WINDOW_IS_SLIDING_TAG)) {
				windowIsSliding = Boolean.parseBoolean(elem.getTextValue());
			}
		}
		AbstractWindowAO logicalWindow = new TimeWindowAO();
		logicalWindow.setWindowSize(windowSize);
		if(windowIsSliding) {
			logicalWindow.setWindowSlide(windowAdvance);
		} else {
			logicalWindow.setWindowAdvance(windowAdvance);
		}
		logicalWindow.setUnit(usedTimeUnit);
		logicalWindow.setWindowType(windowType);

		
		SlidingAdvanceTimeWindowTIPO result = new SlidingAdvanceTimeWindowTIPO(logicalWindow);
		
		return new SimpleImmutableEntry<Integer, SlidingAdvanceTimeWindowTIPO>(operatorId,result);
	}
}
