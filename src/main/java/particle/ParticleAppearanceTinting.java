package particle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import misc.Colour;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public record ParticleAppearanceTinting(Map<Double, Colour> color, String interpolant) {
    public static ParticleAppearanceTinting parse(JsonObject particleAppearanceTinting) {
        JsonElement colors = particleAppearanceTinting.get("colors");

        String interpolant = null;
        Map<Double, Colour> map = new HashMap<>();

        if (colors.isJsonArray()) {
            String r = colors.getAsJsonArray().get(0).getAsString();
            String g = colors.getAsJsonArray().get(1).getAsString();
            String b = colors.getAsJsonArray().get(2).getAsString();

            map.put(0.0, new Colour(r, g, b));
        } else if (colors.isJsonPrimitive()) {
            String color = colors.getAsString();
            var c = Color.decode(color);
            map.put(0.0, new Colour(c.getRed(), c.getGreen(), c.getBlue()));
        } else if (colors.isJsonObject()) {
            interpolant = colors.getAsJsonObject().get("interpolant").getAsString();

            if (colors.getAsJsonObject().has("gradient")) {
                JsonElement gradient = colors.getAsJsonObject().get("gradient");

                if (gradient.isJsonObject()) {
                    for (Map.Entry<String, JsonElement> entry : colors.getAsJsonObject().get("gradient").getAsJsonObject().entrySet()) {
                        String r = entry.getValue().getAsJsonArray().get(0).getAsString();
                        String g = entry.getValue().getAsJsonArray().get(1).getAsString();
                        String b = entry.getValue().getAsJsonArray().get(2).getAsString();

                        map.put(Double.parseDouble(entry.getKey()), new Colour(r, g, b));
                    }
                } else {
                    JsonArray gradientArray = gradient.getAsJsonArray();
                    for (int i = 0; i < gradientArray.size(); i++) {
                        String r = gradientArray.get(i).getAsJsonArray().get(0).getAsString();
                        String g = gradientArray.get(i).getAsJsonArray().get(1).getAsString();
                        String b = gradientArray.get(i).getAsJsonArray().get(2).getAsString();

                        map.put((double) i / (gradientArray.size() - 1), new Colour(r, g, b));
                    }
                }
            } else {
                for (Map.Entry<String, JsonElement> entry : colors.getAsJsonObject().entrySet()) {
                    String key = entry.getKey();
                    JsonElement value = entry.getValue();

                    String r = value.getAsJsonArray().get(0).getAsString();
                    String g = value.getAsJsonArray().get(1).getAsString();
                    String b = value.getAsJsonArray().get(2).getAsString();

                    map.put(Double.parseDouble(key), new Colour(r, g, b));
                }
            }
        }

        return new ParticleAppearanceTinting(map, interpolant);
    }
}
