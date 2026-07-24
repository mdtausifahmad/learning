# Local observability lab

A minimal stack to instrument your own service and watch its telemetry flow:

```
your app ──OTLP──▶ OpenTelemetry Collector ──▶ Prometheus (metrics)
                                           └──▶ Jaeger    (traces)
                                                   Grafana (dashboards over Prometheus)
```

## Run

```sh
docker compose up -d      # start
docker compose down       # stop
```

## Open

| UI | URL |
|----|-----|
| Prometheus (PromQL) | http://localhost:9090 |
| Grafana (dashboards)| http://localhost:3000 |
| Jaeger (traces)     | http://localhost:16686 |

## Point your service at the Collector

Add OpenTelemetry (zero-code auto-instrumentation is fastest) and set:

```sh
export OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318
export OTEL_SERVICE_NAME=my-service
```

Generate some traffic, then query your golden signals in Prometheus and follow a
request in Jaeger. See Lesson 08 for the full walkthrough.

> Images use `:latest` for convenience — pin versions (e.g. the Collector to a
> specific release) for anything real.
