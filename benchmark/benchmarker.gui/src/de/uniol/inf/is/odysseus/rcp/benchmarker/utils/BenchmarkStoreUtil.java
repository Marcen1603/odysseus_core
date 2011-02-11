package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkIdHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

public class BenchmarkStoreUtil {

	private static final String RELATIVE_FOLDER = "benchmarks";
	private static final String BENCHMARK_FILENAME_PREFIX = "benchmark_";

	public static Benchmark storeBenchmarkParam(BenchmarkParam param) {
		Benchmark benchmark = BenchmarkHolder.INSTANCE.getBenchmark(param.getId());

		boolean newEntity = false;
		if (benchmark == null) {
			newEntity = true;
			benchmark = new Benchmark(param);
		}
		if (param.getId() <= 0) {
			param.setId(BenchmarkIdHolder.INSTANCE.generateNextId());
		}

		storeBenchmark(benchmark);
		if (newEntity) {
			BenchmarkHolder.INSTANCE.addBenchmarkIfNotExists(benchmark);
		}
		return benchmark;
	}

	private static void storeBenchmark(Benchmark benchmark) {
		FileOutputStream fos = null;
		try {
			File outputFile = new File(RELATIVE_FOLDER, BENCHMARK_FILENAME_PREFIX + benchmark.getId() + ".xml");
			fos = new FileOutputStream(outputFile);
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(benchmark);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void loadAllBenchmarks() {
		File directory = new File(RELATIVE_FOLDER);
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				throw new RuntimeException("Can't load benchmarks. Wan't subdirectory and no file with name: "
						+ RELATIVE_FOLDER);
			}

			File[] benchmarkFiles = directory.listFiles();
			for (File benchmarkFile : benchmarkFiles) {
				loadBenchmarksFromFile(benchmarkFile);
			}
			// Nächste ID berechnen
			BenchmarkIdHolder.INSTANCE.calculateNextId(BenchmarkHolder.INSTANCE.getBenchmarks());
		}
	}

	private static void loadBenchmarksFromFile(File file) {
		FileInputStream fis = null;
		try {
			if (file.exists()) {
				fis = new FileInputStream(file);

				XMLDecoder decoder = new XMLDecoder(fis);
				Benchmark benchmark = (Benchmark) decoder.readObject();
				BenchmarkHolder.INSTANCE.addBenchmarkIfNotExists(benchmark);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
