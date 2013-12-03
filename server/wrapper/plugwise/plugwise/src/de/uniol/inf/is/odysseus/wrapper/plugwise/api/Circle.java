package de.uniol.inf.is.odysseus.wrapper.plugwise.api;

/**
 * A Circle represent a specific plugwise element 
 * 
 * @author Marco Grawunder
 *
 */
public class Circle {
	
	final String mac;
	Double gain_a;
	Double gain_b;
	Double off_ruis;
	Double off_tot;
	
	
	public Circle(String mac) {
		// TODO: Validate mac-Adress
		this.mac = mac;
	}
	
	public void calibrate(){
//	      """fetch calibration info from the device
//        """
//        msg = PlugwiseCalibrationRequest(self.mac).serialize()
//        self._comchan.send_msg(msg)
//        calibration_response = self._expect_response(PlugwiseCalibrationResponse)
//        retl = []
//
//        for x in ('gain_a', 'gain_b', 'off_ruis', 'off_tot'):
//            val = getattr(calibration_response, x).value
//            retl.append(val)
//            setattr(self, x, val)
//
//        return retl
		
	}
	
	
	double pulse_correction(double pulses, long seconds){
		if (pulses == 0){
			return 0.0;
		}
		
		if (gain_a == null){
			calibrate();
		}
		
		pulses /=seconds;
		return seconds * (((Math.pow(pulses + off_ruis,2) * gain_b) + ((pulses + off_ruis) * gain_a)) + off_tot);
	}
	
	

}
