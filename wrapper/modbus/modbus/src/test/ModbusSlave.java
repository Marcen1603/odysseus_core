package test;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.ModbusCoupler;
import com.ghgande.j2mod.modbus.net.ModbusTCPListener;
import com.ghgande.j2mod.modbus.procimg.SimpleDigitalIn;
import com.ghgande.j2mod.modbus.procimg.SimpleDigitalOut;
import com.ghgande.j2mod.modbus.procimg.SimpleInputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;

public class ModbusSlave {

	public static void main(String[] args) {
		try {
			/* The important instances and variables */
			ModbusTCPListener listener = null;
			SimpleProcessImage spi = null;
			int port = Modbus.DEFAULT_PORT;

			// 1. Set port number from commandline parameter
			if (args != null && args.length == 1) {
				port = Integer.parseInt(args[0]);
			}

			// 2. Prepare a process image
			spi = new SimpleProcessImage();
			spi.addDigitalOut(new SimpleDigitalOut(true));
			spi.addDigitalOut(new SimpleDigitalOut(false));
			spi.addDigitalIn(new SimpleDigitalIn(false));
			spi.addDigitalIn(new SimpleDigitalIn(true));
			spi.addDigitalIn(new SimpleDigitalIn(false));
			spi.addDigitalIn(new SimpleDigitalIn(true));
			spi.addRegister(new SimpleRegister(251));
			spi.addInputRegister(new SimpleInputRegister(45));

			// 3. Set the image on the coupler
			ModbusCoupler.getReference().setProcessImage(spi);
			ModbusCoupler.getReference().setMaster(false);
			ModbusCoupler.getReference().setUnitID(15);

			// 4. Create a listener with 3 threads in pool
			listener = new ModbusTCPListener(3);
			listener.setPort(port);
			System.err.println("Starting new Slave on port "+port);
			listener.listen();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}// main

}
