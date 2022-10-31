package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;
import runtime.ParticleEmitter;
import runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

public record EmitterShapeSphere(ParticleEmitterScript offsetX, ParticleEmitterScript offsetY, ParticleEmitterScript offsetZ,
                                 ParticleEmitterScript radius, boolean surfaceOnly,
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
        JsonElement offsetEl = asJsonObject.get("offset").getAsJsonArray();
        JsonArray offset = offsetEl != null ? offsetEl.getAsJsonArray() : defaultOffset;

        JsonElement radiusEl = asJsonObject.get("radius");
        ParticleEmitterScript radius = radiusEl != null ? ParticleEmitterScript.fromString(radiusEl.getAsString()) : ParticleEmitterScript.fromDouble(1);

        JsonElement surfaceOnlyEl = asJsonObject.get("surface_only");
        boolean surfaceOnly = surfaceOnlyEl != null && surfaceOnlyEl.getAsBoolean();

        JsonElement direction = asJsonObject.get("direction");
        if (direction == null) direction = new JsonPrimitive("outwards");

        var offsetX = ParticleEmitterScript.fromString(offset.get(0).getAsString());
        var offsetY = ParticleEmitterScript.fromString(offset.get(1).getAsString());
        var offsetZ = ParticleEmitterScript.fromString(offset.get(2).getAsString());

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase(Locale.ROOT));

            return new EmitterShapeSphere(offsetX, offsetY, offsetZ,
                    radius, surfaceOnly, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();

            var directionX = ParticleEmitterScript.fromString(directionArray.get(0).getAsString());
            var directionY = ParticleEmitterScript.fromString(directionArray.get(1).getAsString());
            var directionZ = ParticleEmitterScript.fromString(directionArray.get(2).getAsString());

            return new EmitterShapeSphere(offsetX, offsetY, offsetZ,
                    radius, surfaceOnly, EmitterDirectionType.VELOCITY,
                    directionX, directionY, directionZ);
        }

    }

    @Override
    public Vec emitPosition(ParticleEmitter particleEmitter) {
        return null;
    }

    @Override
    public Vec emitDirection(ParticleEmitter particleEmitter) {
        return null;
    }
}
