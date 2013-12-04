/*******************************************************************************
 * Copyright 2013 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.opcua.physicaloperator.access;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.Session;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseNextResponse;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.BrowseResultMask;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.transport.SecureChannel;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.AttributesUtil;

/**
 * @author DGeesen
 * 
 */
public class OPCClient {

	public static NodeId expandedNodeIdToNodeId(ExpandedNodeId en) {
		return NodeId.get(en.getIdType(), en.getNamespaceIndex(), en.getValue());
	}

	public static Map<String, NodeId> fetchKeyNames(SessionChannel sc, NodeId node, int depth) throws ServiceFaultException, ServiceResultException {
		String name = getAttributeValue(sc, node, Attributes.DisplayName);
		Map<String, NodeId> mappings = new TreeMap<>();
		if (depth > 0) {
			mappings.putAll(fetchKeyNames(sc, node, name, depth - 1));
		}
		mappings.put(name, node);
		// print(mappings);
		return mappings;
	}

	public static Map<String, NodeId> fetchKeyNames(SessionChannel sc, NodeId node, String parentKey, int depth) throws ServiceFaultException, ServiceResultException {
		if (!parentKey.isEmpty()) {
			parentKey = parentKey + ".";
		}
		Map<String, NodeId> mappings = new TreeMap<>();
		BrowseDescription nodeToBrowse1 = new BrowseDescription();

		nodeToBrowse1.setNodeId(node);
		nodeToBrowse1.setBrowseDirection(BrowseDirection.Forward);
		nodeToBrowse1.setReferenceTypeId(Identifiers.Aggregates);

		nodeToBrowse1.setIncludeSubtypes(true);
		nodeToBrowse1.setNodeClassMask(NodeClass.Object, NodeClass.Variable);
		nodeToBrowse1.setResultMask(BrowseResultMask.All);

		// find all nodes organized by the node.
		BrowseDescription nodeToBrowse2 = new BrowseDescription();

		nodeToBrowse2.setNodeId(node);
		nodeToBrowse2.setBrowseDirection(BrowseDirection.Forward);
		nodeToBrowse2.setReferenceTypeId(Identifiers.Organizes);
		nodeToBrowse2.setIncludeSubtypes(true);
		nodeToBrowse2.setNodeClassMask(NodeClass.Object, NodeClass.Variable);
		nodeToBrowse2.setResultMask(BrowseResultMask.All);

		BrowseDescription[] nodesToBrowse = { nodeToBrowse1, nodeToBrowse2 };

		// fetch references from the server.
		List<ReferenceDescription> references = browse(sc, nodesToBrowse);

		// process results.
		for (int ii = 0; ii < references.size(); ii++) {
			ReferenceDescription target = references.get(ii);
			NodeId subNode = expandedNodeIdToNodeId(target.getNodeId());

			String key = parentKey + target.getDisplayName().getText();
			// save only if it will have a value
			if (target.getNodeClass() == NodeClass.Variable) {
				mappings.put(key, subNode);
			}
			if (depth > 0) {
				mappings.putAll(fetchKeyNames(sc, subNode, key, depth - 1));
			}
		}

		return mappings;
	}

