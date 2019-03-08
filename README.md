# Skema

![java](https://img.shields.io/badge/java-8-green.svg) ![java](https://img.shields.io/badge/java-11-green.svg) [![Build Status](https://travis-ci.org/hhu-bsinfo/skema.svg?branch=development)](https://travis-ci.org/hhu-bsinfo/skema)

A fast and simple Java object serialization library based on Ahead-of-Time schema generation.

## Getting Started

This project hast been tested with the

 * Java SE Development Kit 8 [[Download](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)]

Other versions may work but are __not__ supported officially.

### Features

 * On-the-fly schema generation for unregistered classes
 * [Random instance initialization](skema/src/test/java/de/hhu/bsinfo/skema/random/ObjectGeneratorTest.java)
 * [Partial serialization and deserialization](skema/src/test/java/de/hhu/bsinfo/skema/SerializerTest.java)
 * Optional off-heap memory usage
 * No implementation needed
 * Simple API

### Usage

```java
// Enable auto registration
Skema.enableAutoRegistration();

// Create an instance of your object
MyObject original = new MyObject();

// Serialize the object using Skema
byte[] buffer = Skema.serialize(original);

// Create a new instance of your object using the serialized data
MyObject copy = Skema.deserialize(MyObject.class, buffer);

```

### Download

```groovy
repositories {
    maven {
        url "https://dl.bintray.com/hhu-bsinfo/dxram"
    }
}

dependencies {
    implementation 'de.hhu.bsinfo:skema:1.0.0'
}

```

## Running the tests

Since this project uses the Gradle build system running all included tests is possible using the following command.

```
./gradlew skema:test
```

### Running the benchmark

This project includes a set of [JMH](https://openjdk.java.net/projects/code-tools/jmh/) benchmarks which can be executed using the following command.

```
./benchmark.sh
```

## Built With

* [Gradle](https://gradle.org/) - Build System

## Authors

* **Filip Krakowski** [[filkra](https://github.com/filkra)]

See also the list of [contributors](https://github.com/filkra/skema/contributors) who participated in this project.

## License

This project is licensed under the GNU GPLv3 License - see the [LICENSE.md](LICENSE.md) file for details.
