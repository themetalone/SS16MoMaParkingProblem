del *.db
del log.txt
java -Dorg.slf4j.simpleLogger.showShortLogName="true" -Dorg.slf4j.simpleLogger.logFile="./log.txt" -Dcfg="./spring-config.xml" -jar ..\..\parking.simulation-0.0.1-SNAPSHOT-jar-with-dependencies.jar