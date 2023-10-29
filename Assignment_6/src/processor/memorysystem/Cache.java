package processor.memorysystem;

import configuration.Configuration;
import generic.*;
import generic.event.*;
import processor.Clock;
import processor.Processor;


public class Cache implements Element {

    Processor containingProcessor;
    int cacheType, numCacheLines, cacheLatency, cacheLineSize, cacheLineArraySize, cacheSize, currIndex, associativity;
    CacheLine[] buffer;
    boolean isCacheBusy;


    public Cache(Processor containingProcessor, int cacheType, int associativity) {
        this.containingProcessor = containingProcessor;
        this.cacheType = cacheType;
        if (this.cacheType == 0) {
            this.numCacheLines = Configuration.L1i_numberOfLines;
            this.cacheLatency = Configuration.L1i_latency;
        } else {
            this.numCacheLines = Configuration.L1d_numberOfLines;
            this.cacheLatency = Configuration.L1d_latency;
        }
        this.cacheLineSize = Configuration.CACHE_LINE_SIZE;
        this.cacheLineArraySize = Configuration.CACHE_LINE_SIZE / Configuration.INSTRUCTION_SIZE;
        this.cacheSize = this.cacheLineSize * this.numCacheLines;

        this.buffer = new CacheLine[this.numCacheLines];
        for (int i = 0; i < this.numCacheLines; ++i) {
            this.buffer[i] = null;
        }

        this.currIndex = 0;
        this.isCacheBusy = false;
        this.associativity = associativity;
    }

    public boolean isCacheBusy() {
        return isCacheBusy;
    }

    public void setCacheBusy(boolean isCacheBusy) {
        this.isCacheBusy = isCacheBusy;
    }


    public boolean cacheRead(int address, Element processingElement) {

        for (int i = 0; i < numCacheLines && buffer[i] != null; ++i) {
            int index = buffer[i].findIndexOf(address);
            if (index != -1) {
                updateCacheEvent(processingElement, i, index);
                Statistics.setCache_hit(Statistics.getCache_hit() + 1);
                return true;
            }
        }
        HandleCacheMiss(address);
        return false;
    }

    private void updateCacheEvent(Element processingElement, int i, int index) {
        CacheResponseEvent newEvent = new CacheResponseEvent(Clock.getCurrentTime() + cacheLatency, this, processingElement, buffer[i].getDataAt(index));
        Simulator.getEventQueue().addEvent(newEvent);
        isCacheBusy = true;
    }


    public void cacheWrite(int address, int value, Element processingElement) {
        MemoryWriteEvent newMemoEvent = new MemoryWriteEvent(Clock.getCurrentTime(), this, containingProcessor.getMainMemory(), address, value);
        Simulator.getEventQueue().addEvent(newMemoEvent);

        CacheResponseEvent newCacheEvent = new CacheResponseEvent(Clock.getCurrentTime() + cacheLatency + Configuration.mainMemoryLatency, this, processingElement, value);
        Simulator.getEventQueue().addEvent(newCacheEvent);
    }


    public void HandleCacheMiss(int address) {
        Statistics.setCache_misses(Statistics.getCache_misses()+1);
        if (containingProcessor.getMainMemory().isMainMemoBuzy()) {
            return;
        }
        updateCacheMissEvent(address);
    }

    private void updateCacheMissEvent(int address) {
        MemoryReadEvent newMemoReadEvent = new MemoryReadEvent(Clock.getCurrentTime(), this, containingProcessor.getMainMemory(), address);
        Simulator.getEventQueue().addEvent(newMemoReadEvent);
        isCacheBusy = true;
    }


    public void cacheResponseHandler(int address) {
        boolean isPresent = false;
        for (int i = 0; i < numCacheLines && buffer[i] != null; ++i) {
            int index = buffer[i].findIndexOf(address);
            if (index != -1) {
                buffer[i].setDataAt(index, containingProcessor.getMainMemory().getWord(address));
                isPresent = true;
            }
        }
        if (!isPresent) {
            buffer[currIndex] = new CacheLine(cacheLineSize, cacheLineArraySize);
            for (int i = 0; i < cacheLineArraySize && ((address + i) < 65536); ++i) {
                buffer[currIndex].setValuesAt(i, address + i, containingProcessor.getMainMemory().getWord(address + i));
            }
            currIndex = (currIndex + 1) % numCacheLines;
        }
    }

    @Override
    public void handleEvent(Event e) {

        if (e.getEventType() == Event.EventType.CacheRead) {
            CacheReadEvent event = (CacheReadEvent) e;
            boolean cacheHit = cacheRead(event.getAddr(), e.getRequestingElement());
            if (!cacheHit) {
                e.setEventTime(Clock.getCurrentTime() + 1);
                Simulator.getEventQueue().addEvent(e);
            }

        } else if (e.getEventType() == Event.EventType.CacheWrite) {
            if (containingProcessor.getMainMemory().isMainMemoBuzy()) {
                e.setEventTime(Clock.getCurrentTime() + 1);
                Simulator.getEventQueue().addEvent(e);
            } else {
                CacheWriteEvent event = (CacheWriteEvent) e;
                cacheWrite(event.getAddr(), event.getVal(), event.getRequestingElement());
                isCacheBusy = true;
            }

        } else if (e.getEventType() == Event.EventType.MemoryResponse) {
            MemoryResponseEvent event = (MemoryResponseEvent) e;
            cacheResponseHandler(event.getAddress());
            containingProcessor.getMainMemory().setMainMemoBuzy(false);
        }
    }

}
