package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;


public class NetworkMonitor extends Thread {
	private static final Sigar sigar = new Sigar();
	public static void main(String[] args) {
		new Thread(new NetworkMonitor()).start();
	}

	private long bandwidthInKBs;

	public NetworkMonitor() {
		try {
			this.bandwidthInKBs = sigar.getNetInterfaceStat(sigar.getNetInterfaceConfig(null).getName()).getSpeed() / 1000;

		} catch (SigarException e) {
			e.printStackTrace();
		}
	}

	private int MEASUREMENTS = 10;
	private double inputRate;
	private double outputRate;
	private long previousInputTotal = 0;
	private long previousOutputTotal = 0;
	private ArrayList<Double> inputRates = new ArrayList<>();
	private ArrayList<Double> outputRates = new ArrayList<>();

	private double[] collect() throws SigarException {
		boolean firstOne = previousInputTotal == 0.0 && previousOutputTotal == 0.0;
		double[] networkData = new double[2];
		
		String interfaceName = sigar.getNetInterfaceConfig(null).getName();
		NetInterfaceStat net = sigar.getNetInterfaceStat(interfaceName);
		
		long newIn = net.getRxBytes();
		long newOut = net.getTxBytes();
		
		// rates are in kb/s
		networkData[0] = (newIn - previousInputTotal) / 1000.0;
		networkData[1] = (newOut - previousOutputTotal) / 1000.0;

		previousInputTotal = newIn;
		previousOutputTotal = newOut;
		if(firstOne) {
			return new double[] {0.,0.};
		}
		
		if(inputRates.size() >= MEASUREMENTS) {
			inputRates.remove(0);
		}
		inputRates.add(networkData[0]);
		
		if(outputRates.size() >= MEASUREMENTS) {
			outputRates.remove(0);
		}
		outputRates.add(networkData[1]);
		return networkData;
	}

	public double getInputRate() {
		return inputRate;
	}

	public double getOutputRate() {
		return outputRate;
	}

	@Override
	public void run() {
		while (true) {
			try {
				double[] current = collect();
				this.inputRate = current[0];
				this.outputRate = current[1];
				TimeUnit.SECONDS.sleep(1);
				//System.out.println("in: " + this.inputRate + " kb/s, out: " + this.outputRate + " kb/s");
				//System.out.println("in (mean): " + this.getMeanInputRate() + " kb/s, out (mean): " + this.getMeanOutputRate() + " kb/s");
				//System.out.println(this.getNetUsage());
			} catch (SigarException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setInputRate(double inputRate) {
		this.inputRate = inputRate;
	}

	public void setOutputRate(double outputRate) {
		this.outputRate = outputRate;
	}
	
	public double getMeanInputRate() {
		int size = inputRates.size();
		if(size == 0) {
			return 0;
		}
		double sum = 0;
		for(double d : inputRates) {
			sum+=d;
		}
		return sum/size;
	}
	
	public double getMeanOutputRate() {
		int size = outputRates.size();
		if(size == 0) {
			return 0;
		}
		double sum = 0;
		for(double d : outputRates) {
			sum+=d;
		}
		return sum/size;
	}
	
	public double getNetUsage() {
		return (getMeanInputRate() + getMeanOutputRate()) / this.bandwidthInKBs;
	}
}