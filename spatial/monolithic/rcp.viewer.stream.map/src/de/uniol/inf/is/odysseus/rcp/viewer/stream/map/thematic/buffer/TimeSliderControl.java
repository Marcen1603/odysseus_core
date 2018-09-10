//package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;
//
//import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
//
//
///**
// * 
// * Control class for the TimeSliderComposite
// * 
// * @see TimeSliderComposite
// * @author Stefan Bothe
// * 
// */
//public class TimeSliderControl {
//	private TimeSliderComposite timeSliderComposite;
//	private StreamInputReader buffer;
//	public TimeSliderControl(StreamInputReader buffer, TimeSliderComposite timeSliderComposite) {
//		this.timeSliderComposite = timeSliderComposite;
//		this.timeSliderComposite.setTimeSliderControl(this);
//		this.buffer = buffer;
//	}
//	public Long getIntervalMin(){
//		return timeSliderComposite.getIntervalMin();
//	}
//	public Long getIntervalMax(){
//		return timeSliderComposite.getIntervalMax();
//	}
//	public void updateCanvas() {
//		buffer.draw(getIntervalMin(), getIntervalMax());
//	}
//	public void updateSliderRange(PointInTime startPointInTime, PointInTime endPointInTime) {
//		timeSliderComposite.updateSliderInterval(startPointInTime.getMainPoint(), endPointInTime.getMainPoint()+1);
//	}
//}
