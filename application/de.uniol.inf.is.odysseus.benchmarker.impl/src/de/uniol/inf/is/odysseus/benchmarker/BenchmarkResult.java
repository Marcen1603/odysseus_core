package de.uniol.inf.is.odysseus.benchmarker;

import java.text.DecimalFormat;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class BenchmarkResult {
	public static class MyStats {
		private long[] values = new long[10000];
		private int count = 0;
		boolean notcalculated = true;
		private double standardDeviation;
		private double variance;
		private double sum = 0;
		private int offset;

		public int getN() {
			return this.count;
		}

		public void addValue(long latency) {
			if (!notcalculated) {// TODO variablenname
				throw new RuntimeException(
						"geht nicht mehr, array schon sortiert ;)");
			}
			if (count + 1 == values.length) {
				long[] tmp = values;
				values = new long[tmp.length * 3 / 2 + 1];
				System.arraycopy(tmp, 0, values, 0, tmp.length);
			}
			values[count++] = latency;
			sum += latency;
		}

		public double getMean() {
			return this.sum / this.count;
		}

		public double getStandardDeviation() {
			prepareStats();
			return this.standardDeviation;
		}

		private void prepareStats() {
			if (notcalculated == false) {
				return;
			}
			notcalculated = false;
			double accum = 0.0;
			double dev = 0.0;
			double accum2 = 0.0;
			double mean = getMean();
			Arrays.sort(this.values);
			this.offset = this.values.length - this.count;
			for (int i = offset; i < this.values.length; ++i) {
				dev = this.values[i] - mean;
				accum += dev * dev;
				accum2 += dev;
			}
			this.variance = (accum - (accum2 * accum2 / count)) / count;
			this.standardDeviation = Math.sqrt(this.variance);
		}

		public double getVariance() {
			prepareStats();
			return this.variance;
		}

		public long getMin() {
			prepareStats();
			return this.values[offset];
		}

		public long getMax() {
			prepareStats();
			return this.values[this.values.length - 1];
		}

		// public long calcMax() {
		// long max = Long.MIN_VALUE;
		// for(long v : this.values){
		// if (v>max){
		// max = v;
		// }
		// }
		// return max;
		// }

		public long getPercentile(int i) {
			prepareStats();
			int pos = offset + count * i / 100;
			if (count % 2 == 0) {
				// TODO nicht ganz genau wegen int div, sollte double returnen
				return (this.values[pos] + this.values[pos - 1]) / 2L;
			} else {
				return this.values[pos];
			}
		}

	}

	private long startTime;
	// final private SortedMap<Byte, long[]> priorizedElements = new
	// TreeMap<Byte, long[]>();
	// long[][] priorizedElements = new long[128][];
	// int[] priorizedSizes = new int[128];
	MyStats[] priorizedElements = new MyStats[128];
	// private List<RelationalTuple<? extends ILatency>> results = new
	// ArrayList<RelationalTuple<? extends ILatency>>();
	private long endTime;
	private int size = 0;
	private boolean writeResultsToFile;
	private String fileName;

	public int getPriorizedCount(Byte prio) {
		return (int) priorizedElements[prio].getN();
	}

	public void setWriteResultsToFile(boolean write) {
		this.writeResultsToFile = write;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	public MyStats getPriorizedStatistics(int prio) {
		return priorizedElements[prio];
	}

	public void setStartTime(long start) {
		this.startTime = start;
	}

	public void setEndTime(long end) {
		this.endTime = end;
	}

	public long getDuration() {
		return this.endTime - this.startTime;
	}

	// should be T extends ILatency & IPriority, so no casts are needed, bug
	// suns compiler (1.6) is buggy and doesn't accept it
	public <T extends ILatency> void add(T object) {
		++size;
		Byte prio = 0;
		if (object instanceof IPriority) {
			prio = ((IPriority) object).getPriority();
		}
		MyStats desc = this.priorizedElements[prio];
		if (desc == null) {
			desc = new MyStats();
			this.priorizedElements[prio] = desc;
		}
		desc.addValue(object.getLatency());
		// long[] elems = priorizedElements[prio];
		// if (elems == null) {
		// elems = new long[100000];
		// this.priorizedElements[prio] = elems;
		// this.priorizedSizes[prio] = 0;
		// }
		// if (this.priorizedSizes[prio] == this.priorizedElements[prio].length
		// - 1) {
		// long[] tmp = this.priorizedElements[prio];
		// this.priorizedElements[prio] = new long[tmp.length * 2 + 1];
		// System.arraycopy(tmp, 0, this.priorizedElements[prio], 0,
		// tmp.length);
		// }
		// elems[this.priorizedSizes[prio]++] = (object.getLatency());
	}

	// public <T extends ILatency> void add(RelationalTuple<T> rt) {
	// this.results.add(rt);
	// }

	private int correctlyIdentified = 0;
	private int wrongIdentified = 0;
	private int totalNoOfCorrectResults = 0;
	private int totalResults = 0;

	public void calculateMovingObjectsResult(RelationalTuple element) {

		totalResults++;

		// if identifier are equal and timestamp are not equal
		// the element has been identified correctly
		if (element.getAttribute(2).toString().equals(element.getAttribute(12)) /*
																				 * &&
																				 * !
																				 * element
																				 * .
																				 * getAttribute
																				 * (
																				 * 5
																				 * )
																				 * .
																				 * toString
																				 * (
																				 * )
																				 * .
																				 * equals
																				 * (
																				 * element
																				 * .
																				 * getAttribute
																				 * (
																				 * 15
																				 * )
																				 * )
																				 */) {
			correctlyIdentified++;
		}
		// if identifiers are not equal and timestamps are equal
		// or not, the element has not been identified correctly
		else if (!element.getAttribute(2).toString().equals(
				element.getAttribute(12))) {
			wrongIdentified++;
		}
	}

	public double getPrecision() {
		return (double) correctlyIdentified / (double) totalResults;
	}

	public double getRecall() {
		return (double) correctlyIdentified / (double) totalNoOfCorrectResults;
	}

	public void printPrecisionAndRecall() {
		double recall = (double) correctlyIdentified
				/ (double) totalNoOfCorrectResults;
		double precision = (double) correctlyIdentified / (double) totalResults;
		System.out.println("Recall: " + recall);
		System.out.println("Precision: " + precision);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("  <Result>\n");
		ret.append("    <Duration>" + getDuration() + "</Duration>\n");
		DecimalFormat nf = new DecimalFormat("###0.##");
		for (Byte key = 0; key < 127; ++key) {
			if (this.priorizedElements[key] == null) {
				continue;
			}
			ret.append("    <Priority value='" + key + "'>\n");
			MyStats stat = getPriorizedStatistics(key);

			ret.append("       <Count>" + stat.getN() + "</Count>\n");
			ret.append("       <AVG>" + nf.format(stat.getMean()) + "</AVG>\n");
			ret.append("       <STDDEV>"
					+ nf.format(stat.getStandardDeviation()) + "</STDDEV>\n");
			ret.append("       <VARIATIONSKOEFFIZIENT>"
					+ nf.format(stat.getStandardDeviation() / stat.getMean())
					+ "</VARIATIONSKOEFFIZIENT>\n");
			ret.append("       <VARIATIONCOEFFICIENTREF0>"
					+ nf.format(stat.getStandardDeviation()
							/ getPriorizedStatistics(0).getMean())
					+ "</VARIATIONCOEFFICIENTREF0>\n");

			ret.append("       <MIN>" + nf.format(stat.getMin())

			+ "</MIN>\n");
			ret.append("       <MAX>" + nf.format(stat.getMax()) + "</MAX>\n");
			// ret.append("       <CALCMAX>" + nf.format(stat.calcMax())
			// + "</CALCMAX>\n");
			ret.append("       <Median>" + nf.format(stat.getPercentile(50))
					+ "</Median>\n");
			int[] v = new int[] { 10, 25, 50, 75, 90 };
			for (int i : v) {
				ret.append("       <p" + i + ">"
						+ nf.format(stat.getPercentile(i)) + "</p" + i + ">\n");
			}
			ret.append("    </Priority>\n");
			ret.append("    <Precision>" + this.getPrecision()
					+ "</Precision>\n");
			ret.append("    <Recall>" + this.getRecall() + "</Recall>\n");
		}
		ret.append("  </Result>");

		// if (this.writeResultsToFile) {
		// // Ausgabe der Ergebnisse, sortiert nach Latenzen
		// Comparator<RelationalTuple<? extends ILatency>> comp = new
		// Comparator<RelationalTuple<? extends ILatency>>() {
		// public int compare(RelationalTuple<? extends ILatency> left,
		// RelationalTuple<? extends ILatency> right) {
		// if (left.getMetadata().getLatency() == right.getMetadata()
		// .getLatency()) {
		// return 0;
		// }
		// return left.getMetadata().getLatency() < right
		// .getMetadata().getLatency() ? -1 : 1;
		// }
		// };
		//
		// Collections.sort(this.results, comp);
		// try {
		// File file = new File(this.fileName);
		// StringBuffer sb = new StringBuffer();
		// FileWriter writer = new FileWriter(file);
		// for (RelationalTuple<?> rt : this.results) {
		// sb.append(rt.toString() + "\n");
		// }
		// writer.append(sb.toString());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		return ret.toString();
	}

	public long size() {
		return size;
	}

}
