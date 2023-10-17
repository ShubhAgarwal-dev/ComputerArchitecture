package processor.memorysystem;

import generic.*;
import generic.events.MemoryReadEvent;
import generic.events.MemoryResponseEvent;
import generic.events.MemoryWriteEvent;
import processor.Clock;

public class MainMemory implements Element {
    int[] memory;

    public MainMemory() {
        memory = new int[65536];
    }

    public int getWord(int address) {
        return memory[address];
    }

    public void setWord(int address, int value) {
        memory[address] = value;
    }

    public String getContentsAsString(int startingAddress, int endingAddress) {
        if (startingAddress == endingAddress)
            return "";

        StringBuilder sb = new StringBuilder();
        sb.append("\nMain Memory Contents:\n\n");
        for (int i = startingAddress; i <= endingAddress; i++) {
            sb.append(i + "\t\t: " + memory[i] + "\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public void handleEvent(Event event) {
        if (event.getEventType() == Event.EventType.MemoryRead) {
            System.out.println("[DEBUG][MM] Handling Memory Read Event");
            MemoryReadEvent e = (MemoryReadEvent) event;
            Simulator.getEventQueue().addEvent(new MemoryResponseEvent(
                    Clock.getCurrentTime(),
                    this,
                    event.getRequestingElement(),
                    getWord(((MemoryReadEvent) event).getAddressToReadFrom())
            ));
            System.out.println("[DEBUG][MM] Added Memory Response Event");
        } else if (event.getEventType() == Event.EventType.MemoryResponse) {
            System.out.println("[DEBUG][MM] Handling Memory Response Event");
			MemoryResponseEvent e = (MemoryResponseEvent) event;
            e.getRequestingElement().handleEvent(e);
        } else if (event.getEventType() == Event.EventType.MemoryWrite) {
            System.out.println("[DEBUG][MM] Handling Memory Write Event");
            MemoryWriteEvent e = (MemoryWriteEvent) event;
            e.getRequestingElement().handleEvent(e);
        }
    }
}
