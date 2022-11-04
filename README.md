# CI-Info for Kotlin

[![License](https://img.shields.io/badge/MIT-green.svg)](https://github.com/cloudflightio/ci-info/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/io.cloudflight.ci.info/ci-info.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.cloudflight.ci.info/ci-info)

This is a Kotlin-Port of https://github.com/watson/ci-info, it provides a 
singleton `io.cloudflight.ci.info.CI` which gives you information about the current
Continuous Integration Environment. It also comes with an `CIServer` enum class with all
known servers.

# Requirements

* Java 11
* Kotlin 1.7 (will be pulled automatically)

# Usage

If you use gradle, add the following dependency:

````groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "io.cloudflight.ci.info:ci-info:1.0.0"
}
````

Then just use the properties of the object `io.cloudflight.ci.info.CI`:

````kotlin
// on Jenkins:
val ciServer = CI.server // will return CIServer.JENKINS if the code is executed on Jenkins
val isPR = CI.isPR       // will return true if the server is currently building a pull request
````

See the class [CIServer](src/main/kotlin/io/cloudflight/ci/info/CIServer.kt) for a list of all known
CI Servers.

# Development

If you wanna participate in development, feel free to submit a PR.

The packaged `vendors.json` is a 1:1 copy of https://raw.githubusercontent.com/watson/ci-info/master/vendors.json. If
you want to update / sync that file, also once run the `CIServerEnumGenerator.kt`, it will re-generate
the `CIServer` list.