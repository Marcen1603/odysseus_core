package de.uniol.inf.is.odysseus.server.opcua.doc;

import static de.uniol.inf.is.odysseus.server.opcua.util.GraphUtil.toAttributeStr;
import static de.uniol.inf.is.odysseus.server.opcua.util.GraphUtil.toStr;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.structured.ReferenceNode;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

import de.uniol.inf.is.odysseus.server.opcua.doc.Customizers.IEdgeCustomizer;
import de.uniol.inf.is.odysseus.server.opcua.doc.Customizers.INodeCustomizer;

public class GraphBuilder {

	public static GraphBuilder create() {
		return new GraphBuilder();
	}

	protected BrowseDirection dir;
	protected GraphKind kind;
	protected String ending;
	protected String encoding;
	protected INodeModel model;
	protected NodeId entry;
	protected Collection<String> lines;
	protected String title;
	protected Layout layout;
	protected File dot;
	protected Format format;
	protected INodeCustomizer noder;
	protected IEdgeCustomizer edger;

	protected GraphBuilder() {
		dir = BrowseDirection.Both;
		kind = GraphKind.Directed;
		ending = System.getProperty("line.separator");
		encoding = "UTF8";
		model = mock(INodeModel.class);
		entry = Identifiers.RootFolder;
		lines = new LinkedHashSet<>();
		title = "opcua";
		layout = Layout.fdp;
		dot = new File("model.dot");
		format = Format.svg;
		noder = Customizers::defNoder;
		edger = Customizers::defEdger;
	}

	public BrowseDirection getDir() {
		return dir;
	}

	public GraphKind getKind() {
		return kind;
	}

	public String getEnding() {
		return ending;
	}

	public String getEncoding() {
		return encoding;
	}

	public INodeModel getModel() {
		return model;
	}

	public NodeId getEntry() {
		return entry;
	}

	public Collection<String> getLines() {
		return lines;
	}

	public String getTitle() {
		return title;
	}

	public Layout getLayout() {
		return layout;
	}

	public File getDot() {
		return dot;
	}

	public Format getFormat() {
		return format;
	}

	public INodeCustomizer getNoder() {
		return noder;
	}

	public IEdgeCustomizer getEdger() {
		return edger;
	}

	public GraphBuilder dir(BrowseDirection dir) {
		this.dir = dir;
		return this;
	}

	public GraphBuilder kind(GraphKind kind) {
		this.kind = kind;
		return this;
	}

	public GraphBuilder ending(String ending) {
		this.ending = ending;
		return this;
	}

	public GraphBuilder encoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public GraphBuilder model(INodeModel model) {
		this.model = model;
		return this;
	}

	public GraphBuilder entry(NodeId entry) {
		this.entry = entry;
		return this;
	}

	public GraphBuilder lines(Collection<String> lines) {
		this.lines = lines;
		return this;
	}

	public GraphBuilder title(String title) {
		this.title = title;
		return this;
	}

	public GraphBuilder layout(Layout layout) {
		this.layout = layout;
		return this;
	}

	public GraphBuilder dot(File dot) {
		this.dot = dot;
		return this;
	}

	public GraphBuilder format(Format format) {
		this.format = format;
		return this;
	}

	public GraphBuilder noder(INodeCustomizer noder) {
		this.noder = noder;
		return this;
	}

	public GraphBuilder edger(IEdgeCustomizer edger) {
		this.edger = edger;
		return this;
	}

	public GraphBuilder clean() {
		this.lines.clear();
		return this;
	}

	public GraphBuilder browse() {
		String prefix = kind == GraphKind.Directed ? "digraph" : "graph";
		lines.add(prefix + " " + title + " {");
		lines.add('\t' + "overlap = false;");
		browse(entry);
		lines.add("}");
		return this;
	}

	protected void browse(NodeId id) {
		if (dir == BrowseDirection.Both || dir == BrowseDirection.Forward)
			for (ReferenceNode forward : model.browse(id, BrowseDirection.Forward))
				browse(id, forward, BrowseDirection.Forward);
		if (dir == BrowseDirection.Both || dir == BrowseDirection.Inverse)
			for (ReferenceNode backward : model.browse(id, BrowseDirection.Inverse))
				browse(id, backward, BrowseDirection.Inverse);
	}

	protected void browse(NodeId id, ReferenceNode refNode, BrowseDirection bd) {
		NodeId src = id;
		NodeId ref = refNode.getReferenceTypeId();
		if (ref == null || ref.equals(Identifiers.HasTypeDefinition))
			return;
		ref = ref == null ? Identifiers.HasModellingRule : ref;
		NodeId dest = refNode.getTargetId().local().get();
		lines.add('\t' + toStr(src) + " " + toAttributeStr(noder.customize(model, src)) + ";");
		boolean isNew = lines.add('\t' + toStr(dest) + " " + toAttributeStr(noder.customize(model, dest)) + ";");
		String arrow = kind == GraphKind.Directed ? " -> " : " -- ";
		lines.add('\t' + toStr(src) + arrow + toStr(dest) + " " + toAttributeStr(edger.customize(ref, bd)) + ";");
		// Recursive call
		if (isNew)
			browse(dest);
	}

	public GraphBuilder write() throws IOException {
		write(dot);
		return this;
	}

	protected void write(File file) throws IOException {
		try (OutputStream out = new FileOutputStream(file)) {
			IOUtils.writeLines(lines, ending, out, encoding);
		}
	}

	public GraphBuilder graph() throws IOException, InterruptedException {
		List<String> args = new ArrayList<>();
		args.add("dot");
		args.add("-K" + layout);
		args.add("-T" + format);
		args.add("-Goverlap=prism");
		args.add("-O");
		args.add("-v");
		args.add(dot.getAbsolutePath());
		Process proc = (new ProcessBuilder(args)).inheritIO().start();
		int ret;
		if ((ret = proc.waitFor()) != 0)
			throw new UnsupportedOperationException("Error code => " + ret + "!");
		return this;
	}
}