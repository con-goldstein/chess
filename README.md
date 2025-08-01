# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-8HLxBgAAKQgJKVh1YAhwpABqbh5hKW62payOS-QlliJQegXM+LgCTqgHACADkoCbDLd26yla-xvEmNy82vxT3nsvdexivKgXDvgrCJtfSYAACsF1N3nWrJq7kpWKwKRU3+cqDHiOMfLDNkVwFqqdfArVditHzwqta1xkoHXGu1YfWNfDs2kadTqy2vaYaZiiYjNYPqUYJMmkklJwaMnhqLb6EtVjPz5LjQh7RLIBlYiHehypLNLFLJw20pdiwNn8YcbtSqzjdXpTcTRmAZa+ORscfplUwmew13jU0UMHA4CmgRKmlT0gZNMyqdmrmF1zmfqvdAA+JnFiKaQQZozGmiN6pgGPa9NZzT1gjJ1TMOYhqsZGkWDjGMoshgtAVWLvHFrBfWsplAZmF61XZVWpokG0D1pmVW5tUF70fL7bbIaNxMCsz4zQjinsoBnvgNwPAJpsD4r3kpUlXDxJbvDs0SO0dY7x2MEfUrT6QB9exHnIDgnEAhlW9KrRlMVuzmc8W1zkCAGGA+Dtyz4WVsDNUGUtTmG-6nYKxoODonlp7a24PW7h3VWPegeRtCL20GjKfb1rbVXG01fU9uqhYSajWy+U18YLW+NAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[SequenceDiagram of Phase 2](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-8HLxBgAAKQgJKVh1YAhwpABqbh5hKW62payOS-QlliJQegXM+LgCTqgHACADkoCbDLd26yla-xvEmNy82vxT3nsvdexivKgXDvgrCJtfSYAACsF1N3nWrJq7kpWKwKRU3+cqDHiOMfLDNkVwFqqdfArVditHzwqta1xkoHXGu1YfWNfDs2kadTqy2vaYaZiiYjNYPqUYJMmkklJwaMnhqLb6EtVjPz5LjQh7RLIBlYiHehypLNLFLJw20pdiwNn8YcbtSqzjdXpTcTRmAZa+ORscfplUwmew13jU0UMHA4CmgRKmlT0gZNMyqdmrmF1zmfqvdAA+JnFiKaQQZozGmiN6pgGPa9NZzT1gjJ1TMOYhqsZGkWDjGMoshgtAVWLvHFrBfWsplAZmF61XZVWpokG0D1pmVW5tUF70fL7bbIaNxMCsz4zQjinsoBnvgNwPAJpsD4r3kpUlXDxJbvDs0SO0dY7x2MEfUrT6QB9exHnIDgnEAhlW9KrRlMVuzmc8W1zkCAGGA+Dtyz4WVsDNUGUtTmG-6nYKxoODonlp7a24PW7h3VWPegeR1B5nRlPs23gKrjaavqe3VQsJNRrZfKa+MG4QA)
