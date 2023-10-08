package generic;

import processor.Clock;
import processor.Processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class Simulator {

    static Processor processor;
    static boolean simulationComplete;

    public static void setupSimulation(String assemblyProgramFile, Processor p)  {
        Simulator.processor = p;
        loadProgram(assemblyProgramFile);

        simulationComplete = false;
    }

    static void loadProgram(String assemblyProgramFile) {
        /*
         * 1. load the program into memory according to the program layout described
         *    in the ISA specification (Done)
         * 2. set PC to the address of the first instruction in the main
         * 3. set the following registers:
         *     x0 = 0
         *     x1 = 65535
         *     x2 = 65535
         */
        try (FileInputStream file = new FileInputStream(assemblyProgramFile)) {
            BufferedInputStream fis = new BufferedInputStream(file); // init the BufferReader as normal readers donot work
            int currLoc = 0; // counter to insert into memory
            byte[] bytes = new byte[4]; // reading 4 words at a time
            fis.read(bytes);
            processor.getRegisterFile().setProgramCounter(new BigInteger(bytes).intValue()-1);
            while (fis.read(bytes) != -1) {
                int val = new BigInteger(bytes).intValue();
                processor.getMainMemory().setWord(currLoc,val); // converting the bytes array to int and inserting it to memory
                currLoc+=1;
            }
            Statistics.setStaticInstCount(currLoc-processor.getRegisterFile().getProgramCounter());

        } catch (IOException e) {
            e.printStackTrace();
        }
        processor.getRegisterFile().setValue(0,0);
        processor.getRegisterFile().setValue(1,65535);
        processor.getRegisterFile().setValue(2,65535);
    }

//    public static void simulate() {
//
////        Statistics.
//        int cycles =0;
//        int numberOfInstructionsExecuted=0;
//        while (!simulationComplete) {
////            System.out.println("Iter:"+cycles);
//            processor.getIFUnit().performIF();
//            Clock.incrementClock();
//            processor.getOFUnit().performOF();
//            Clock.incrementClock();
//            processor.getEXUnit().performEX();
//            Clock.incrementClock();
//            processor.getMAUnit().performMA();
//            Clock.incrementClock();
//            processor.getRWUnit().performRW();
//            Clock.incrementClock();
//            cycles+=1;
//            numberOfInstructionsExecuted+=1;
//        }
//
//        Statistics.setNumCycles(cycles);
//        Statistics.setDynamicInstCount(numberOfInstructionsExecuted);
//        Statistics.setFrequency((float) Statistics.getNumCycles()/Clock.getCurrentTime());
//        Statistics.setIPC((float) Statistics.getDynamicInstCount()/Statistics.getNumCycles());
//    }

    public static void simulate() {

//        Statistics.
        int cycles =0;
        int numberOfInstructionsExecuted=0;

        while (!simulationComplete) {
//            System.out.println("Iter:"+cycles);
            processor.getRWUnit().performRW();
//            Clock.incrementClock();
            if(simulationComplete) { break; }
            processor.getMAUnit().performMA();
//            Clock.incrementClock();
            processor.getEXUnit().performEX();
//            Clock.incrementClock();
            processor.getOFUnit().performOF();
//            Clock.incrementClock();
            processor.getIFUnit().performIF();
            System.out.println("\n");
            Clock.incrementClock();
            cycles+=1;
            numberOfInstructionsExecuted+=1;
        }
//
        Statistics.setNumCycles(cycles);
        Statistics.setDynamicInstCount(numberOfInstructionsExecuted);
        Statistics.setFrequency((float) Statistics.getNumCycles()/Clock.getCurrentTime());
        Statistics.setIPC((float) Statistics.getDynamicInstCount()/Statistics.getNumCycles());
    }

//    public static void simulate()
//    {
////        Statistics.setNumberOfDynamicInstructions(0);
////        Statistics.setNumberOfCycles(0);
//
////        int i = 0;
//        while(!simulationComplete)
//        {
////            i++;
////            processor.getIF_EnableLatch().setIsBubbled(false);
//            processor.getRWUnit().performRW();
//            if(simulationComplete)
//            {
//                break;
//            }
//            processor.getMAUnit().performMA();
//            processor.getEXUnit().performEX();
//            processor.getOFUnit().performOF();
//            processor.getIFUnit().performIF();
//            Clock.incrementClock();
//
//            // Update statistics
////            if(i%5 == 0)
////            {
////                Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
////            }
//        }
//
//        // Set statistics
////        Statistics.setIPC((float)Statistics.getNumberOfDynamicInstructions() / Statistics.getNumberOfCycles());
////        Statistics.setFrequency((float)Statistics.getNumberOfCycles() / Clock.getCurrentTime());
//    }

    public static void setSimulationComplete(boolean value) {
        simulationComplete = value;
    }
}
