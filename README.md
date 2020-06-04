# How to: Configure a Gradle project to run Integration tests after Unit tests

> This is still work in progress

## Summary

A Gradle project which runs `Integration tests` after `Unit tests` (only if they pass). Before running the `Integration tests`, `Gradle` brings up `Docker` containers using `Docker Compose`. After the `Integration tests` finish stop the `Docker` containers.

## Goals

Learn how to:

- Separate Unit tests from Integration tests to fail fast
- Leverage Docker Compose to bring up external dependencies during the `Integration test` phase
- Play with [WireMock](http://wiremock.org/)
