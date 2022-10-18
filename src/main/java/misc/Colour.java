package misc;

public record Colour(String r, String g, String b) {
    public Colour(int red, int green, int blue) {
        this(String.valueOf(red), String.valueOf(green), String.valueOf(blue));
    }
}
