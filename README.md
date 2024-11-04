## 360T Coding Case

Having a Player class - an instance of this class with that can communicate with other Player(s) (other instances of this class)
The use case for this task is as bellow:
  1. create 2 players
  2. one of the players should send a message to second player (let's call this player "initiator")
  3. when a player receives a message should send back a new message that contains the received message concatenated with the message counter that this player sent.
  4. finalize the program (gracefully) after the initiator sent 10 messages and received back 10 messages (stop condition)
  5. have every player in a separate JAVA process (different PID).
  6. document for every class the responsibilities it has.

- Please use pure Java as much as possible (no additional frameworks like spring, etc.)
- Please deliver one single maven project with the source code only (no jars). Please send the maven project as archive attached to e-mail (eventual links for download will be ignored due to security policy).
- Please provide a shell script to start the program.
- Everything what is not clearly specified is to be decided by developer. Everything what is specified is a hard requirement.
- Please focus on design and not on technology, the technology should be the simplest possible that is achieving the target.
- The focus of the exercise is to deliver the cleanest and clearest design that you can achieve (and the system has to be functional).

## Prerequisites

Ensure you have Java and Maven installed:
- Java (JDK 11 or higher)
- Maven

## Running the Project
Note: Make sure the scripts have execute permissions. You can set them with:
```bash
chmod +x run_socket_main.sh
```
```bash
chmod +x run_thread_main.sh
```
- Based on one process
```bash
./run_thread_main.sh
```
- based on socket communication
```bash
./run_socket_main.sh
```
