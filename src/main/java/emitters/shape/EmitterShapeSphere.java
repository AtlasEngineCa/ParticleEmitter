package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;

import java.util.Locale;

public record EmitterShapeSphere(String offsetX, String offsetY, String offsetZ,
                                 String radius, boolean surfaceOnly,
                                 EmitterDirectionType type,
                                 String directionX, String directionY, String directionZ) implements EmitterShape {
    private static final JsonArray defaultOffset;

    static {
        defaultOffset = new JsonArray();
        defaultOffset.add("0");
        defaultOffset.add("0");
        defaultOffset.add("0");
    }

    public static EmitterShape parse(JsonObject asJsonObject) {
        JsonElement offsetEl = asJsonObject.get("offset").getAsJsonArray();
        JsonArray offset = offsetEl != null ? offsetEl.getAsJsonArray() : defaultOffset;

        JsonElement radiusEl = asJsonObject.get("radius");
        String radius = radiusEl != null ? radiusEl.getAsString() : "1";

        JsonElement surfaceOnlyEl = asJsonObject.get("surface_only");
        boolean surfaceOnly = surfaceOnlyEl != null && surfaceOnlyEl.getAsBoolean();

        JsonElement direction = asJsonObject.get("direction");
        if (direction == null) direction = new JsonPrimitive("outwards");

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase(Locale.ROOT));
            return new EmitterShapeSphere(offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                    radius, surfaceOnly, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();
            return new EmitterShapeSphere(offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                    radius, surfaceOnly, EmitterDirectionType.VELOCITY, directionArray.get(0).getAsString(),
                    directionArray.get(1).getAsString(), directionArray.get(2).getAsString());
        }

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
