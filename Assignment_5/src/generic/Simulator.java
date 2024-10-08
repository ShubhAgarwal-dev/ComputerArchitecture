package generic;

import processor.Clock;
import processor.Processor;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Simulator {

    static Processor processor;
    static boolean simulationComplete;

    static EventQueue eventQueue = new EventQueue();

    public static EventQueue getEventQueue() {
        return eventQueue;
    }

    public static boolean PrimeDebug = true;

    public static void setupSimulation(String assemblyProgramFile, Processor p) {
        Simulator.processor = p;
        loadProgram(assemblyProgramFile);
        simulationComplete = false;
    }

    static void loadProgram(String assemblyProgramFile) {
        try (FileInputStream file = new FileInputStream(assemblyProgramFile)) {
            BufferedInputStream fis = new BufferedInputStream(file); // init the BufferReader as normal readers donot work
            int currLoc = 0; // counter to insert into memory
            byte[] bytes = new byte[4]; // reading 4 words at a time
            fis.read(bytes);
            processor.getRegisterFile().setProgramCounter(new BigInteger(bytes).intValue() - 1);
            while (fis.read(bytes) != -1) {
                int val = new BigInteger(bytes).intValue();
                processor.getMainMemory().setWord(currLoc, val); // converting the bytes array to int and inserting it to memory
                currLoc += 1;
            }
            Statistics.setStaticInstCount(currLoc - processor.getRegisterFile().getProgramCounter());

        } catch (IOException e) {
            e.printStackTrace();
        }
        processor.getRegisterFile().setValue(0, 0);
        processor.getRegisterFile().setValue(1, 65535);
        processor.getRegisterFile().setValue(2, 65535);
    }

    public static void simulate() {

        Statistics.setStalls(0);
        Statistics.setNumBranchHazards(0);


        while (!simulationComplete) {
            System.out.println("[Debug] Cycle Number: "+Clock.getCurrentTime());
            processor.getRWUnit().performRW();
            if (simulationComplete) {
                break;
            }
            processor.getMAUnit().performMA();
            processor.getEXUnit().performEX();
            eventQueue.processEvents();
            processor.getOFUnit().performOF();
            processor.getIFUnit().performIF();
            System.out.println("\n");
            Clock.incrementClock();
        }
        processor.getRegisterFile().setProgramCounter(processor.getIFUnit().endPC+1);
        Statistics.setNumCycles((int) Clock.getCurrentTime());
//        Statistics.setDynamicInstCount(numberOfInstructionsExecuted - 4);
        Statistics.setFrequency((float) Statistics.getNumCycles() / Clock.getCurrentTime());
        float correct_inst = Statistics.getDynamicInstCount() - Statistics.getStalls() * 2 - Statistics.getNumBranchHazards();
        Statistics.setIPC(correct_inst / Statistics.getNumCycles());
    }


    public static void setSimulationComplete(boolean value) {
        simulationComplete = value;
    }
}
