package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.buffer;



public class TimeSliderControl {
	private TimeSliderComposite timeSliderComposite;
	private DynamicBuffer buffer;
	public TimeSliderControl(DynamicBuffer buffer, TimeSliderComposite timeSliderComposite) {
		this.timeSliderComposite = timeSliderComposite;
		this.timeSliderComposite.setTimeSliderControl(this);
		this.buffer = buffer;
	}
	public void setMaxValue(int size) {
		if(size<1){
			timeSliderComposite.setMaxValue(1);
		}else{
			timeSliderComposite.setMaxValue(size); 
		}
	}
	public int getIntervalMin(){
		return timeSliderComposite.getIntervalMin();
	}
	public int getIntervalMax(){
		return timeSliderComposite.getIntervalMax();
	}
	public void updateCanvas() {
		buffer.draw(getIntervalMin(), getIntervalMax());
	}
	public Long getTimestampToString(int value){
		if(buffer.getTimeList().size()>=(value-1) && buffer.getTimeList().get(value)!=null){
			return buffer.getTimeList().get(value).getMainPoint();
		}else{
			return null;
		}
	}
}
