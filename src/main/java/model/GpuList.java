package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GpuList {
    private static final List<GPU> gpuList = new ArrayList<>();

    private GpuList() {
    }

    public static void addGpu(final GPU gpu) {
        if (!gpuList.contains(gpu)) {
            gpuList.add(gpu);
        } else throw new RuntimeException(gpu.getName() + " already in list !");
    }

    public static GPU getGpu(final String name) {
        for (final GPU g : gpuList) {
            if (g.getName().equals(name)) {
                return g;
            }
        }
        return null;
    }

    public static List<GPU> getGpuList() {
        Collections.sort(gpuList);
        return gpuList;
    }
}
