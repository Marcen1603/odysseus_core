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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.Benchmark;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkGroup;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkHolder;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkMetadata;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.model.BenchmarkResult;
import de.uniol.inf.is.odysseus.rcp.benchmarker.gui.view.ProjectView;

/**
 * Diese Klasse speichert/lädt die Objekte/.XMLs und löscht Ergebnisse
 * 
 * @author Stefanie Witzke
 * 
 */
public class BenchmarkStoreUtil {

	private static final String RESULT_PREFIX = "result";
	private static final String RELATIVE_FOLDER = "Benchmarks";

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
		String folderName = createFolderName(benchmark);
		new File(folderName).mkdirs();
		return folderName;
	}

	private static String createFolderName(Benchmark benchmark) {
		BenchmarkGroup benchmarkGroup = benchmark.getParentGroup();

		String folderName = StringUtils.nameToFoldername(benchmarkGroup.getName());
		folderName = RELATIVE_FOLDER + File.separator + folderName + File.separator + benchmark.getId();
		return folderName;
	}

	private static void storeBenchmarkInternal(String folderName, Benchmark benchmark) {
		BenchmarkMetadata metadata = benchmark.getMetadata();
		FileOutputStream fos = null;
		try {
			String folder = folderName + File.separator;
			String path = folder + "param.xml";

			File outputFile = new File(path);
			fos = new FileOutputStream(outputFile);
			XMLEncoder encoder = new XMLEncoder(fos);
			encoder.writeObject(benchmark);
			encoder.close();

			FileOutputStream fosMeta = null;
			try {
				File outputMetadataFile = new File(folder + "metadata.xml");
				fosMeta = new FileOutputStream(outputMetadataFile);
				XMLEncoder metadataEncoder = new XMLEncoder(fosMeta);
				metadataEncoder.writeObject(metadata);
				metadataEncoder.close();
			} finally {
				close(fosMeta);
			}
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

	public static void loadResultsOfGroupAndBenchmark(Benchmark benchmark) {
		String benchmarkFolder = createFolderName(benchmark);

		File[] resultFiles = new File(benchmarkFolder).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String fileName = pathname.getName();
				if (fileName.startsWith(RESULT_PREFIX) && fileName.endsWith(".xml")) {
					return true;
				}
				return false;
			}
		});

		List<BenchmarkResult> results = new ArrayList<BenchmarkResult>(resultFiles.length);
		for (File resultFile : resultFiles) {
			results.add(loadBenchmarkResult(resultFile));
		}

		benchmark.setResults(results);
	}

	/**
	 * Diese Methode wandelt die XML-Dateien in Objekte um
	 * 
	 * @param group
	 * @param benchmarkDirectory
	 */
	private static void loadBenchmarksOfGroup(BenchmarkGroup group, File benchmarkDirectory) {
		if (!benchmarkDirectory.isDirectory()) {
			throw new RuntimeException("Can't load benchmarks. Want directory and no file with name: "
					+ benchmarkDirectory);
		}

		File[] benchmarkFiles = benchmarkDirectory.listFiles();
		Benchmark benchmark = null;
		BenchmarkMetadata metadata = null;
		List<BenchmarkResult> results = new ArrayList<BenchmarkResult>();
		for (File benchmarkFile : benchmarkFiles) {
			// param.xml in BenchmarkParam
			if ("param.xml".equals(benchmarkFile.getName())) {
				benchmark = loadBenchmarkObject(benchmarkFile, Benchmark.class);
				// result*.xml in BenchmarkResult
			} else if (benchmarkFile.getName().contains(RESULT_PREFIX) && benchmarkFile.getName().endsWith(".xml")) {
				results.add(loadBenchmarkResult(benchmarkFile));
				// metadata.xml in BenchmarkMetadata
			} else if ("metadata.xml".equals(benchmarkFile.getName())) {
				metadata = loadBenchmarkObject(benchmarkFile, BenchmarkMetadata.class);
			}
		}

		if (benchmark == null) {
			throw new IllegalStateException("Can't find Benchmark (param.xml)");
		}
		benchmark.setMetadata(metadata);
		benchmark.setResults(results);
		benchmark.setParentGroup(group);
		group.addBenchmark(benchmark);
	}

	/**
	 * Benchmark-Ergebnisse laden
	 * 
	 * @param benchmarkFile
	 * @return
	 */
	private static BenchmarkResult loadBenchmarkResult(File benchmarkFile) {
		BenchmarkResult result = new BenchmarkResult();
		String name = benchmarkFile.getName().substring(6);
		name = name.replace(".xml", "");
		result.setId(Integer.parseInt(name));
		BufferedReader reader = null;
		try {
			StringBuilder stringBuilder = new StringBuilder();
			reader = new BufferedReader(new FileReader(benchmarkFile));
			while (reader.ready()) {
				stringBuilder.append(reader.readLine()).append(System.getProperty("line.separator"));
			}
			result.setResultXml(stringBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T loadBenchmarkObject(File benchmarkPartFile, Class T) {

		FileInputStream fis = null;
		try {
			if (benchmarkPartFile.exists()) {
				fis = new FileInputStream(benchmarkPartFile);

				XMLDecoder decoder = new XMLDecoder(fis);
				return (T) decoder.readObject();
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

	/*
	 * Ergebnisse löschen
	 */

	/**
	 * Diese Methode löscht das übergebene Ergebnis
	 * 
	 * @param benchmark
	 * @param result
	 */
	public static void deleteResult(Benchmark benchmark, BenchmarkResult result) {
		String path = createFolderName(benchmark);
		path = path + File.separator + RESULT_PREFIX + result.getId() + ".xml";
		try {
			File file = new File(path);
			if (file.canWrite()) {
				file.delete();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		benchmark.getResults().remove(result);
		// TreeViewer aktualisieren
		Display.getDefault().asyncExec(new Runnable() {
			@Override
            public void run() {
				ProjectView.getDefault().refresh();
			}
		});
		// Result-Fenster schliessen
		PlatformUI
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.closeEditor(
						PlatformUI
								.getWorkbench()
								.getActiveWorkbenchWindow()
								.getActivePage()
								.findEditor(
										PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
												.getActiveEditor().getEditorInput()), false);
	}
}
