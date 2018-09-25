package de.uniol.inf.is.odysseus.relational_interval.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class RelationalFastMedianHistogramPO<T extends Comparable<T>>
		extends
		AbstractFastMedianPO<T> {

	Map<Object, Histogram<T, Tuple<? extends ITimeInterval>>> elements = new HashMap<>();
	double roundfactor = 100;

	public RelationalFastMedianHistogramPO(int medianAttrPos, boolean numericalMedian, long roundfactor) {
		super(medianAttrPos , numericalMedian);
		this.roundfactor = roundfactor;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		elements.clear();
		super.process_open();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port, Object groupID) {
		
		T medianAttribute = (T) object.getAttribute(medianAttrPos);

		Histogram<T, Tuple<? extends ITimeInterval>> groupHistogram = getOrCreategroupHistogram(
				groupID, medianAttribute);
		groupHistogram.cleanUp(object.getMetadata().getStart());
		if (roundfactor>0){
			groupHistogram.addElement((T)new Double((Math.round(((Number)medianAttribute).doubleValue()*roundfactor)/(roundfactor))), object);
		}else{
			groupHistogram.addElement(medianAttribute, object);
		}

		Tuple<? extends ITimeInterval> gr = createMedian(object, groupHistogram);

		createOutput(groupID, gr);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Histogram<T, Tuple<? extends ITimeInterval>> getOrCreategroupHistogram(
			Object groupID, T median) {
		Histogram<T, Tuple<? extends ITimeInterval>> groupHistogram = elements
				.get(groupID);
		if (groupHistogram == null) {
			if (numericalMedian) {
				// Problem: T must be a number, must this is not testable at compile time
				groupHistogram = new NumericalHistogram();
			} else {
				groupHistogram = new NonNumericalHistogram<T, Tuple<? extends ITimeInterval>>();
			}
			elements.put(groupID, groupHistogram);
		}
		return groupHistogram;
	}

	public Tuple<? extends ITimeInterval> createMedian(
			Tuple<? extends ITimeInterval> object,
			Histogram<T, Tuple<? extends ITimeInterval>> groupHistogram) {

		Tuple<? extends ITimeInterval> gr = groupProcessor
				.getGroupingPart(object);
		if (percentiles == null){
			return gr.append(groupHistogram.getMedian());
		}else{
			return gr.appendList(groupHistogram.getPercentiles(percentiles), true);
		}
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalFastMedianHistogramPO)){
			return false;
		}
		
		@SuppressWarnings("unchecked")
		RelationalFastMedianHistogramPO<T> po = (RelationalFastMedianHistogramPO<T>) ipo;
		if (po.roundfactor != this.roundfactor){
			return false;
		}
		
		return super.isSemanticallyEqual(ipo);
	}
	
	

}
