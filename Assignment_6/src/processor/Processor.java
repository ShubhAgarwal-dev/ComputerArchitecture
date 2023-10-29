package processor;

import processor.interlocks.BranchLock;
import processor.interlocks.DataLock;
import processor.memorysystem.Cache;
import processor.memorysystem.MainMemory;
import processor.memorysystem.RegisterFile;
import processor.pipeline.stages.RegisterWrite;
import processor.pipeline.latch.*;
import processor.pipeline.stages.Execute;
import processor.pipeline.stages.InstructionFetch;
import processor.pipeline.stages.MemoryAccess;
import processor.pipeline.stages.OperandFetch;

public class Processor {

    RegisterFile registerFile;
    MainMemory mainMemory;

    IF_EnableLatchType IF_EnableLatch;
    IF_OF_LatchType IF_OF_Latch;
    OF_EX_LatchType OF_EX_Latch;
    EX_MA_LatchType EX_MA_Latch;
    EX_IF_LatchType EX_IF_Latch;
    MA_RW_LatchType MA_RW_Latch;

    InstructionFetch IFUnit;
    OperandFetch OFUnit;
    Execute EXUnit;
    MemoryAccess MAUnit;
    RegisterWrite RWUnit;

    DataLock dataLockUnit; // The Data-Interlock unit of the processor
    BranchLock branchLockUnit; // The Control-Interlock unit of the processor

    Cache l1iCache; // L1i Cache
    Cache l1dCache; // L1d Cache

    public Processor() {
        registerFile = new RegisterFile();
        mainMemory = new MainMemory();

        IF_EnableLatch = new IF_EnableLatchType();
        IF_OF_Latch = new IF_OF_LatchType();
        OF_EX_Latch = new OF_EX_LatchType();
        EX_MA_Latch = new EX_MA_LatchType();
        EX_IF_Latch = new EX_IF_LatchType();
        MA_RW_Latch = new MA_RW_LatchType();

        IFUnit = new InstructionFetch(this, IF_EnableLatch, IF_OF_Latch, EX_IF_Latch);
        OFUnit = new OperandFetch(this, IF_OF_Latch, OF_EX_Latch);
        EXUnit = new Execute(this, OF_EX_Latch, EX_MA_Latch, EX_IF_Latch);
        MAUnit = new MemoryAccess(this, EX_MA_Latch, MA_RW_Latch);
        RWUnit = new RegisterWrite(this, MA_RW_Latch, IF_EnableLatch);

        // Initializing Data-Interlock unit and Control-Interlock unit
        dataLockUnit =
                new DataLock(this, IF_EnableLatch, IF_OF_Latch, EX_MA_Latch, MA_RW_Latch);
        branchLockUnit = new BranchLock(IF_OF_Latch, EX_IF_Latch);

        l1iCache = new Cache(this, 0);
        l1dCache = new Cache(this, 1);
    }

    public void printState(int memoryStartingAddress, int memoryEndingAddress) {
        System.out.println(registerFile.getContentsAsString());

        System.out.println(
                mainMemory.getContentsAsString(memoryStartingAddress, memoryEndingAddress));
    }

    public RegisterFile getRegisterFile() {
        return registerFile;
    }

    public void setRegisterFile(RegisterFile registerFile) {
        this.registerFile = registerFile;
    }

    public MainMemory getMainMemory() {
        return mainMemory;
    }

    public void setMainMemory(MainMemory mainMemory) {
        this.mainMemory = mainMemory;
    }

    public InstructionFetch getIFUnit() {
        return IFUnit;
    }

    public OperandFetch getOFUnit() {
        return OFUnit;
    }

    public Execute getEXUnit() {
        return EXUnit;
    }

    public MemoryAccess getMAUnit() {
        return MAUnit;
    }

    public RegisterWrite getRWUnit() {
        return RWUnit;
    }

    // Getter and Setter Methods for Interlock units defined above
    public DataLock getDataInterlockUnit() {
        return dataLockUnit;
    }

    public void setDataInterlockUnit(DataLock dataLockUnit) {
        this.dataLockUnit = dataLockUnit;
    }

    public BranchLock getControlInterlockUnit() {
        return branchLockUnit;
    }

    public void setControlInterlockUnit(BranchLock branchLockUnit) {
        this.branchLockUnit = branchLockUnit;
    }

    public Cache getL1iCache() {
        return l1iCache;
    }

    public void setL1iCache(Cache l1iCache) {
        this.l1iCache = l1iCache;
    }

    public Cache getL1dCache() {
        return l1dCache;
    }

    public void setL1dCache(Cache l1dCache) {
        this.l1dCache = l1dCache;
    }

}
