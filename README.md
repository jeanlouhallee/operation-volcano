# Operation volcano

Small project that allows booking with a REST api.

## Getting Started

These instructions will show you how to run the project locally.

### Prerequisites

It is recommanded to have docker installed in ordre to benefit from this installation guide.
https://docs.docker.com/get-docker/

This Project uses Lombok and Mapstruct to generate boilerplate code.
https://projectlombok.org/
https://mapstruct.org/

This documentation assumes that you are confortable with your own IDE to setup and run this project.
This was developped using Intellij Community IDE.

## Running the project

To build and run the project from the command line.

```
./gradlew bootBuildImage
docker-compose up -d
```

To attach to your microservice container and inspect logs, run:

```
docker logs -f operation-volcano
```

All good and ready!
The microservice will be available at http://localhost:8080

Note: If running your application in the IDE without docker, you need to set these environment variables to connect to your database:
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD

## Testing

If you wish to run all tests in an isolated environment (like in a CI server, for example), you may proceed as follows:

```
docker run -it --rm -v $PWD:$PWD -w $PWD -v /var/run/docker.sock:/var/run/docker.sock gradle:6.7.1-jdk11 gradle clean build
```

### Concurrency

To test the concurrency issue on reservations, launch your microservice and database with docker-compose and run the following script:

```
sh ./concurrencyTest.sh
```

### Performance

To test the application resistance to heavy load, you can proceed as follows:

``` 
docker run -it --rm --network operation-volcano_default alpine/bombardier -d 30s -c 100 -l http://operation-volcano:8080/booking/v1/availabilities
```

After the execution, you should see a result that looks similar:

``` 
Done!
Statistics        Avg      Stdev        Max
  Reqs/sec       461.42     172.99    1154.09
  Latency      215.90ms    89.48ms   764.10ms
  Latency Distribution
     50%   207.21ms
     75%   264.82ms
     90%   329.30ms
     95%   373.62ms
     99%   472.67ms
  HTTP codes:
    1xx - 0, 2xx - 13928, 3xx - 0, 4xx - 0, 5xx - 0
    others - 0
  Throughput:   264.39KB/s
```

## API Documentation

THe documentation for the API is located at defautl swagger-ui locationé
For exemple, locally http://localhost:8080/swagger-ui.html

## CI/CD

No pipeline has been setup yet for this project.


