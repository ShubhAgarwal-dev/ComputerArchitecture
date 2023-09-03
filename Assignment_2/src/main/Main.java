package main;

import generic.Misc;
import generic.Simulator;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2)
        {
            Misc.printErrorAndExit("usage: java -jar <path-to-jar-file> <path-to-assembly-program> <path-to-object-file>\n");
        }
        Simulator.setupSimulation(args[0]);
        Simulator.assemble(args[1]);
//        SimulatorDada.setupSimulation(args[0]);
//        SimulatorDada.assemble(args[1]);
    }
}

