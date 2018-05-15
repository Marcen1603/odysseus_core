package de.uniol.inf.is.odysseus.test.component.parser.cql;

import java.io.File;
import java.util.List;

public class CQLParserUtil {

	public static void searchQueryFilesRecursive(File dir, List<File> queryFiles, List<File> pqlFiles) {
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				if (child.isDirectory())
					searchQueryFilesRecursive(child, queryFiles, pqlFiles);
				else {
					if (child.getName().endsWith(".qry"))
						queryFiles.add(child);
					else if (child.getName().endsWith(".pql"))
						pqlFiles.add(child);
				}
			}
		}
	}
}
