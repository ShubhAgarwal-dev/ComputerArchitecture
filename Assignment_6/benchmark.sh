ant clean
ant make-jar
echo "Starting for L1i Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1i/128_3.xml ./benchmark/L1i/stat/128_3.log ./benchmark/L1i/benchmark.out
echo "Done for 512B Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1i/256_4.xml ./benchmark/L1i/stat/256_4.log ./benchmark/L1i/benchmark.out
echo "Done for 1KB Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1i/4_1.xml ./benchmark/L1i/stat/4_1.log ./benchmark/L1i/benchmark.out
echo "Done for 16B Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1i/32_2.xml ./benchmark/L1i/stat/32_2.log ./benchmark/L1i/benchmark.out
echo "Done for 128B Cache"256

echo "Starting for L1d Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1d/128_3.xml ./benchmark/L1d/stat/128_3.log ./benchmark/L1d/benchmark.out
echo "Done for 512B Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1d/256_4.xml ./benchmark/L1d/stat/256_4.log ./benchmark/L1d/benchmark.out
echo "Done for 1KB Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1d/4_1.xml ./benchmark/L1d/stat/4_1.log ./benchmark/L1d/benchmark.out
echo "Done for 16B Cache"
java -Xmx1g -jar ./jars/simulator.jar ./benchmark/L1d/32_2.xml ./benchmark/L1d/stat/32_2.log ./benchmark/L1d/benchmark.out
echo "Done for 128B Cache"256

	
