# A Kotlin & Gradle project which runs Integration tests only after Unit tests passed

## Summary

A Gradle project which runs `Integration tests` after `Unit tests` (only if they pass). Before running the `Integration tests`, `Gradle` brings up `Docker` containers using `Docker Compose`. After the `Integration tests` finish stop the `Docker` containers.

## Goals

Learn how to:

- Separate Unit tests from Integration tests to fail fast
- Use [WireMock](http://wiremock.org/) during `Integration tests`
- Leverage Docker Compose to bring up external dependencies during the `Integration test` phase

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

### Use WireMock for during Integration tests

WireMock is a mock API server. There are different ways to use it and to define the mock endpoints and responses. Since we want to bring up the mock server with `Docker Compose
`, I define the mocks in JSON files. See the `wiremock` folder and in there the `sample-mock-endpoint.json`.

In the `docker-compose.yml` I use a WireMock Docker image and map the `wiremock` folder
to the container.

After you run `docker-compose up`, you can request `GET http://localhost:9999/some/thing`. ✅

### Bring up external dependencies during Integration tests

I use a [Gradle plugin](https://github.com/avast/gradle-docker-compose-plugin) to integrate Docker Compose into the Gradle build. The `integrationTest` task has been modified to
 run `docker-compose up` before and `docker-compose down` after the tests.
 
```kotlin
task<Test>("integrationTest") {
    description = "Runs integration tests."
    ...
    dependsOn("composeUp")
    finalizedBy("composeDown")
}
``` 
See the logs of the [Build and Test](https://github.com/abedurftig/gradle-integration-tests/runs/743499035?check_suite_focus=true) step of the related GitHub Actions workflow. ✅

## Final thoughts

It was a lot easier than I thought. WireMock seems like a great tool
which can help to simulate different API scenarios. Even those, which might be rather rare. Thinking of long response times for example. Using the JSON format and running
WireMock in a Docker container makes the setup fairly straight forward.

Plus obviously we can bring up more than just the API dependency. Also databases like Postgres or Redis for example.
 
I don't see the benefit of using multiple `sourceSets`. Filtering the tests by matching a pattern in the classname seems like an easy approach.
 
For this example I have chosen a multi-module setup (with a single module) and the `BuildSrc` folder, so there are a few open questions.

- How would it work if I had `Integration tests` in more than one module? Would the WireMock container have to go up and down multiple times?
- What about test coverage reports? How does that work when I have multiple tests tasks? I already noticed in a different project, that Jacoco together with `Console Reporter
` plugin don't work reliably.
