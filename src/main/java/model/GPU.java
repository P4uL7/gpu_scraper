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

    public GPU(final String name) {
        this.name = name;
        final String toLowerCase = name.toLowerCase();
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
        for (final String year : Constants.YEAR) {
            popularityMap.put(year, new TreeMap<>());
            for (final Month month : Month.values()) {
                popularityMap.get(year).put(month, "-");
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GPU)) {
            return false;
        }
        return this.getName().equals(((GPU) o).getName());
    }

    public void addInfo(final String year, final Month month, final String popularity) {
        popularityMap.get(year).put(month, popularity);
    }

    @Override
    public int compareTo(final GPU o) {
        if (getName() == null || o.getName() == null) {
            return 0;
        }
        return getName().compareTo(o.getName());
    }
}

