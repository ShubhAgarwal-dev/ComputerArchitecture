package generic;

import processor.Clock;
import processor.Processor;

import java.io.*;

public class Simulator {

    static Processor processor;
    static boolean simulationComplete;

    static int numInst;
    static int numDataHazards;

    static int numNop;

    static EventQueue eventQueue;

    public static void setupSimulation(String assemblyProgramFile, Processor p) {
        Simulator.processor = p;
        loadProgram(assemblyProgramFile);

        simulationComplete = false;

        numInst = numDataHazards = numNop = 0;

        eventQueue = new EventQueue();
    }

    static void loadProgram(String assemblyProgramFile) {

        try {
            FileInputStream fis = new FileInputStream(assemblyProgramFile);
            DataInputStream dis = new DataInputStream(fis);
            try {
                try {
                    int pc = -1, address = 0;
                    while (dis.available() > 0) {
                        int num = dis.readInt();
                        if (pc == -1) {

                            pc = num;

                            Simulator.processor.getRegisterFile().setProgramCounter(pc);
                        } else {

                            Simulator.processor.getMainMemory().setWord(address, num);
                            ++address;
                        }
                    }
                } catch (EOFException e) {
                }
                dis.close();
            } catch (IOException e) {
                Misc.printErrorAndExit(e.toString());
            }
        } catch (FileNotFoundException e) {
            Misc.printErrorAndExit(e.toString());
        }

        Simulator.processor.getRegisterFile().setValue(0, 0);
        Simulator.processor.getRegisterFile().setValue(1, 65535);
        Simulator.processor.getRegisterFile().setValue(2, 65535);
    }

    public static void simulate() {

        int count = 0;

        while (!simulationComplete) {
            processor.getRWUnit().performRW();
            if (simulationComplete) {
                break;
            }
            processor.getMAUnit().performMA();
            processor.getEXUnit().performEX();
            eventQueue.processEvents();
            processor.getOFUnit().performOF();
            processor.getIFUnit().performIF();
            Clock.incrementClock();

        }
        processor.getRegisterFile().setProgramCounter(processor.getRegisterFile().getProgramCounter() - 1);
        Statistics.setNumCycles((int) Clock.getCurrentTime());
        Statistics.setFrequency((float) Statistics.getNumCycles() / Clock.getCurrentTime());
        float correct_inst = Statistics.getDynamicInstCount() - Statistics.getStalls() * 2 - Statistics.getNumBranchHazards();
        Statistics.setIPC(correct_inst / Statistics.getNumCycles());
    }

    public static void setSimulationComplete(boolean value) {
        simulationComplete = value;
    }


    public static void incNumInst() {
        ++numInst;
    }


    public static void incNumDataHazards() {
        ++numDataHazards;
    }


    public static void incNop() {
        ++numNop;
    }

    public static EventQueue getEventQueue() {
        return eventQueue;
    }
}
