package de.uniol.inf.is.odysseus.report.rcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class Util {
	
	private static final Logger LOG = LoggerFactory.getLogger(Util.class);
	
	public static void loadFromFile(final StringBuilder report, Path path, String title) {

		if (Files.exists(path)) {
			try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				report.append("\t* " + title + ":\n");
				String line = "";
				while ((line = reader.readLine()) != null) {
					report.append(line).append("\n");
				}
				report.append("\n");
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	
	public static List<String> read(IFile file) throws CoreException {
		if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
			file.refreshLocal(IResource.DEPTH_ZERO, null);
		}
		final Scanner lineScanner = new Scanner(file.getContents());

		final List<String> lines = Lists.newArrayList();
		while (lineScanner.hasNextLine()) {
			lines.add(lineScanner.nextLine());
		}

		lineScanner.close();

		return ImmutableList.copyOf(lines);
	}

}
