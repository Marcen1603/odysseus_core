/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkParam;

public class BenchmarkStoreUtil {

	private static final String RELATIVE_FOLDER = "Benchmarks";
//	private static final String BENCHMARK_FILENAME_PREFIX = "benchmark_";

	/*
	 * -------------------- Benchmarks speichern --------------------
	 */

	public static void storeBenchmark(Benchmark benchmark) {
		if (benchmark == null || benchmark.getId() < 1) {
			throw new IllegalArgumentException("Benchmark is not set or has no valid ID!");
		}

		String folderName = createFolderNameAndFolders(benchmark);
		storeBenchmarkInternal(folderName, benchmark);
	}

	private static String createFolderNameAndFolders(Benchmark benchmark) {
		BenchmarkGroup benchmarkGroup = benchmark.getParentGroup();

		String folderName = StringUtils.nameToFoldername(benchmarkGroup.getName());
		folderName = RELATIVE_FOLDER + File.separator + folderName + File.separator + benchmark.getId();
		new File(folderName).mkdirs();
		return folderName;
	}

	
	//TODO: Metadaten speichern!!!
	private static void storeBenchmarkInternal(String folderName, Benchmark benchmark) {
		FileOutputStream fos = null;
		try {
			//String path = folderName + File.separator + BENCHMARK_FILENAME_PREFIX + benchmark.getId() + ".xml";

			String path = folderName + File.separator + "param.xml";
			
			File outputFile = new File(path);
			fos = new FileOutputStream(outputFile);
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(benchmark);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			close(fos);
		}
	}

	/*
	 * -------------------- Benchmarks laden --------------------
	 */

	public static void loadAllBenchmarks() {
		File directory = new File(RELATIVE_FOLDER);
		if (directory.exists()) {
			if (!directory.isDirectory()) {
				throw new RuntimeException("Can't load benchmarks. Want subdirectory and no file with name: "
						+ RELATIVE_FOLDER);
			}

			File[] benchmarkGroupDirectories = directory.listFiles();
			for (File benchmarkGroupDir : benchmarkGroupDirectories) {
				loadBenchmarkGroup(benchmarkGroupDir);
			}
		}
	}

	private static void loadBenchmarkGroup(final File benchmarkGroupDir) {
		if (!benchmarkGroupDir.isDirectory()) {
			throw new RuntimeException("Can't load benchmarks. Want subdirectory and no file with name: "
					+ benchmarkGroupDir);
		}

		String benchmarkGroupName = benchmarkGroupDir.getName();
		final BenchmarkGroup group = new BenchmarkGroup(benchmarkGroupName);

		BenchmarkHolder.INSTANCE.addBenchmarkGroup(group);

		File[] benchmarks = benchmarkGroupDir.listFiles();
		for (File benchmark : benchmarks) {
			loadBenchmarksOfGroup(group, benchmark);
		}
		group.recalculateNextId();
	}

	private static void loadBenchmarksOfGroup(BenchmarkGroup group, File benchmarkDirectory) {
		if (!benchmarkDirectory.isDirectory()) {
			throw new RuntimeException("Can't load benchmarks. Want directory and no file with name: "
					+ benchmarkDirectory);
		}

		File[] benchmarkFiles = benchmarkDirectory.listFiles();
		Benchmark benchmark = null;
		for (File benchmarkFile : benchmarkFiles) {
			if ("param.xml".equals(benchmarkFile.getName())) {
				benchmark = loadBenchmarkParam(benchmarkFile);
//			} else if("result1.xml".equals(benchmarkFile.getName())){
//				benchmark = loadBenchmark(benchmarkFile);
			}
			// TODO: Lade Results
		
			else if("metadata.xml".equals(benchmarkFile.getName())) {
				benchmark = loadBenchmarkMetadata(benchmarkFile); //TODO: ist das so korrekt??
			}
		}
		benchmark.setParentGroup(group);
		group.addBenchmark(benchmark);
	}

	private static Benchmark loadBenchmarkParam(File benchmarkParamFile) {

		FileInputStream fis = null;
		try {
			if (benchmarkParamFile.exists()) {
				fis = new FileInputStream(benchmarkParamFile);

				XMLDecoder decoder = new XMLDecoder(fis);
				return (Benchmark) decoder.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(fis);
		}
		return null;
	}
	
	//????? SO RICHTIG????
	private static Benchmark loadBenchmarkMetadata(File benchmarkMetadataFile) {

		FileInputStream fis = null;
		try {
			if (benchmarkMetadataFile.exists()) {
				fis = new FileInputStream(benchmarkMetadataFile);

				XMLDecoder decoder = new XMLDecoder(fis);
				return (Benchmark) decoder.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(fis);
		}
		return null;
	}

	private static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void close(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
