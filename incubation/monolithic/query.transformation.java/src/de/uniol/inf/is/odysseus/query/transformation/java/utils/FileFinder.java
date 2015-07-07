package de.uniol.inf.is.odysseus.query.transformation.java.utils;
import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileFinder extends SimpleFileVisitor<Path> {

	private final PathMatcher matcher;
	private List<Path> matchedPaths = new ArrayList<Path>();
	
	public FileFinder(String pattern) {
		matcher = FileSystems.getDefault()
				.getPathMatcher("glob:" + pattern);
	}


	void match(Path file) {
		Path name = file.getFileName();

		if (name != null && matcher.matches(name)) {
			matchedPaths.add(file);
		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
		match(file);
		return CONTINUE;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir,
			BasicFileAttributes attrs) {
		match(dir);
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) {
		System.err.println(exc);
		return CONTINUE;
	}

	public int getTotalMatches() {
		return matchedPaths.size();
	}

	public Collection<Path> getMatchedPaths() {
		return matchedPaths;
	}

} 
