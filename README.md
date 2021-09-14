# simpl
SIMP language interpreter written in Java.
## Dependencies
```
Java 11
Maven 3
```
## Building from source
```bash
mvn package
```
## Running
```bash
java -jar target/simpl-1.0-SNAPSHOT.jar example.imp debug
```
## Usage
```bash
simpl debug || [source] (compile) || [source] (debug)
```
## Incompatibilities
This interpreter uses a modified SIMP grammar in order to simplify programming and makes one key change: the grammar for conditional branching changes from "if b then i1 else i2" to "if b then i1 else i2 end" and "while b do i" to "while b do i end". Notice the addition of "end" at the end of conditionals.
