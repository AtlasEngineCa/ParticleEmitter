package misc;

public record Colour(double r, double g, double b) {
    public Colour(String r, String g, String b) {
        this(Double.parseDouble(r), Double.parseDouble(g), Double.parseDouble(b));
    }

    public static Colour white() {
        return new Colour(255, 255, 255);
    }
}
