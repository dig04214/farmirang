package com.cg.farmirang.farm.feature.design.entity;

import java.util.Comparator;

public class CropComparator implements Comparator<Crop> {
    @Override
    public int compare(Crop o1, Crop o2) {
        // Compare height in descending order
        int heightComparison = o2.getHeight().compareTo(o1.getHeight());
        if (heightComparison != 0) {
            return heightComparison;
        }

        // Compare isRepeated with true first
        return o2.getIsRepeated().compareTo(o1.getIsRepeated());
    }
}
