package processor.memorysystem;

import configuration.Configuration;
import generic.*;
import processor.Clock;


public class MainMemory implements Element {
    int[] memory;
    boolean isMainBusy;

    public MainMemory() {
        memory = new int[65536];
        isMainBusy = false;
    }

    public int getWord(int address) {
        return memory[address];
    }

    public void setWord(int address, int value) {
        memory[address] = value;
    }

    public boolean isMainMemoBuzy() {
        return isMainBusy;
    }

    public void setMainMemoBuzy(boolean isMainBusy) {
        this.isMainBusy = isMainBusy;
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
    public void handleEvent(Event e) {
        if (e.getEventType() == Event.EventType.MemoryRead) {
            MemoryReadEvent event = (MemoryReadEvent) e;
            MemoryResponseEvent newMemoResponseEvent = new MemoryResponseEvent(
                    Clock.getCurrentTime() + Configuration.mainMemoryLatency, this,
                    event.getRequestingElement(), getWord(event.getAddressToReadFrom()),
                    event.getAddressToReadFrom());
            Simulator.getEventQueue().addEvent(newMemoResponseEvent);
            isMainBusy = true;

        } else if (e.getEventType() == Event.EventType.MemoryWrite) {
            MemoryWriteEvent currEvent = (MemoryWriteEvent) e;
            setWord(currEvent.getAddressToWriteTo(), currEvent.getValue());
            MemoryResponseEvent newMemoResponseEvent = new MemoryResponseEvent(
                    Clock.getCurrentTime() + Configuration.mainMemoryLatency, this,
                    currEvent.getRequestingElement(), currEvent.getValue(), currEvent.getAddressToWriteTo());
            Simulator.getEventQueue().addEvent(newMemoResponseEvent);
            isMainBusy = true;
        }
    }
}
