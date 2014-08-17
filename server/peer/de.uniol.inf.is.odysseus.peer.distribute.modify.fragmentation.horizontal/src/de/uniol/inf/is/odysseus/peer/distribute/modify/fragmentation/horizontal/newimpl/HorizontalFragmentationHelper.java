package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItem;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AggregateItemParameter;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * An fragmentation helper provides useful methods for fragmentation.
 * 
 * @author Michael Brand
 */
public class HorizontalFragmentationHelper extends AbstractFragmentationHelper {

	/**
	 * Changes the copies of a given aggregation to send partial aggregates.
	 * 
	 * @param originalPart
	 *            The query part to modify.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @param originalAggregation
	 *            The origin aggregation.
	 * @return The aggregation for merging the partial aggregates.
	 */
	public static AggregateAO changeAggregation(ILogicalQueryPart originalPart,
			AggregateAO originalAggregation, FragmentationInfoBundle bundle) {

		Preconditions
				.checkNotNull(originalPart, "Query part must be not null!");
		Preconditions.checkNotNull(originalAggregation,
				"Aggregate operator must be not null!");
		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");
		Preconditions.checkNotNull(bundle.getCopyMap(),
				"Copy map must be not null!");

		AggregateAO reunionAggregate = new AggregateAO();
		reunionAggregate.setGroupingAttributes(originalAggregation
				.getGroupingAttributes());
		reunionAggregate.setDumpAtValueCount(originalAggregation
				.getDumpAtValueCount());
		reunionAggregate.setDrainAtDone(originalAggregation.isDrainAtDone());
		reunionAggregate.setDrainAtClose(originalAggregation.isDrainAtClose());

		List<AggregateItem> aggItems = Lists.newArrayList();

		Collection<ILogicalOperator> copiedAggregations = LogicalQueryHelper
				.collectCopies(originalPart,
						bundle.getCopyMap().get(originalPart),
						originalAggregation);

		for (int copyNo = 0; copyNo < copiedAggregations.size(); copyNo++) {

			((AggregateAO) ((List<ILogicalOperator>) copiedAggregations)
					.get(copyNo)).setOutputPA(true);

			if (copyNo == 0) {

				for (SDFSchema inSchema : originalAggregation.getAggregations()
						.keySet()) {

					for (AggregateFunction function : originalAggregation
							.getAggregations().get(inSchema).keySet()) {

						SDFAttribute attr = originalAggregation
								.getAggregations().get(inSchema).get(function);
						aggItems.add(new AggregateItem(function.getName(),
								attr, attr));

					}

				}

			}

		}

		reunionAggregate.setAggregationItems(aggItems);

		// Creating PQL string of aggregation items
		StringBuffer pql = new StringBuffer();
		for (AggregateItem a : aggItems) {

			List<String> value = Lists.newArrayList(a.aggregateFunction
					.getName());
			if (a.inAttributes.size() == 1)
				value.add(a.inAttributes.get(0).getURI());
			else { // multiple attributes

				String attributes = "[";
				for (SDFAttribute inAttribute : a.inAttributes)
					attributes += "'" + inAttribute.getURI() + "',";
				attributes = attributes.substring(0, attributes.length() - 1);
				attributes += "]";

				value.add(attributes);

			}
			value.add(a.outAttribute.getURI());

			pql.append(AggregateItemParameter.getPQLString(value));
			pql.append(",");

		}
		String pqlString = "[" + pql.substring(0, pql.length() - 1) + "]";

		reunionAggregate.addParameterInfo(AggregateAO.AGGREGATIONS, pqlString);

		return reunionAggregate;

	}

	/**
	 * Creates a new fragmentation helper.
	 * 
	 * @param fragmentationParameters
	 *            The parameters for fragmentation.
	 */
	public HorizontalFragmentationHelper(List<String> fragmentationParameters) {

		super(fragmentationParameters);

	}

}