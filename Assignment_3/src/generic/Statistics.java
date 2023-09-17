package generic;

import java.io.PrintWriter;

public class Statistics {

    // TODO add your statistics here
    static int staticInstCount;
    static int dynamicInstCount;
    static int numCycles;
    static float IPC;
    static float frequency;

    public static void setDynamicInstCount(int dynamicInstCount) {
        Statistics.dynamicInstCount = dynamicInstCount;
    }

    public static int getDynamicInstCount() {
        return dynamicInstCount;
    }

    public static void setStaticInstCount(int staticInstCount) {
        Statistics.staticInstCount = staticInstCount;
    }

    public static int getStaticInstCount() {
        return staticInstCount;
    }

    public static void setNumCycles(int numCycles) {
        Statistics.numCycles = numCycles;
    }

    public static int getNumCycles() {
        return numCycles;
    }

    public static void setIPC(float IPC) {
        Statistics.IPC = IPC;
    }

    public static float getIPC() {
        return IPC;
    }

    public static void setFrequency(float frequency) {
        Statistics.frequency = frequency;
    }

    public static float getFrequency() {
        return frequency;
    }

    public static void printStatistics(String statFile) {
        try {
            PrintWriter writer = new PrintWriter(statFile);

            writer.println("Static Instructions= " + staticInstCount);
            writer.println("Dynamic Instructions= " + dynamicInstCount);
            writer.println("Number of Cycles= " + numCycles);
            writer.println("IPC = " + IPC);
            writer.println("Frequency = " + frequency + " GHz");

            writer.close();
        } catch (Exception e) {
            Misc.printErrorAndExit(e.getMessage());
        }
    }
}
