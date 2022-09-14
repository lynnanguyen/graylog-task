# Graylog Assignment
#### Lynna Nguyen - 2022
***
A small command line interface (CLI) tool that reads and parses messages
from a file with a format matching the attached sample-messages.txt file
and sends them to a running Graylog instance over HTTP using the
[GELF message specification](https://docs.graylog.org/docs/ingest-gelf).
***
### Usage
`java -jar graylog-task-lynna-nguyen.jar [arguments]`

`graylog-task-lynna-nguyen.jar` can be found at: https://github.com/lynnanguyen/graylog-task/blob/master/out/artifacts/graylog-task-lynna-nguyen.jar

See also instructions to run/debug application in Intellij below
### Arguments
| Name, Shorthand |                       Default                       | Description                                                                             |
|:---------------:|:---------------------------------------------------:|:----------------------------------------------------------------------------------------|
| `--help` , `-h` |                       `false`                       | Print help menu and exit. (`true` or `false`)                                           |
| `--file` , `-f` |      File path required if parameter specified      | File path to messages to send to the Graylog server.                                    |
| `--url` , `-u`  |            `http://127.0.0.1:12201/gelf`            | Graylog URL endpoint to send GELF message.                                              |
|      `-r`       |                       `false`                       | Flag to generate random GELF messages and send to the Graylog server.                   |
|      `-r`       |                       `false`                       | Flag to generate random GELF messages and send to the Graylog server.                   |
|      `-n`       | Default = `1` if `-r` is specified and `-n` is not. | Number of random GELF messages to generate and send to the Graylog server, must be > 0. |
| `--bulk` , `-b` |                       `false`                       | Flag to send GELF message in bulk or individually.                                      |

**NOTE**: If neither the `-f` flag or the `-r` flag is specified, one random message will be generated and sent to the Graylog server.

Example Commands:
* Run CLI to read and parse .txt file and send messages to the Graylog server individually:
    * `java -jar graylog-task-lynna-nguyen.jar --file /tmp/sample-messages.txt`
* Run CLI to read and parse .txt file and send messages to the Graylog server in bulk:
      * `java -jar graylog-task-lynna-nguyen.jar --file /tmp/sample-messages.txt --bulk`
* Run CLI to generate one random GELF message and send message to the Graylog server:
      * `java -jar graylog-task-lynna-nguyen.jar -r`
* Run CLI to generate 10 random GELF messages and send messages to the Graylog server in bulk:
      * `java -jar graylog-task-lynna-nguyen.jar -rn 10 --bulk`
* Run CLI to read and parse .txt file AND generate 10 random GELF messages and send messages to the Graylog server:
      * `java -jar graylog-task-lynna-nguyen.jar --file /tmp/sample-messages.txt -rn 10`
* Run CLI to view help menu
    * `java -jar graylog-task-lynna-nguyen.jar -h`

### Run/Debug Configuration for Running Application in Intellij
* Add new run configuration for `Application`
* Set to JDK 8
* Set the main class as `org.graylog.task.TaskApplication`
* Set the program arguments as desired from the table above


### Run/Debug Configuration for Unit Tests in Intellij
* Add new run configuration for `JUnit`
* Set to JDK 8
* Set the Type of Resource to Search for Tests dropdown to `All in package`
* Set `-ea` for VM options

***
### Logging
Logs to `./log/current.log` for the current day with a daily log rotation into
`./logs/graylog-task-logs_%d{yyyy-MM-dd}.log`.
