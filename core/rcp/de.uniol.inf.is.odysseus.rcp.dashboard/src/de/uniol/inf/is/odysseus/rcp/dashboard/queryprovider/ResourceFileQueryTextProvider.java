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

public class ResourceFileQueryTextProvider implements IDashboardPartQueryTextProvider{

	private static final Logger LOG = LoggerFactory.getLogger(ResourceFileQueryTextProvider.class);
	
	private final IFile queryFile;
	
	public ResourceFileQueryTextProvider( IFile queryFile ) {
		this.queryFile = Preconditions.checkNotNull(queryFile, "QueryFile must not be null!");
	}
	
	@Override
	public ImmutableList<String> getQueryText() {
		try {
			if (!queryFile.isSynchronized(IResource.DEPTH_ZERO)) {
				queryFile.refreshLocal(IResource.DEPTH_ZERO, null);
			}
			Scanner lineScanner = new Scanner(queryFile.getContents());
	
			List<String> lines = Lists.newArrayList();

			while (lineScanner.hasNextLine()) {
				lines.add(lineScanner.nextLine());
			}
			
			return ImmutableList.copyOf(lines);
		} catch( Exception ex ) {
			LOG.error("Could not get query text from file {}.", queryFile.getName(), ex);
			return ImmutableList.of();
		}
	}
	
	public IFile getFile() {
		return queryFile;
	}

}
