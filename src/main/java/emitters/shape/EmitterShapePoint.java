package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterShape;
import net.minestom.server.coordinate.Vec;

public record EmitterShapePoint(String offsetX, String offsetY, String offsetZ,
                                String directionX, String directionY, String directionZ) implements EmitterShape {
    private static final JsonArray defaultOffset;

    static {
        defaultOffset = new JsonArray();
        defaultOffset.add("0");
        defaultOffset.add("0");
        defaultOffset.add("0");
    }

    public static EmitterShape parse(JsonObject asJsonObject) {
        if (asJsonObject == null) return new EmitterShapePoint("0", "0", "0", "0", "0", "0");
        JsonElement offsetEl = asJsonObject.get("offset");
        JsonArray offset = offsetEl == null ? defaultOffset : offsetEl.getAsJsonArray();

        JsonArray direction = asJsonObject.get("direction").getAsJsonArray();

        String offsetX = offset.get(0).getAsString();
        String offsetY = offset.get(1).getAsString();
        String offsetZ = offset.get(2).getAsString();

        String directionX = direction.get(0).getAsString();
        String directionY = direction.get(1).getAsString();
        String directionZ = direction.get(2).getAsString();

        return new EmitterShapePoint(offsetX, offsetY, offsetZ, directionX, directionY, directionZ);
    }

    @Override
    public Vec emitPosition() {
        return null;
    }

    @Override
    public Vec emitDirection() {
        return null;
    }
}
