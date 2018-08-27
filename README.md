
[![Build Status](https://travis-ci.org/Vantiv/ews-simulator.svg?branch=4.x)](https://travis-ci.org/Vantiv/ews-simulator) [![codecov](https://codecov.io/gh/Vantiv/ews-simulator/branch/4.x/graph/badge.svg)](https://codecov.io/gh/Vantiv/ews-simulator/branch/4.x)
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

## Configuration
Following are the command line options that can be used to start the simulator to respond accordingly.

| Argument Name   | Default value | Description                                                                                                                                                     | Type      | Sample value                        |
|-----------------|---------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------|-------------------------------------|
| port            | 443           | Port number to start the simulator on.                                                                                                                          | Optional  | 443                                 |
| key-store       |               | Fully qualified location to the key store file                                                                                                                  | Mandatory | /etc/path/to/key/store/keystore.jks |
| key-pass        |               | Password to the key store                                                                                                                                       | Mandatory | keyStorePassword                    |
| validate-header | false         | Determines if soap header needs to be validated in the incoming requests                                                                                        | Optional  | true/false                          |
| simulate-delay  | 0             | Amount of delay in ms to be simulated in response  | Option    | 5                                   |

## Deployment

To start the spring server for a given port use the below command:

```
java -jar -Dport=XXXX -Dkey-pass=<KeyPassword> -Dkey-store=<Location to keystore file> -Dvalidate-header=false -Dsimulate-delay=0 build/libs/worldpay-ews-simulator-X.X.X.jar
```

## Status

To get the current health of the server, use the below command:

```
curl -X GET localhost:port/actuator/health
```

## Simulator behavior customization
The simulator's response can be customized based on the below parameters in the request:
1. First three digits of merchant reference string invokes customization
* If first 2 characters are '00' then third character [0-9] can be used to simulate delay in seconds.
* The last three characters [0-9] can be used to simulate the 10 error codes e.g (001 to 010).
2. In case of invalid schema in request, it will respond with Invalid request mentioning the invalid field that has the error in the <requestValidationFault>.
3. In case of invalid header, when header validation is on, it will respond with Rejected by policy error.
4. Merchant-ref-id is optional, and is responded back with the same value as request.
5. If correlation_id is present in the request, same value will be present in the response, else a random value will be present.
6. PAN is calculated from the registration id by reversing the last four digits.
 ```
 E.g. 1234567890 => 1234560987
 ```
7. Token is calculated from the PAN by keeping the last four digits same and reversing the rest.
 ```
 E.g. 1234567890 => 6543217890
 ```
8. CVV is calculated from token by the last three digits
```
 E.g. 1234567890 => 890
```
9. Registration id is calculated from PAN by reversing the last four digits.
```
 E.g. 1234560987 => 1234567890
```
10. Registration id is calculated from token by reversing the last four digits and reversing the rest of the digits.
```
 E.g. 6543217890 => 1234560987
```
11. PAN is calculated from the token by keeping the last four digits same, and reversing the rest.
```
 E.g. 6543217890 => 1234567890
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

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
