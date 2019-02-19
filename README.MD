CQRS & Event Sourcing solution using Akka persistence
=====================================================

This is an Domain Driven Design example project implementing CQRS pattern + Event sourcing architectural pattern.

## Solution main features

This solution offers:

- Applying Event Sourcing event management using `PersistentActor`
- Using Akka HTTP DSL to create endpoint routes 
- Applying CQRS concepts with commands

## Configuration

Check out the [SBT configuration file](./build.sbt) and [Akka configuration file](./src/main/resources/application.conf)

## How to run

Simply run `sbt run` and this will run a server running on port 8080 

## Curl command samples

You can query the solution REST API with [this commands](./curlExamples.txt) 
