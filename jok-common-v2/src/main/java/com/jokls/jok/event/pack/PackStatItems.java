package com.jokls.jok.event.pack;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Copyright (C) 2019
 * All rights reserved
 *
 * @author: marik.wei
 * @mail: marks@126.com
 * Date: 2019/6/14 11:32
 */
public class PackStatItems extends AbstractPackStat {
    Set<PackStatItem> packStats = new TreeSet(new Comparator<PackStatItem>() {
        public int compare(PackStatItem item1, PackStatItem item2) {
            if (item1.getStartRange() > item2.getStartRange()) {
                return 1;
            } else {
                return item1.getStartRange() < item2.getStartRange() ? -1 : 0;
            }
        }
    });

    public PackStatItems() {
    }

    public void packStatistics(long time, int complexity) {
        PackStatItem packStat = this.getPackStat(complexity);
        if (packStat != null) {
            packStat.packStatistics(time, complexity);
        }
    }

    public PackStatItem getPackStat(int complexity) {
        Iterator it = this.packStats.iterator();

        PackStatItem packStat;
        do {
            if (!it.hasNext()) {
                return null;
            }

            packStat = (PackStatItem)it.next();
        } while(packStat.getStartRange() > complexity || packStat.getEndRange() <= complexity);

        return packStat;
    }

    public void addPackStat(PackStatItem packStat) {
        this.packStats.add(packStat);
    }

    public Set<PackStatItem> getPackStats() {
        return this.packStats;
    }

    public void removePackStat(PackStatItem packStat) {
        this.packStats.remove(packStat);
    }
}
