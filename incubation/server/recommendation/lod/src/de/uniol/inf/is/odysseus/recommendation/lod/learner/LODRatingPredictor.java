package de.uniol.inf.is.odysseus.recommendation.lod.learner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.recommendation.lod.learner.LODRatingPredictor.UserItemRatingMap.ImmutableUserItemRatingMap;
import de.uniol.inf.is.odysseus.recommendation.model.rating_predictor.AbstractTupleBasedRatingPredictor;

/**
 * @author Christopher Schwarz
 */
public class LODRatingPredictor<T extends Tuple<M>, M extends IMetaAttribute, U, I, D> extends AbstractTupleBasedRatingPredictor<T, M, U, I, Double> {

	protected static Logger logger = LoggerFactory.getLogger(LODRatingPredictor.class);

	private final ImmutableUserItemRatingMap<U, I> userItemRatings;
	private final Map<I, D> additionalData;

	public LODRatingPredictor(final ImmutableUserItemRatingMap<U, I> userItemRatings, final ImmutableMap<I, D> additionalData) {
		this.userItemRatings = userItemRatings;
		this.additionalData = additionalData;
		
		
		System.err.println(toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Double predict(final U user, final I item) {
		
		//get data about the item
		D itemData = additionalData.get(item);
		Set itemDataSet;
		if(itemData instanceof Set) {
			itemDataSet = (Set)itemData;
		} else {
			itemDataSet = new HashSet();
			itemDataSet.add(itemData);
		}
		
		//get other items rated by user
		int weights = 0;
		double cumulatedRatings = 0;
		for(I ratedItem : userItemRatings.getRatedItems(user)) {
			double rating = userItemRatings.getRating(user, ratedItem);
			int weight = 0;

			//get data about the rated item
			D ratedItemData = additionalData.get(ratedItem);
			Set ratedItemDataSet;
			if(ratedItemData instanceof Set) {
				ratedItemDataSet = (Set)ratedItemData;
			} else {
				ratedItemDataSet = new HashSet();
				ratedItemDataSet.add(ratedItemData);
			}

			//increment the weight for this rating for every data-object the items have in common
			Iterator iterator = itemDataSet.iterator();
			while(iterator.hasNext()) {
				Object data = iterator.next();
				Iterator ratedIterator = ratedItemDataSet.iterator();
				while(ratedIterator.hasNext()) {
					if(data.equals(ratedIterator.next())) {
						weight++;
					}
				}
			}

			cumulatedRatings += (weight * rating);
			weights += weight;
		}
		
		//generate weighted mean
		return cumulatedRatings / weights;
	}

	@Override
	public String toString() {
		return "LODRatingPredictor [UIR-tuples: " + userItemRatings.userCount() + ", ID-tuples: " + additionalData.size() + "";
	}

	public static class UserItemRatingMap<U, I> {
		private HashMap<U, HashMap<I, Double>> ratings = new HashMap<U, HashMap<I, Double>>();
		
		public void addUserItemRating(U user, I item, double rating) {
			if(!ratings.containsKey(user)) {
				ratings.put(user, new HashMap<I, Double>());
			} 
			ratings.get(user).put(item, rating);
		}

		public Set<I> getRatedItems(U user) {
			return ratings.get(user).keySet();
		}

		public double getRating(U user, I item) {
			if(!ratings.containsKey(user)) return 0.0f;
			if(!ratings.get(user).containsKey(item)) return 0.0f;
			return ratings.get(user).get(item);
		}
		
		public int userCount() {
			return ratings.size();
		}

		public static class ImmutableUserItemRatingMap<U, I> {
			private HashMap<U, HashMap<I, Double>> immutableRatings;

			private ImmutableUserItemRatingMap(HashMap<U, HashMap<I, Double>> originalRatings) {
				immutableRatings = new HashMap<U, HashMap<I, Double>>();

				for(U user : originalRatings.keySet()) {
					HashMap<I, Double> userMap = new HashMap<I, Double>();
					for(I item : originalRatings.get(user).keySet()) {
						userMap.put(item, originalRatings.get(user).get(item));
					}
					immutableRatings.put(user, userMap);
				}
			}

			public static <U, I> ImmutableUserItemRatingMap<U, I> copyOf(UserItemRatingMap<U, I> map) {
				return new ImmutableUserItemRatingMap<U, I>(map.ratings);
			}

			public Set<I> getRatedItems(U user) {
				return ImmutableSet.copyOf(immutableRatings.get(user).keySet());
			}

			public double getRating(U user, I item) {
				if(!immutableRatings.containsKey(user)) return 0.0f;
				if(!immutableRatings.get(user).containsKey(item)) return 0.0f;
				return immutableRatings.get(user).get(item);
			}
			
			public int userCount() {
				return immutableRatings.size();
			}
		}
	}
}
