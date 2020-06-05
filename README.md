# How to: Configure a Gradle project to run Integration tests after Unit tests

> This is still work in progress

## Summary

A Gradle project which runs `Integration tests` after `Unit tests` (only if they pass). Before running the `Integration tests`, `Gradle` brings up `Docker` containers using `Docker Compose`. After the `Integration tests` finish stop the `Docker` containers.

## Goals

Learn how to:

- Separate Unit tests from Integration tests to fail fast
- Leverage Docker Compose to bring up external dependencies during the `Integration test` phase
- Play with [WireMock](http://wiremock.org/)

### Separate Unit tests from Integration tests

While most blog posts and articles are suggesting to use a separate `sourceSet`, this example
leverages a naming pattern for the test classes which should be run as part of the `Integration tests`.

The Gradle `test` task excludes all test classes with the following pattern `"**/*Integration*"`.

```kotlin
test {
    exclude("**/*Integration*")
}
```

A new task of type `Test` and with name `integrationTest` has been added. It uses the same
test classes directory and classpath as the `test` task. It specifically includes the test classes,
which have been excluded in the `test` task. It should run after the `test` task.

```kotlin
task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath

    include("**/*Integration*")
    shouldRunAfter("test")
}
```
This project uses JUnit5. Both tasks should use the Junit platform aka. JUnit5.

```kotlin
tasks.withType(Test::class.java) {
    useJUnitPlatform()
}
```
The last step is to include the `integrationTest` task as part of the `check` task.

```kotlin
check {
    dependsOn("integrationTest")
}
```

#### How to test that it works

In the `ExternalServiceConsumerTest` class just comment in the following line before running `./gradlew check`.

```kotlin
// Assertions.fail<Any>("Just because")
```
The `test` task fails and the build does not execute the `integrationTest` task. ✅
