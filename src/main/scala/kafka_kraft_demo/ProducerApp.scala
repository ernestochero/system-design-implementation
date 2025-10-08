package kafka_kraft_demo

import cats.effect._
import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties
import scala.concurrent.duration._

object ProducerApp extends IOApp.Simple {

  private def producerSettings: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props
  }

  private def sendMessage(
    producer: KafkaProducer[String, String],
    topic: String,
    key: String,
    value: String
  ): IO[Unit] = IO.blocking {
    producer.send(new ProducerRecord[String, String](topic, key, value))
    producer.flush() // Ensure messages are delivered immediately
    println(s"Sent message to topic: $topic, key: $key, value: $value")
  }

  def run: IO[Unit] = {
    val topic = "test-topic"
    Resource.make(
      IO(new KafkaProducer[String, String](producerSettings)) // acquire a producer
    )(p => IO(p.close())) // release a producer
      .use {
        producer =>
          for {
            _ <- IO.println(s"Producing to topic: $topic")
            _ <- sendMessage(producer, topic, "k1", "Hello Kafka KRaft").delayBy(1.second)
            _ <- sendMessage(producer, topic, "k2", "Distributed Systems FTW").delayBy(1.second)
          } yield ()
      }
  }
}
