package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utils.Constants;

import java.util.TreeMap;

@Getter
@Setter
@ToString
public class GPU implements Comparable<GPU> {
    private final String name;
    private final Brand brand;
    private TreeMap<String, TreeMap<Month, String>> popularityMap = new TreeMap<>();

    public GPU(String name) {
        this.name = name;
        String toLowerCase = name.toLowerCase();
        if (toLowerCase.contains("nvidia")) {
            this.brand = Brand.NVIDIA;
        } else if (toLowerCase.contains("intel")) {
            this.brand = Brand.INTEL;
        } else if (toLowerCase.contains("ati") || toLowerCase.contains("amd") || toLowerCase.contains("radeon") || toLowerCase.contains("haswell")) {
            this.brand = Brand.AMD;
        } else {
            this.brand = Brand.OTHER;
        }
        initializePopularityMap();
    }

    private void initializePopularityMap() {
        for (String year : Constants.YEAR) {
            popularityMap.put(year, new TreeMap<>());
            for (Month month : Month.values()) {
                popularityMap.get(year).put(month, "-");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GPU)) {
            return false;
        }
        return this.getName().equals(((GPU) o).getName());
    }

    public void addInfo(String year, Month month, String popularity) {
        popularityMap.get(year).put(month, popularity);
    }

    @Override
    public int compareTo(GPU o) {
        if (getName() == null || o.getName() == null) {
            return 0;
        }
        return getName().compareTo(o.getName());
    }
}

