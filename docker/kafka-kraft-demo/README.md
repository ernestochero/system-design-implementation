# Kafka KRaft Demo

Minimal example of a single-node Kafka broker running in KRaft mode via Docker Compose.

## Requirements

- Docker and Docker Compose installed.

## Getting Started

```bash
cd docker/kafka-kraft-demo
docker compose up -d
```

The broker is exposed on `localhost:9092`.

## Quick Tests

Produce a message:

```bash
docker exec -it kafka_kraft /opt/kafka/bin/kafka-console-producer.sh \
  --broker-list localhost:9092 --topic test-topic
```

Consume from the beginning:

```bash
docker exec -it kafka_kraft /opt/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

## Notes

- `KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR` and related variables are set in `docker-compose.yml` so internal topics can be created with a single broker.
- Log data lives inside the container under `/tmp/kraft-combined-logs`; remove that directory if you need a clean slate.
