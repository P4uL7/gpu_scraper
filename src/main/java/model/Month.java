package model;

public enum Month {

    Jan("JAN"),
    Feb("FEB"),
    Mar("MAR"),
    Apr("APR"),
    May("MAY"),
    Jun("JUN"),
    Jul("JUL"),
    Aug("AUG"),
    Sep("SEP"),
    Oct("OCT"),
    Nov("NOV"),
    Dec("DEC");

    private final String text;

    Month(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
