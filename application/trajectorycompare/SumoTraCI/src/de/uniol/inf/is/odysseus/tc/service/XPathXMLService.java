/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@23956495
 * Copyright (c) ${year}, ${owner}, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package de.uniol.inf.is.odysseus.tc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by marcus on 03.12.14.
 */
public class XPathXMLService implements IXMLService {
	@SuppressWarnings("unused")
    private final static Logger LOGGER = LoggerFactory.getLogger(XPathXMLService.class);

    @Override
    public String getValue(String file, String path, String attr) {

        try {
            final InputStream is = new FileInputStream(file);
            final Document doc =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            is.close();
            final NamedNodeMap result =
                    ((Node) XPathFactory.newInstance().newXPath().compile(path)
                            .evaluate(doc, XPathConstants.NODE)).getAttributes();

            return result.getNamedItem(attr).getNodeValue();
        } catch (Exception e) {

        }
        return null;
    }
}
