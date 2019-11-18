package com.jokls.jok.event.pack;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:32
 */
public class PackStatItem extends AbstractPackStat {
    private int startRange;
    private int endRange;
    private AtomicLong statTime = new AtomicLong(0L);
    private AtomicLong times = new AtomicLong(0L);

    public PackStatItem(int startRanger, int endRange) {
        this.startRange = startRanger;
        this.endRange = endRange;
    }

    public void packStatistics(long time, int complexity) {
        this.statTime.addAndGet(time);
        this.times.addAndGet(1L);
    }

    public long getPackStatTime() {
        return this.statTime.longValue();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PackStatItem)) {
            return false;
        } else {
            PackStatItem item = (PackStatItem)obj;
            return this.startRange == item.startRange && this.endRange == item.endRange;
        }
    }

    public int hashCode() {
        return (new Integer(this.startRange)).hashCode() + (new Integer(this.endRange)).hashCode();
    }

    public int getStartRange() {
        return this.startRange;
    }

    public int getEndRange() {
        return this.endRange;
    }

    public long getTimes() {
        return this.times.longValue();
    }
}
