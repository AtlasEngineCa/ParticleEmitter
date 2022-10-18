package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;

public record EmitterShapeEntityAABB(boolean surfaceOnly,
                                     EmitterDirectionType type,
                                     String directionX, String directionY, String directionZ) implements EmitterShape {
    public static EmitterShape parse(JsonObject asJsonObject) {
        boolean surface_only = asJsonObject.get("surface_only").getAsBoolean();
        JsonElement direction = asJsonObject.get("direction");

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase());
            return new EmitterShapeEntityAABB(surface_only, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();
            return new EmitterShapeEntityAABB(surface_only, EmitterDirectionType.VELOCITY, directionArray.get(0).getAsString(),
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
