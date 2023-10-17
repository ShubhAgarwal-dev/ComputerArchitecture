package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.Simulator;
import generic.events.MemoryReadEvent;
import generic.events.MemoryResponseEvent;
import generic.events.MemoryWriteEvent;
import processor.Clock;
import processor.Processor;
import processor.latches.EX_MA_LatchType;
import processor.latches.MA_RW_LatchType;

public class MemoryAccess implements Element {
    Processor containingProcessor;
    EX_MA_LatchType EX_MA_Latch;
    MA_RW_LatchType MA_RW_Latch;
    int opCode;

    public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
        this.containingProcessor = containingProcessor;
        this.EX_MA_Latch = eX_MA_Latch;
        this.MA_RW_Latch = mA_RW_Latch;
    }

    public void performMA() {
        if (EX_MA_Latch.isMA_enable()) {
            if (EX_MA_Latch.isMA_Buzy()) {
                System.out.println("[Debug][MA] MA Busy.");
                return;
            }
            System.out.println("[Debug][MA] MA is running.");
            int opCode = EX_MA_Latch.getOpCode();
            if (opCode == 22) {
                int memoLocation = EX_MA_Latch.getOpRes();
                Simulator.getEventQueue().addEvent(
                        new MemoryReadEvent(
                                Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                                this,
                                containingProcessor.getMainMemory(),
                                memoLocation
                        )
                );
                System.out.println("[Debug][MA] Memory Read Event Added.");
                EX_MA_Latch.setMA_Buzy(true);
            } else if (opCode == 23) {
                int data = EX_MA_Latch.getOp1();
                int memoLocation = EX_MA_Latch.getOpRes();
                Simulator.getEventQueue().addEvent(
                        new MemoryWriteEvent(
                                Clock.getCurrentTime() + Configuration.mainMemoryLatency,
                                this,
                                containingProcessor.getMainMemory(),
                                memoLocation,
                                data
                        )
                );
                System.out.println("[Debug][MA] Memory Write Event Added.");
                EX_MA_Latch.setMA_Buzy(true);
            }else{
                EX_MA_Latch.setMA_enable(false);
                MA_RW_Latch.setOpResult(EX_MA_Latch.getOpRes());
                MA_RW_Latch.setR31(EX_MA_Latch.getR31());
                MA_RW_Latch.setOpCode(EX_MA_Latch.getOpCode());
                MA_RW_Latch.setRd(EX_MA_Latch.getRd());
                MA_RW_Latch.setRW_enable(true);
            }
        }
    }

    @Override
    public void handleEvent(Event event) {
        if (MA_RW_Latch.isRW_Buzy()) {
            System.out.println("[DEBUG][MA] Handling MA RW Busy");
            event.setEventTime(Clock.getCurrentTime() + 1);
            Simulator.getEventQueue().addEvent(event);
            return;
        } else if (event.getEventType() == Event.EventType.MemoryWrite) {
            System.out.println("[DEBUG][MA] Handling MA Memory Response");
            MemoryWriteEvent e = (MemoryWriteEvent) event;
            containingProcessor.getMainMemory().setWord(e.getAddressToWriteTo(), e.getValue());
            System.out.println("[DEBUG][MA] Word: "+ e.getValue() + " written to: " + e.getAddressToWriteTo());
        } else if (event.getEventType() == Event.EventType.MemoryResponse) {
            MemoryResponseEvent e = (MemoryResponseEvent) event;
            MA_RW_Latch.setLoadResult(e.getValue());
            System.out.println("[DEBUG][MA] Load Result: "+e.getValue());
        }
        MA_RW_Latch.setOpResult(EX_MA_Latch.getOpRes());
        MA_RW_Latch.setR31(EX_MA_Latch.getR31());
        MA_RW_Latch.setOpCode(EX_MA_Latch.getOpCode());
        MA_RW_Latch.setRd(EX_MA_Latch.getRd());
        MA_RW_Latch.setRW_enable(true);
        EX_MA_Latch.setMA_Buzy(false);
        EX_MA_Latch.setMA_enable(false);
        System.out.println("[Debug][MA] MA Disabled");
    }
}
