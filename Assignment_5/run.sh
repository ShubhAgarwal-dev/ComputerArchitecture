ant make-jar
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/evenorodd.stat ./test_cases/evenorodd.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/prime.stat ./test_cases/prime.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/fibonacci.stat ./test_cases/fibonacci.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/palindrome.stat ./test_cases/palindrome.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/descending.stat ./test_cases/descending.out

