package de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractSimplePullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class Temper1TransportHandler extends
		AbstractSimplePullTransportHandler<Tuple<?>> {
	private static Logger LOG = LoggerFactory
			.getLogger(Temper1TransportHandler.class);

	public static final String NAME = "Temper1";
	private static final int VENDOR_ID = 3141;
	private static final int PRODUCT_ID = 29697;
	private static final int BUFSIZE = 2048;
	private static ArrayList<HIDDeviceInfo> temperaturSensors = new ArrayList<HIDDeviceInfo>();
	public static final String TEMPNUMBER = "tempnumber";
	public static final String METHOD_HID_MANAGER = "hidmanager";
	public static final String METHOD_RPI_TEMPER = "rpitemper";

	private static final String RPI_TEMPER_BIN = "/rpitemper.bin";

	private static final long DELTA_THROWEXCEPTIONS = 5000;

	static {
		ClassPathLibraryLoader.loadNativeHIDLibrary();
	}

	private int currentTempNumber;
	private long lastUpdateConnectedTemperatureSensorsTime;
	private long DELTATIME_FOR_DEVICE_UPDATE_MS = 10000;
	private HIDManager hidManager;
	private static String methodToGetTemperature;
	private static String rpiTemper1TempPath;
	private static boolean initRPiTemperBinRunSuccessfull = false;
	private static boolean exceptionWasThrownInitRPiTemperBin = false;
	private static long exceptionThrownInitRPiTemperBinLastTime;
	private static long lastExceptionThrown;
	private static long firstFailureTime;
	private static boolean couldNotParseTemperature=false;
	
	public Temper1TransportHandler() {
		try {
			setHidManager(HIDManager.getInstance());

			methodToGetTemperature = METHOD_HID_MANAGER;
		} catch (UnsatisfiedLinkError ex) {
			LOG.error(ex.getMessage(), ex);

			// use the fallback program:
			methodToGetTemperature = METHOD_RPI_TEMPER;
			initRPiTemperBin();
		} catch (IOException ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	public Temper1TransportHandler(Temper1TransportHandler other) {

	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		updateConnectedTemperatureSensors();

		Temper1TransportHandler tHandler = new Temper1TransportHandler();

		if (options.containsKey(TEMPNUMBER)) {
			tHandler.setTempNumber(options.get(TEMPNUMBER));
		}

		protocolHandler.setTransportHandler(tHandler);

		return tHandler;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tuple<?> getNext() {
		Tuple<?> tuple = new Tuple(2, false);

		int deviceNumber = this.currentTempNumber < temperaturSensors.size() ? this.currentTempNumber
				: 0;

		try {
			tuple.setAttribute(0, getTemperature(deviceNumber));
			tuple.setAttribute(1, getTemperature(deviceNumber));
			// readDevice(temperaturSensors.get(deviceNumber)));

			/*
			 * tuple.setAttribute(0, readDevice(temperaturSensors.get(--i)));
			 * if(i==0){ i=temperaturSensors.size(); }
			 */
		} catch (IOException e) {
			tuple.setAttribute(0, getSimulatedTemperature());
			tuple.setAttribute(1, getSimulatedTemperature());
			updateConnectedTemperatureSensors();
		} catch (Exception e) {
			tuple.setAttribute(0, getSimulatedTemperature());
			tuple.setAttribute(1, getSimulatedTemperature());
			updateConnectedTemperatureSensors();
		}

		// tuple.setAttribute(1, new SDFTimeUnit("YYYY-MM-dd HH-mm-ss"));

		return tuple;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

	public static String getMethodToGetTemperature() {
		return Temper1TransportHandler.methodToGetTemperature;
	}

	public static void setMethodToGetTemperature(String methodToGetTemperature) {
		Temper1TransportHandler.methodToGetTemperature = methodToGetTemperature;
	}

	private void setTempNumber(String tempNumber) {
		Integer tempNumberInteger = Integer.parseInt(tempNumber);

		this.currentTempNumber = tempNumberInteger.intValue();
	}

	private float getTemperature(int deviceNumber) throws Exception {
		switch (methodToGetTemperature) {
		case METHOD_HID_MANAGER:
			return getTemperatureFromHIDManager(temperaturSensors
					.get(deviceNumber));
		case METHOD_RPI_TEMPER:
			return getTemperatureFromRPiTemperBinary(deviceNumber);
		}
		throw new IOException("No temperature sensor available.");
	}

	/*****************************************************************/
	// Temper1 get temperature with the HID-API
	/*****************************************************************/

	/**
	 * Nach Quelle:
	 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi
	 * -on-os-x-to-read-temperature-from-the-temper1-sensor/
	 * 
	 * @param hidDeviceInfo
	 * @throws IOException
	 * 
	 */
	@SuppressWarnings("unused")
	private static float getTemperatureFromHIDManager(HIDDeviceInfo device)
			throws IOException {
		HIDDevice dev;

		HIDManager hid_mgr = HIDManager.getInstance();
		// dev = hid_mgr.openById(VENDOR_ID, PRODUCT_ID, null);
		dev = hid_mgr.openByPath(device.getPath());

		byte[] temp = new byte[] { (byte) 0x01, (byte) 0x80, (byte) 0x33,
				(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

		try {
			int res = dev.write(temp);

			byte[] buf = new byte[BUFSIZE];
			int n = dev.read(buf);

			int rawtemp = (buf[3] & (byte) 0xFF) + (buf[2] << 8);
			if ((buf[2] & 0x80) != 0) {
				/* return the negative of magnitude of the temperature */
				rawtemp = -((rawtemp ^ 0xffff) + 1);
			}

			return c_to_u(raw_to_c(rawtemp), 'C');
		} finally {
			dev.close();
			hid_mgr.release();
		}
	}

	/**
	 * Quelle:
	 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on
	 * -os-x-to-read-temperature-from-the-temper1-sensor/
	 * 
	 * @param rawtemp
	 * @return
	 */
	private static float raw_to_c(int rawtemp) {
		float temp_c = rawtemp * (125.f / 32000.f);
		return temp_c;
	}

	/**
	 * Quelle:
	 * http://www.igorkromin.net/index.php/2013/02/16/using-java-hidapi-on
	 * -os-x-to-read-temperature-from-the-temper1-sensor/
	 * 
	 * @param deg_c
	 * @param unit
	 * @return
	 */
	private static float c_to_u(float deg_c, char unit) {
		if (unit == 'F')
			return (deg_c * 1.8f) + 32.f;
		else if (unit == 'K')
			return (deg_c + 273.15f);
		else
			return deg_c;
	}

	public HIDManager getHidManager() {
		return hidManager;
	}

	public void setHidManager(HIDManager hidManager) {
		this.hidManager = hidManager;
	}

	private void updateConnectedTemperatureSensors() {
		if (methodToGetTemperature == null || getHidManager() == null
				|| methodToGetTemperature.isEmpty()
				|| !methodToGetTemperature.equals(METHOD_HID_MANAGER)) {
			return;
		}

		long delta = System.currentTimeMillis()
				- lastUpdateConnectedTemperatureSensorsTime;

		if (delta >= DELTATIME_FOR_DEVICE_UPDATE_MS) {
			temperaturSensors.clear();
			try {
				// com.codeminders.hidapi.ClassPathLibraryLoader.loadNativeHIDLibrary();
				HIDDeviceInfo[] infos = HIDManager.getInstance().listDevices();

				for (HIDDeviceInfo info : infos) {
					if (info.getVendor_id() == VENDOR_ID
							&& info.getProduct_id() == PRODUCT_ID
							&& info.getUsage() == 1) {
						if (!temperaturSensors.contains(info)) {
							temperaturSensors.add(info);
						}
					}
				}
			} catch (IOException ex) {
				LOG.error(ex.getMessage(), ex);
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}

			lastUpdateConnectedTemperatureSensorsTime = System
					.currentTimeMillis();
		}
	}

	/*****************************************************************/
	// Temper1 get temperature with the fallback program: rpitemper.bin
	// note: in some cases the temper1 is not working
	/*****************************************************************/
	private static void initRPiTemperBin() {
		try {
			String path = RPI_TEMPER_BIN;
			InputStream in = ClassPathLibraryLoader.class
					.getResourceAsStream(path);
			if (in != null) {
				try {
					// always write to different location
					String tempName = path.substring(path.lastIndexOf('/') + 1);
					File fileOut = File.createTempFile(tempName.substring(0,
							tempName.lastIndexOf('.')), tempName.substring(
							tempName.lastIndexOf('.'), tempName.length()));
					fileOut.deleteOnExit();

					OutputStream out = new FileOutputStream(fileOut);
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					out.close();
					// Runtime.getRuntime().load(fileOut.toString());

					// rpiTemperPath = tempName;
					setRpiTemperPath(fileOut.getAbsolutePath());

				} finally {
					in.close();
					setInitRPiTemperBinRunSuccessfull(true);
				}
			}
			if (exceptionWasThrownInitRPiTemperBin) {
				exceptionWasThrownInitRPiTemperBin = false;
			}
		} catch (IOException ex) {
			long delta = System.currentTimeMillis()
					- exceptionThrownInitRPiTemperBinLastTime;
			if (!exceptionWasThrownInitRPiTemperBin
					|| delta >= DELTA_THROWEXCEPTIONS) {
				ex.printStackTrace();
				exceptionThrownInitRPiTemperBinLastTime = System
						.currentTimeMillis();
				exceptionWasThrownInitRPiTemperBin = true;
			}
		}
	}

	// This method is a fallback for some raspberry pi models
	private static synchronized float getTemperatureFromRPiTemperBinary(
			int deviceNumber) throws Exception {
		try {
			boolean valueReturned=false;
			int tries=0;
			while(!valueReturned){
				try{
					final Process p = Runtime.getRuntime()
							.exec(getRpiTemper1TempPath());
					
					BufferedReader input = new BufferedReader(new InputStreamReader(
							p.getInputStream()));
					String line = null;

					try {
						int lineNumber = 0;
						while ((line = input.readLine()) != null) {
							if (lineNumber == deviceNumber) {
								String temperature = parseTemperature(line);
								Float temperatureFloat = Float.parseFloat(temperature);
								return temperatureFloat.floatValue();
							}
						}
					} catch (IOException e) {
						LOG.debug("IOException 111110000");
						LOG.error(e.getMessage(), e);
					} catch (IllegalStateException e) {
						throw e;
					}
					p.waitFor();
					
					
					valueReturned=true;
					couldNotParseTemperature=false;
					tries=0;
				} catch(IllegalStateException e){
					tries++;
					//LOG.debug("0000");
					// no temperature value could be parsed. try again in a loop
					long delta = System.currentTimeMillis() - firstFailureTime;
					if(!couldNotParseTemperature){
						LOG.debug(e.getMessage(), e);
						firstFailureTime=System.currentTimeMillis();
						couldNotParseTemperature=true;
					}else if(couldNotParseTemperature && delta >= DELTA_THROWEXCEPTIONS){
						couldNotParseTemperature=false;
						firstFailureTime=System.currentTimeMillis();
						throw new Exception("No temperature value available. (trys to get temp:"+tries+" in:"+delta+" sec.)");
					}
				}
			}	
		} catch (IOException e) {
			LOG.debug("22222");
			LOG.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOG.debug("33333");
			LOG.error(e.getMessage(), e);
		} catch (Exception e) {
			LOG.debug("44444");
			LOG.error(e.getMessage(), e);
		}
		throw new Exception("No temperature value available.");
	}

	private static String parseTemperature(String line) {
		// Example: 2014/10/16 17:23:41 Temperature 71.71F 22.06C
		Pattern pattern = Pattern.compile(".*([0-9]{2}\\.[0-9]{2})[C]$");

		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			return matcher.group(1);
		} else {
			throw new IllegalStateException("No match found");
		}
	}

	public static String getRpiTemper1TempPath() {
		if (!isInitRPiTemperBinRunSuccessfull()) {
			initRPiTemperBin();
		}

		return rpiTemper1TempPath;
	}

	public static void setRpiTemperPath(String rpiTemper1TempPath) {
		LOG.debug(RPI_TEMPER_BIN + " path:" + rpiTemper1TempPath);
		Temper1TransportHandler.rpiTemper1TempPath = rpiTemper1TempPath;

		chmod("777", rpiTemper1TempPath, true);
	}

	private static void chmod(String mod, String rpiTemper1TempPath,
			boolean wait) {
		try {
			Process p1 = Runtime.getRuntime().exec(
					"chmod " + mod + " " + rpiTemper1TempPath);
			if (wait) {
				p1.waitFor();
			}
		} catch (IOException | InterruptedException e) {
			long delta = System.currentTimeMillis() - lastExceptionThrown;
			if (delta >= DELTA_THROWEXCEPTIONS) {
				e.printStackTrace();
				lastExceptionThrown = System.currentTimeMillis();
			}
		}
	}

	public static boolean isInitRPiTemperBinRunSuccessfull() {
		return initRPiTemperBinRunSuccessfull;
	}

	public static void setInitRPiTemperBinRunSuccessfull(
			boolean initRPiTemperBinRunSuccessfull) {
		Temper1TransportHandler.initRPiTemperBinRunSuccessfull = initRPiTemperBinRunSuccessfull;
	}

	/*****************************************************************/
	// get simulated temperature values
	/*****************************************************************/
	private static float getSimulatedTemperature() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		float seconds = calendar.get(Calendar.SECOND);

		float y = 0;
		if (seconds >= 30) {
			y = ((seconds * -1) + 60) / 60; // 30...0
		} else {
			y = seconds / 60;// 0...30
		}

		return 10 + y * 30; // 10..15..20..25..20..15..10
	}
}
