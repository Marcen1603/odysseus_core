package de.uniol.inf.is.odysseus.wrapper.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * @author Dennis
 *
 */
public class KafkaPartitioner implements Partitioner {

  public KafkaPartitioner(VerifiableProperties props) {

  }

  /*
   * (non-Javadoc)
   * 
   * @see kafka.producer.Partitioner#partition(java.lang.Object, int)
   */
  @Override
  public int partition(Object arg0, int arg1) {
    int sensorId = Integer.parseInt((String) arg0);
    int partition = sensorId % arg1;
    return partition;
  }

}
