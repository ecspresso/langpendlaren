package langpendlaren.trafikverket.json;

public enum ActivityType {
    ANKOMST("Ankomst"),
    AVGANG("Avgang");


    private final String name;

    ActivityType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
