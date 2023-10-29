ant cleanall
ant make-jar
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/256LI_128_LD_2048_L2/evenorodd.log ./test_cases/evenorodd.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/256LI_128_LD_2048_L2/prime.log ./test_cases/prime.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/256LI_128_LD_2048_L2/fibonacci.log ./test_cases/fibonacci.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/256LI_128_LD_2048_L2/palindrome.log ./test_cases/palindrome.out
java -Xmx1g -jar ./jars/simulator.jar ./src/configuration/config.xml ./stat/256LI_128_LD_2048_L2/descending.log ./test_cases/descending.out