	public static void print(Map<String, ? extends Object> mappings) {
		for (Entry<String, ? extends Object> e : mappings.entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue().toString());
		}
	}

	public static Map<String, Object> fetchAttributeValues(SessionChannel sc, Map<String, NodeId> keyMappings) throws ServiceFaultException, ServiceResultException {
		Map<String, Object> values = new TreeMap<>();
		for (Entry<String, NodeId> entry : keyMappings.entrySet()) {
			Object value = fetchAttributeValues(sc, entry.getValue());
			values.put(entry.getKey(), value);
		}
		return values;
	}

	public static String getAttributeValue(SessionChannel sc, NodeId node, UnsignedInteger attributeID) throws ServiceFaultException, ServiceResultException {
		ReadRequest req = new ReadRequest();
		ReadValueId[] ids = { new ReadValueId(node, attributeID, null, null) };
		req.setNodesToRead(ids);
		req.setTimestampsToReturn(TimestampsToReturn.Both);
		ReadResponse res = sc.Read(req);
		if (res.getResults().length > 0) {
			return res.getResults()[0].getValue().toString();
		} else {
			return "";
		}
	}

	private static Map<String, Object> fetchAttributeValues(SessionChannel sc, NodeId node) throws ServiceFaultException, ServiceResultException {

		List<ReadValueId> nodesToRead = new ArrayList<>();

		// currently, we only want the value!

		ReadValueId nodeToRead = new ReadValueId();
		nodeToRead.setNodeId(node);
		nodeToRead.setAttributeId(Attributes.Value);
		nodesToRead.add(nodeToRead);

		// find all of the props of the node.
		BrowseDescription nodeToBrowse1 = new BrowseDescription();

		nodeToBrowse1.setNodeId(node);
		nodeToBrowse1.setBrowseDirection(BrowseDirection.Forward);
		nodeToBrowse1.setReferenceTypeId(Identifiers.HasProperty);
		nodeToBrowse1.setIncludeSubtypes(true);
		nodeToBrowse1.setNodeClassMask(NodeClass.NONE);
		nodeToBrowse1.setResultMask(BrowseResultMask.All);

		BrowseDescription[] nodesToBrowse = { nodeToBrowse1 };

		// fetch property references from the server.
		List<ReferenceDescription> references = browse(sc, nodesToBrowse);

		if (references.isEmpty()) {
			return new TreeMap<>();
		}
		for (ReferenceDescription ref : references) {
			if (ref.getNodeId().isAbsolute()) {
				continue;
			}
			nodeToRead = new ReadValueId();
			nodeToRead.setNodeId(expandedNodeIdToNodeId(ref.getNodeId()));
			nodeToRead.setAttributeId(Attributes.Value);
			nodesToRead.add(nodeToRead);
		}

		// read all values.

		ReadResponse rr = sc.Read(null, 0.0d, TimestampsToReturn.Both, nodesToRead.toArray(new ReadValueId[0]));
		Map<String, Object> values = new TreeMap<>();
		int i = 0;
		for (DataValue val : rr.getResults()) {
			if (!val.getStatusCode().isStatusCode(StatusCodes.Bad_AttributeIdInvalid)) {
				String name = AttributesUtil.toString(nodesToRead.get(i).getAttributeId());
				values.put(name, val.getValue().toString());
			}
			i++;
		}
		return values;
	}

	public static List<ReferenceDescription> browse(SessionChannel sc, BrowseDescription[] nodesToBrowse) throws ServiceFaultException, ServiceResultException {
		List<ReferenceDescription> references = new ArrayList<>();
		List<BrowseDescription> unprocessedOperations = new ArrayList<>();

		while (nodesToBrowse.length > 0) {

			BrowseResponse response = sc.Browse(null, null, UnsignedInteger.ZERO, nodesToBrowse);
			BrowseResult[] results = response.getResults();

			List<byte[]> continuationPoints = new ArrayList<>();

			for (int ii = 0; ii < nodesToBrowse.length; ii++) {
				// check for error.
				if (results[ii].getStatusCode().isBad()) {
					// this error indicates that the server does not have
					// enough simultaneously active
					// continuation points. This request will need to be
					// resent after the other operations
					// have been completed and their continuation points
					// released.
					if (results[ii].getStatusCode().isStatusCode(StatusCodes.Bad_NoContinuationPoints)) {
						unprocessedOperations.add(nodesToBrowse[ii]);
					}

					continue;
				}

				// check if all references have been fetched.
				if (results[ii].getReferences().length == 0) {
					continue;
				}

				// save results.
				references.addAll(Arrays.asList(results[ii].getReferences()));

				// check for continuation point.
				if (results[ii].getContinuationPoint() != null) {
					continuationPoints.add(results[ii].getContinuationPoint());
				}
			}

			// process continuation points.
			List<byte[]> revisedContiuationPoints = new ArrayList<>();

			while (continuationPoints.size() > 0) {
				// continue browse operation.

				BrowseNextResponse br = sc.BrowseNext(null, false, continuationPoints.toArray(new byte[0][]));

				// TODO: oder add all?!
				results = br.getResults();
				// ClientBase.ValidateResponse(results, continuationPoints);
				// ClientBase.ValidateDiagnosticInfos(diagnosticInfos,
				// continuationPoints);

				for (int ii = 0; ii < continuationPoints.size(); ii++) {
					// check for error.
					if (results[ii].getStatusCode().isBad()) {
						continue;
					}

					// check if all references have been fetched.
					if (results[ii].getReferences().length == 0) {
						continue;
					}

					// save results.
					references.addAll(Arrays.asList(results[ii].getReferences()));

					// check for continuation point.
					if (results[ii].getContinuationPoint() != null) {
						revisedContiuationPoints.add(results[ii].getContinuationPoint());
					}
				}

				// check if browsing must continue;
				revisedContiuationPoints = continuationPoints;
			}

			// check if unprocessed results exist.
			nodesToBrowse = unprocessedOperations.toArray(new BrowseDescription[0]);
		}

		// return complete list.
		return references;
	}

	public static void createConnnection(KeyPair keyPair, String endpoint, Cert serverCertificate) throws ServiceResultException, CertificateException, IOException {
		Application app = new Application();
		app.getHttpsSettings().setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		app.getHttpsSettings().setCertificateValidator(CertificateValidator.ALLOW_ALL);
		app.getHttpsSettings().setKeyPair(keyPair);
		app.addApplicationInstanceCertificate(keyPair);
		Client client = new Client(app);
		SecurityMode mode = SecurityMode.BASIC256_SIGN_ENCRYPT;

		SecureChannel channel = client.createSecureChannel(endpoint, endpoint, mode, serverCertificate);

		Session session = client.createSession(channel);
		SessionChannel sessionChannel = session.createSessionChannel(channel, client);
		sessionChannel.activate();
	}

	

}
