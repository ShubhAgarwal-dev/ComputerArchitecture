ant make-jar
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/evenorodd_1.log ./test_cases/evenorodd.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/prime_1.log ./test_cases/prime.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/fibonacci_1.log ./test_cases/fibonacci.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/palindrome_1.log ./test_cases/palindrome.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/descending_1.log ./test_cases/descending.out

