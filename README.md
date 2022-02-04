# logAnalyzer

## Desciption
* This application is reading logs from all files in directory
* directory path can be provided in argument of main method
* default path selected as src/main/resources

## Step to Run Application
* in order to run application we need to run main method in App class
* Below is command to run app from maven (preffered to run form IDE)

    ```mvn exec:java -Dexec.args="src/main/resources/eventLogs/log1.txt"```

## Step to Run Test
* Below is command to run app from maven (preffered to run form IDE)

    ```mvn test```
