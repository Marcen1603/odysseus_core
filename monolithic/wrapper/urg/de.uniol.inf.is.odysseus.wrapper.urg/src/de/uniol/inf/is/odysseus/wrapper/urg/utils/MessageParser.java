package de.uniol.inf.is.odysseus.wrapper.urg.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.wrapper.urg.datatype.UrgScann;
import de.uniol.inf.is.odysseus.wrapper.urg.datatype.UrgScann.Precision;

public class MessageParser {
	public static UrgScann parseMessage(ByteBuffer buffer) {
		UrgScann res = new UrgScann();
		byte[] buf = new byte[buffer.capacity()];
		buffer.get(buf);
		BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf)));
		
		try {
			String line = in.readLine();
			
			// magic starting number
			if (line.charAt(0) != 'M')
				return res;
			
			// set precision
			char precision = line.charAt(1);
			if (precision == 'D') {
				res.setPrecision(Precision.Double);
			} else if (precision == 'S') {
				res.setPrecision(Precision.Single);
			} else {
				return res;
			}
			
			res.setStartingStep(Integer.parseInt(line.substring(2, 6)));
			res.setEndStep(Integer.parseInt(line.substring(6, 10)));
			res.setClusterCount(Integer.parseInt(line.substring(10, 12)));
			res.setScanInterval(Integer.parseInt(line.substring(12, 13)));
			res.setRemainingScans(Integer.parseInt(line.substring(13, 15)));
			
			if (line.length() > 16) {
				res.setMessage(line.substring(16));
			}
			
			// status code
			line = in.readLine();
			res.setStatusCode(Integer.parseInt(line.substring(0, 2)));
			
			if (res.getStatusCode() != 99)
				return res;		// we are done here, no data (error, or answer)
			
			// time stamp
			line = in.readLine();
			res.setTimeStamp(decodeFourCharacter(line.substring(0, 4)));
			
			// data
			StringBuilder sb = new StringBuilder();
			while (in.ready()) {
				sb.append(in.readLine());
				if (in.ready())
					sb.deleteCharAt(sb.length() - 1);
			}
			
			float[] data = new float[res.getEndStep() - res.getStartingStep() + 1];
			if (res.getPrecision() == Precision.Double) {
				for (int i = 0; i < data.length; i++) {
					data[i] = decodeThreeCharacter(sb.substring(i * 3, i * 3 + 3));
				}
			} else {
				for (int i = 0; i < data.length; i++) {
					data[i] = decodeTwoCharacter(sb.substring(i * 2, i * 2 + 2));
				}
			}
			res.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static int decodeFourCharacter(String in) {
		byte[] res = new byte[4];
		res[0] = (byte)(in.charAt(0) - '0');
		res[1] = (byte)(in.charAt(1) - '0');
		res[2] = (byte)(in.charAt(2) - '0');
		res[3] = (byte)(in.charAt(3) - '0');
		return byteArrayToInt24(res);
	}
	
	public static int decodeThreeCharacter(String in) {
		byte[] res = new byte[3];
		res[0] = (byte)(in.charAt(0) - '0');
		res[1] = (byte)(in.charAt(1) - '0');
		res[2] = (byte)(in.charAt(2) - '0');
		return byteArrayToInt18(res);
	}
	
	public static int decodeTwoCharacter(String in) {
		byte[] res = new byte[2];
		res[0] = (byte)(in.charAt(0) - '0');
		res[1] = (byte)(in.charAt(1) - '0');
		return byteArrayToInt12(res);
	}
	
	public static int byteArrayToInt24(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 6 |
	            (b[1] & 0xFF) << 12 |
	            (b[0] & 0xFF) << 18;
	}
	
	public static int byteArrayToInt18(byte[] b) 
	{
	    return   b[2] & 0xFF |
	            (b[1] & 0xFF) << 6 |
	            (b[0] & 0xFF) << 12;
	}
	
	public static int byteArrayToInt12(byte[] b)
	{
	    return   b[1] & 0xFF |
	            (b[0] & 0xFF) << 6;
	}
}
