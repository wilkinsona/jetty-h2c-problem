This repository illustrates a problem with the h2c support in Jetty 10 and Apache HttpClient 5.
Java 11 or later is required.
To reproduce the problem, run the following command:

```
$  ./gradlew test

> Task :test FAILED

JettyH2cTests > test() FAILED
    java.util.concurrent.ExecutionException at JettyH2cTests.java:59
        Caused by: org.apache.hc.core5.http.ConnectionClosedException at AbstractH2StreamMultiplexer.java:661

1 test completed, 1 failed
```

The problem does not occur when using Jetty 9.4.x, as shown by running the following command:

```
$ ./gradlew -PjettyVersion=9.4.39.v20210325 test

BUILD SUCCESSFUL in 1s
2 actionable tasks: 2 executed
```