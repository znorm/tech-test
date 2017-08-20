

# Prerequisites #

To compile the project using intellij, you will need to install the lombok plugin.
To build the project jar, you need maven installed.
To run the project, you need spark 2.10
https://spark.apache.org/downloads.html


# How to run #
Checkout the source.

Run `mvn clean install` to generate the jar file

Run `<your spark path>/bin/spark-submit  --master "local[*]" target/backend-test-1.0-SNAPSHOT.jar` to generate the output folder with the yearly outputs

