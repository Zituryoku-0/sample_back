#!/usr/bin/env bash

set -Eeuo pipefail

SCHEMA_REPOSITORY="${SCHEMA_REPOSITORY:-Zituryoku-0/sample-api-schema}"
SCHEMA_VERSION="${SCHEMA_VERSION:-v1.0.0}"
OUTPUT_DIRECTORY="${OUTPUT_DIRECTORY:-openapi}"
OUTPUT_FILE="${OUTPUT_DIRECTORY}/openapi.yaml"

mkdir -p "${OUTPUT_DIRECTORY}"

echo "Downloading OpenAPI schema..."
echo "Repository: ${SCHEMA_REPOSITORY}"
echo "Version:    ${SCHEMA_VERSION}"

gh release download "${SCHEMA_VERSION}" \
  --repo "${SCHEMA_REPOSITORY}" \
  --pattern "openapi.yaml" \
  --output "${OUTPUT_FILE}" \
  --clobber

test -s "${OUTPUT_FILE}" || {
  echo "OpenAPI schema was not downloaded or is empty." >&2
  exit 1
}

echo "OpenAPI schema downloaded to ${OUTPUT_FILE}"