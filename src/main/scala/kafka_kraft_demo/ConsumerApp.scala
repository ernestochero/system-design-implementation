package kafka_kraft_demo

import cats.effect._
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters._

object ConsumerApp extends IOApp.Simple {

  def consumerSettings: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group1") // Changed group id to avoid offset issues
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props
  }

  def consumeMessages(consumer: KafkaConsumer[String, String], topic: String): IO[Unit] =
    IO.blocking {
      consumer.subscribe(java.util.Collections.singletonList(topic))
      println(s"Subscribed to topic: $topic")
      while (true) {
        val records = consumer.poll(Duration.ofMillis(500)).asScala
        records.foreach { record =>
          println(s"Consumed -> key=${record.key()}, value=${record.value()}, partition=${record.partition()}")
        }
      }
    }

  def run: IO[Unit] = {
    Resource.make(IO(new KafkaConsumer[String, String](consumerSettings)))(consumer => IO(consumer.close()))
      .use { consumer =>
      val topic = "test-topic"
      IO.println(s"Consuming from topic: $topic") *> consumeMessages(consumer, topic)
    }
  }




}
