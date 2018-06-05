/*******************************************************************************
 * Copyright 2016 Georg Berendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.xafero.turjumaan.server.rdf;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 * The utilities for handling RDF.
 */
public final class RdfUtils {

	/**
	 * Instantiates a new RDF utility.
	 */
	private RdfUtils() {
	}

	/**
	 * Creates the default model.
	 *
	 * @return the model
	 */
	public static Model createModel() {
		return ModelFactory.createDefaultModel();
	}

	/**
	 * Load the model from file.
	 *
	 * @param file
	 *            the file
	 * @return the model
	 */
	public static Model load(File file) {
		return FileManager.get().loadModel(file.toString());
	}

	/**
	 * Load the model from stream.
	 *
	 * @param in
	 *            the input stream
	 * @param baseURI
	 *            the base URI
	 * @param syntax
	 *            the syntax
	 * @return the model
	 */
	public static Model load(InputStream in, String baseURI, String syntax) {
		return load(createModel(), in, baseURI, syntax);
	}

	/**
	 * Load the model from reader.
	 *
	 * @param in
	 *            the reader
	 * @param baseURI
	 *            the base URI
	 * @param syntax
	 *            the syntax
	 * @return the model
	 */
	public static Model load(Reader in, String baseURI, String syntax) {
		return load(createModel(), in, baseURI, syntax);
	}

	/**
	 * Load the model from file.
	 *
	 * @param model
	 *            the model to fill
	 * @param in
	 *            the input file
	 * @param baseURI
	 *            the base URI
	 * @param syntax
	 *            the syntax
	 * @return the model
	 */
	public static Model load(Model model, File in, String baseURI, String syntax) {
		return load(model, toStream(in), baseURI, syntax);
	}

	/**
	 * Load the model from stream.
	 *
	 * @param model
	 *            the model to fill
	 * @param in
	 *            the input stream
	 * @param baseURI
	 *            the base URI
	 * @param syntax
	 *            the syntax
	 * @return the model
	 */
	public static Model load(Model model, InputStream in, String baseURI, String syntax) {
		model.read(in, baseURI, syntax);
		closeQuietly(in);
		return model;
	}

	/**
	 * Load the model from reader.
	 *
	 * @param model
	 *            the model to fill
	 * @param in
	 *            the reader
	 * @param baseURI
	 *            the base URI
	 * @param syntax
	 *            the syntax
	 * @return the model
	 */
	public static Model load(Model model, Reader in, String baseURI, String syntax) {
		model.read(in, baseURI, syntax);
		closeQuietly(in);
		return model;
	}

	/**
	 * Close quietly.
	 *
	 * @param closeable
	 *            the closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable == null)
			return;
		try {
			if (closeable instanceof Flushable)
				((Flushable) closeable).flush();
			closeable.close();
		} catch (IOException ioe) {
		}
	}

	/**
	 * Create stream from file.
	 *
	 * @param file
	 *            the file
	 * @return the file input stream
	 */
	public static FileInputStream toStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}