# simpl
SIMP language interpreter written in Java.
## Dependencies
```
Java 16
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
### Default CLI
Statement by statement evaluation with persistent execution environment.
```bash
simpl
```
### Debug CLI
Enables debugging information on each statement, such as token stream and program data from AST.
```bash
simpl debug
```
### Debug file
Prints debugging information during interpreting, such as token stream and program data from AST.
```bash
simpl [file] debug
```
### Compile file (experimental)
Converts a .imp file into a .c
```bash
simpl [file] compile
```
