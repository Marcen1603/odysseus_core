package measure.windperformancercp.model.result;

import java.sql.Timestamp;

import org.eclipse.swt.graphics.RGB;

public class DataPoint {
	double windSpeed;
	double power;
	double variance;
	RGB color;
	Timestamp tstamp;
	
	public DataPoint(double ws, double p, double var, RGB col, Timestamp t){
		this.windSpeed = ws;
		this.power = p;
		this.variance = var;
		this.color = col;
		this.tstamp = t;
	}
	
	public void setTimeColor(){
		Timestamp actts = new Timestamp(System.currentTimeMillis());
		long diff = Math.abs(actts.getTime() -tstamp.getTime())/1000;
		
		color.blue = Math.max(0, (int)(color.blue - diff));
		color.red = Math.max(0, (int)(color.red -diff));
		color.green = Math.max(0, (int)(color.green -diff));
	}
	
	

}
