package processor.memorysystem;

import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;


public class Cache implements Element {

    Processor containingProcessor;
    int cacheType;
    int NUM_CACHE_LINES;
    int CACHE_LATENCY;
    int CACHE_LINE_SIZE;
    int CACHE_LINE_ARRAY_SIZE;
    int CACHE_SIZE;
    CacheLine[] buffer;
    int currIndex;
    boolean isCacheBusy;


    public Cache(Processor containingProcessor, int cacheType) {
        this.containingProcessor = containingProcessor;
        this.cacheType = cacheType;
        if (this.cacheType == 0) {
            this.NUM_CACHE_LINES = Configuration.L1i_numberOfLines;
            this.CACHE_LATENCY = Configuration.L1i_latency;
        } else {
            this.NUM_CACHE_LINES = Configuration.L1d_numberOfLines;
            this.CACHE_LATENCY = Configuration.L1d_latency;
        }
        this.CACHE_LINE_SIZE = Configuration.CACHE_LINE_SIZE;
        this.CACHE_LINE_ARRAY_SIZE = Configuration.CACHE_LINE_SIZE / Configuration.INSTRUCTION_SIZE;
        this.CACHE_SIZE = this.CACHE_LINE_SIZE * this.NUM_CACHE_LINES;

        this.buffer = new CacheLine[this.NUM_CACHE_LINES];
        for (int i = 0; i < this.NUM_CACHE_LINES; ++i) {
            this.buffer[i] = null;
        }

        this.currIndex = 0;
        this.isCacheBusy = false;
    }

    public boolean isCacheBusy() {
        return isCacheBusy;
    }

    public void setCacheBusy(boolean isCacheBusy) {
        this.isCacheBusy = isCacheBusy;
    }


    public boolean cacheRead(int address, Element processingElement) {

        for (int i = 0; i < NUM_CACHE_LINES && buffer[i] != null; ++i) {
            int index = buffer[i].findIndexOf(address);
            if (index != -1) {

                Simulator.getEventQueue()
                        .addEvent(new CacheResponseEvent(Clock.getCurrentTime() + CACHE_LATENCY,
                                this, processingElement, buffer[i].getDataAt(index)));
                isCacheBusy = true;
                return true;
            }
        }
        handleCacheMiss(address);
        return false;
    }


    public void cacheWrite(int address, int value, Element processingElement) {


        Simulator.getEventQueue().addEvent(new MemoryWriteEvent(Clock.getCurrentTime(), this,
                containingProcessor.getMainMemory(), address, value));

        Simulator.getEventQueue()
                .addEvent(new CacheResponseEvent(
                        Clock.getCurrentTime() + CACHE_LATENCY + Configuration.mainMemoryLatency,
                        this, processingElement, value));
    }


    public void handleCacheMiss(int address) {

        if (containingProcessor.getMainMemory().isMainBusy()) {

            return;
        }

        Simulator.getEventQueue().addEvent(new MemoryReadEvent(Clock.getCurrentTime(), this,
                containingProcessor.getMainMemory(), address));
        isCacheBusy = true;
    }


    public void handleResponse(int address) {

        boolean isPresent = false;
        for (int i = 0; i < NUM_CACHE_LINES && buffer[i] != null; ++i) {
            int index = buffer[i].findIndexOf(address);
            if (index != -1) {

                buffer[i].setDataAt(index, containingProcessor.getMainMemory().getWord(address));
                isPresent = true;
            }
        }
        if (!isPresent) {

            buffer[currIndex] = new CacheLine(CACHE_LINE_SIZE, CACHE_LINE_ARRAY_SIZE);
            for (int i = 0; i < CACHE_LINE_ARRAY_SIZE && ((address + i) < containingProcessor
                    .getMainMemory().MAIN_MEMORY_SIZE); ++i) {
                buffer[currIndex].setValuesAt(i, address + i,
                        containingProcessor.getMainMemory().getWord(address + i));
            }
            currIndex = (currIndex + 1) % NUM_CACHE_LINES;
        }
    }


    @Override
    public void handleEvent(Event e) {

        if (e.getEventType() == Event.EventType.CacheRead) {
            CacheReadEvent event = (CacheReadEvent) e;

            boolean cacheHit = cacheRead(event.getAddressToReadFrom(), e.getRequestingElement());
            if (!cacheHit) {

                e.setEventTime(Clock.getCurrentTime() + 1);
                Simulator.getEventQueue().addEvent(e);
            }

        } else if (e.getEventType() == Event.EventType.CacheWrite) {
            if (containingProcessor.getMainMemory().isMainBusy()) {

                e.setEventTime(Clock.getCurrentTime() + 1);
                Simulator.getEventQueue().addEvent(e);
            } else {
                CacheWriteEvent event = (CacheWriteEvent) e;

                cacheWrite(event.getAddressToWriteTo(), event.getValue(),
                        event.getRequestingElement());
                isCacheBusy = true;
            }

        } else if (e.getEventType() == Event.EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e;

            handleResponse(event.getAddress());
            containingProcessor.getMainMemory().setMainBusy(false);
        }
    }

}
