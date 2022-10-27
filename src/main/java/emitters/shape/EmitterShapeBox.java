package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;
import runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public record EmitterShapeBox(ParticleEmitterScript offsetX, ParticleEmitterScript offsetY, ParticleEmitterScript offsetZ,
                              ParticleEmitterScript halfDimensionX, ParticleEmitterScript halfDimensionY, ParticleEmitterScript halfDimensionZ,
                              boolean surfaceOnly,
                              EmitterDirectionType type,
                              ParticleEmitterScript directionX, ParticleEmitterScript directionY, ParticleEmitterScript directionZ) implements EmitterShape {
    private static final JsonArray defaultOffset;

    static {
        defaultOffset = new JsonArray();
        defaultOffset.add("0");
        defaultOffset.add("0");
        defaultOffset.add("0");
    }

    public static EmitterShape parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JsonElement offsetEl = asJsonObject.get("offset");
        JsonArray offset = offsetEl != null ? offsetEl.getAsJsonArray() : defaultOffset;

        JsonArray half_dimension = asJsonObject.get("half_dimensions").getAsJsonArray();
        boolean surface_only = asJsonObject.has("surface_only") && asJsonObject.get("surface_only").getAsBoolean();

        JsonElement direction = asJsonObject.get("direction");
        if (direction == null) direction = new JsonPrimitive("outwards");

        var offsetX = ParticleEmitterScript.fromString(offset.get(0).getAsString());
        var offsetY = ParticleEmitterScript.fromString(offset.get(1).getAsString());
        var offsetZ = ParticleEmitterScript.fromString(offset.get(2).getAsString());

        var halfDimensionX = ParticleEmitterScript.fromString(half_dimension.get(0).getAsString());
        var halfDimensionY = ParticleEmitterScript.fromString(half_dimension.get(1).getAsString());
        var halfDimensionZ = ParticleEmitterScript.fromString(half_dimension.get(2).getAsString());

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase(Locale.ROOT));
            return new EmitterShapeBox(offsetX, offsetY, offsetZ, halfDimensionX, halfDimensionY, halfDimensionZ, surface_only, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();
            var directionX = ParticleEmitterScript.fromString(directionArray.get(0).getAsString());
            var directionY = ParticleEmitterScript.fromString(directionArray.get(1).getAsString());
            var directionZ = ParticleEmitterScript.fromString(directionArray.get(2).getAsString());

            return new EmitterShapeBox(offsetX, offsetY, offsetZ, halfDimensionX, halfDimensionY, halfDimensionZ, surface_only, EmitterDirectionType.VELOCITY, directionX, directionY, directionZ);
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