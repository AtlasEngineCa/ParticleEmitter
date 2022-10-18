package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;

import java.util.Locale;

public record EmitterShapeSphere(String offsetX, String offsetY, String offsetZ,
                                 String radius, boolean surfaceOnly,
                                 EmitterDirectionType type,
                                 String directionX, String directionY, String directionZ) implements EmitterShape {
    public static EmitterShape parse(JsonObject asJsonObject) {
        JsonArray offset = asJsonObject.get("offset").getAsJsonArray();
        String radius = asJsonObject.get("radius").getAsString();
        boolean surface_only = asJsonObject.get("surface_only").getAsBoolean();

        JsonElement direction = asJsonObject.get("direction");

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase(Locale.ROOT));
            return new EmitterShapeSphere(offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                    radius, surface_only, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();
            return new EmitterShapeSphere(offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                    radius, surface_only, EmitterDirectionType.VELOCITY, directionArray.get(0).getAsString(),
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
