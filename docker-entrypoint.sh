#!/bin/sh
set -e

JAVA_OPTS="${JAVA_OPTS} -XX:MaxMetaspaceSize=256m"
JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom"
JAVA_OPTS="${JAVA_OPTS} -Dspring.backgroundpreinitializer.ignore=true"
JAVA_OPTS="${JAVA_OPTS} -Xlog:gc*=debug:file=/var/log/gc.log:time,level,tags"

# Set JVM options based on container memory limits
if [ -n "$JAVA_MAX_RAM_PERCENTAGE" ]; then
  JAVA_OPTS="${JAVA_OPTS} -XX:MaxRAMPercentage=${JAVA_MAX_RAM_PERCENTAGE}"
else
  JAVA_OPTS="${JAVA_OPTS} -XX:MaxRAMPercentage=75.0"
fi

exec java ${JAVA_OPTS} -Dspring.profiles.active=${PROFILE} -jar /inventory.jar
