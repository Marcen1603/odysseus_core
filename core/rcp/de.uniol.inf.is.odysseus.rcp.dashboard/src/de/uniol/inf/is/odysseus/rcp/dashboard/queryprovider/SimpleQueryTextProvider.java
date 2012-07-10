package de.uniol.inf.is.odysseus.rcp.dashboard.queryprovider;

import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.dashboard.IDashboardPartQueryTextProvider;

public class SimpleQueryTextProvider implements IDashboardPartQueryTextProvider {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleQueryTextProvider.class);
	
	private final ImmutableList<String> queryTextLines;

	public SimpleQueryTextProvider(List<String> queryTextLines) {
		Preconditions.checkNotNull(queryTextLines, "QueryTextLines must not be null!");

		this.queryTextLines = ImmutableList.copyOf(queryTextLines);
	}
	
	public SimpleQueryTextProvider( IFile copyFrom ) {
		Preconditions.checkNotNull(copyFrom, "File for copying query text must not be null!");
		
		this.queryTextLines = copyQueryTextFromFile(copyFrom);
	}

	@Override
	public ImmutableList<String> getQueryText() {
		return queryTextLines;
	}

	private static ImmutableList<String> copyQueryTextFromFile(IFile file) {
		try {
			if (!file.isSynchronized(IResource.DEPTH_ZERO)) {
				file.refreshLocal(IResource.DEPTH_ZERO, null);
			}
			Scanner lineScanner = new Scanner(file.getContents());
	
			List<String> lines = Lists.newArrayList();
	
			while (lineScanner.hasNextLine()) {
				lines.add(lineScanner.nextLine());
			}
			
			return ImmutableList.copyOf(lines);
		} catch( Exception ex ) {
			LOG.error("Could not copy query text from file {}.", file.getName(), ex);
			return ImmutableList.of();
		}

	}
}
