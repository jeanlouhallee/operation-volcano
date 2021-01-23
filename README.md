# Operation volcano

Small project that allows booking with a REST api.

## Getting Started

These instructions will show you how to run the project locally.

### Prerequisites

It is recommanded to have docker installed in ordre to benefit from this installation guide.
https://docs.docker.com/get-docker/ 

## Running the project
To build and run the project
```
./gradlew bootBuildImage
docker run -p 8080:8080 operation-volcano:1.0.0
```