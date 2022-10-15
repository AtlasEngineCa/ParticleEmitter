package curves;

import java.util.ArrayList;
import java.util.List;

public class Curve {
    final String curve_name;
    final String input;

    public enum CurveType {
        LINEAR, BEZIER, BEZIER_CHAIN, CATMULL_ROM
    }

    List<CurveNode> nodes = new ArrayList<>();

    public Curve(String curve_name, String input) {
        this.curve_name = curve_name;
        this.input = input;
    }
}
