# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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

[SequenceDiagram of Phase 2](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-GgpZ36YCIbQJ2zZLbe1QQ-dxmGk6ho3EwKzSTNCOKeygPe+A3A8AmmwDSveSkmVcPEse8OzRI7R1jvHYwR8RUtsnGN7EecoMycQCGXbGrM2Ux27ODTTMOl-wAYYD4J3FNnaO7M1QLTfPXcgbdyrGh5P1JgCAHbg9XuXdrR9qBqQmNoR+zVzbdXDt4Ca92lrxmT1UP9aNsFXXxg9ck0AA)
