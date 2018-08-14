
[![Build Status](https://travis-ci.org/Vantiv/ews-simulator.svg?branch=master)](https://travis-ci.org/Vantiv/ews-simulator) [![codecov](https://codecov.io/gh/Vantiv/ews-simulator/branch/master/graph/badge.svg)](https://codecov.io/gh/Vantiv/ews-simulator)
 [![GitHub](https://img.shields.io/github/license/vantiv/ews-simulator.svg)](https://github.com/Vantiv/ews-simulator/blob/master/LICENSE) [![GitHub issues](https://img.shields.io/github/issues/vantiv/ews-simulator.svg)](https://github.com/Vantiv/ews-simulator/issues) ![Github All Releases](https://img.shields.io/github/downloads/vantiv/ews-simulator/total.svg)


# Encryption Web Services Simulator

Encryption Web Services (EWS) provides a SOAP-based web service for Vantiv’s Encryption Services. This includes primary support for OmniToken and in some case, legacy support for Reverse Crypto. Operations that support Reverse Crypto will be noted. A caveat to this support is that the service assumes that Reverse Crypto at Vantiv will be retired prior to the next key rotation; hence supported operations will not return the token Id at this time.

This document details the use of Vantiv’s Encryption Web Services and its message structure. The intended audience is developers who want to consume Vantiv’s Encryption Web Services and have received the on-boarding materials that are associated with this document.
In the Message Structure section, the entire message is broken down layer by layer and is meant as a reference. Many of the tags seen in that section are not meant to be used together, but rather describe each tag and its potential inner tags and attributes. This is a great place to find the meaning of a tag and how to format the contents properly.

The Web Service Operations section calls out each operation and relevant details about the unique fields. 
An important note about the order of tags in a message: order matters!  Misordering tags will result in a request that is invalid which will generate a fault response.


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Gradle v4.0 and above. To install, follow the steps mentioned at [Gradle | Installation](https://gradle.org/install/)


### Installing

1. Get the latest code from the repo using the command below:

```
git clone https://github.com/Vantiv/ews-simulator.git
```

2. Run the gradle build:

```
gradle build
```

3. The gradle build should create the Spring boot jar as below:

```
build/libs/worldpay-ews-simulator-X.X.X.jar
```


## Running the tests

Tests are run as part of the build steps above, but to run them manually you can run the command:

```
gradle test
```

## Deployment

To start the spring server for a given port use the below command:

```
java -jar -Dserver.port=XXXX build/libs/worldpay-ews-simulator-X.X.X.jar
```

## Shutdown

To stop the server, use the below command:

```
curl -X POST localhost:port/actuator/shutdown 
```

## Status

To get the current health of the server, use the below command:

```
curl -X GET localhost:port/actuator/health
```

## Built With

* [Gradle](https://gradle.org/) - The build tool
* [Maven](https://maven.apache.org/) - Dependency Management

## Versioning

For the versions available, see the [tags on this repository](https://github.com/vantiv/ews-simulator/tags). 

## Authors

* [**Ajjunesh Raju**](https://github.com/Ajjunesh)
* [**Charmik Sheth**](https://github.com/Charmik-Sheth)
* [**Chen Chang**](https://github.com/cc6980312)
* [**Kartik Dave**](https://github.com/davekartik24)

See also the list of [contributors](https://github.com/vantiv/ews-simulator/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
