package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;

import java.util.Locale;

public record EmitterShapeBox(String offsetX, String offsetY, String offsetZ,
                              String halfDimensionX, String halfDimensionY, String halfDimensionZ,
                              boolean surfaceOnly,
                              EmitterDirectionType type,
                              String directionX, String directionY, String directionZ) implements EmitterShape {
    public static EmitterShape parse(JsonObject asJsonObject) {
        JsonArray offset = asJsonObject.get("offset").getAsJsonArray();
        JsonArray half_dimension = asJsonObject.get("half_dimensions").getAsJsonArray();
        boolean surface_only = asJsonObject.get("surface_only").getAsBoolean();

        JsonElement direction = asJsonObject.get("direction");

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase(Locale.ROOT));
            return new EmitterShapeBox(offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                    half_dimension.get(0).getAsString(), half_dimension.get(1).getAsString(), half_dimension.get(2).getAsString(),
                    surface_only, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();
            return new EmitterShapeBox(offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                    half_dimension.get(0).getAsString(), half_dimension.get(1).getAsString(), half_dimension.get(2).getAsString(),
                    surface_only, EmitterDirectionType.VELOCITY, directionArray.get(0).getAsString(),
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