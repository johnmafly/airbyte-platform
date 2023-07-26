# A test describe in the README is available to test a version update
FROM temporal/auto-setup:1.13.0-relax-grpc

ENV TEMPORAL_HOME /etc/temporal

COPY scripts/update-and-start-temporal.sh update-and-start-temporal.sh

ENTRYPOINT ["./update-and-start-temporal.sh"]
