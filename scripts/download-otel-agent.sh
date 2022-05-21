#!/usr/bin/env bash

PROJECT_ROOT=$(git rev-parse --show-toplevel)

source "${PROJECT_ROOT}"/gradle.properties

if [ -z  "${otelVersion}" ]; then
    echo "OpenTelemetry version not set!"
    exit 1
fi

echo "OpenTelemetry SDK version: ${otelVersion}"

AGENT_DIR=~/otel/agents
if [ ! -d ${AGENT_DIR} ]; then
    mkdir -p "${AGENT_DIR}"
fi

AGENT_FILE_NAME=opentelemetry-javaagent.jar
AGENT_VERSIONED_FILE_NAME=opentelemetry-javaagent-${otelVersion}.jar;
CANONICAL_VERSION=v${otelVersion}
ABSOLUTE_AGENT_PATH=$(dirname ${AGENT_DIR}/"${AGENT_VERSIONED_FILE_NAME}")/${AGENT_VERSIONED_FILE_NAME}



GITHUB_BASE_URL=https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download
RELEASE_ASSET="${CANONICAL_VERSION}/${AGENT_FILE_NAME}"

if [ -f ${AGENT_DIR}/${AGENT_FILE_NAME} ]; then
    echo "Deleting non-versioned agent file: ${AGENT_DIR}/${AGENT_FILE_NAME}"
    delete ${AGENT_DIR}/${AGENT_FILE_NAME}
fi

if [ ! -f ${AGENT_DIR}/"${AGENT_VERSIONED_FILE_NAME}" ]; then
  echo "About to download agent at ${GITHUB_BASE_URL}/${RELEASE_ASSET}"
  wget --no-check-certificate -P ${AGENT_DIR} "${GITHUB_BASE_URL}/${RELEASE_ASSET}"
  mv ${AGENT_DIR}/${AGENT_FILE_NAME} ${AGENT_DIR}/${AGENT_VERSIONED_FILE_NAME}
fi


echo "OpenTelemetry ${CANONICAL_VERSION} Java agent is located at ${ABSOLUTE_AGENT_PATH}"
echo "Set the java agent through environment variable (JAVA_TOOL_OPTIONS) or command line arguments"
echo "Environment variable: export JAVA_TOOL_OPTIONS=-javaagent:${ABSOLUTE_AGENT_PATH}"
echo "CLI argument: java -javaagent:${ABSOLUTE_AGENT_PATH} -jar myapp.jar"