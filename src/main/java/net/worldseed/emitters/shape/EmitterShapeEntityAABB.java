package net.worldseed.emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.emitters.EmitterShape;
import net.worldseed.misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;
import net.worldseed.runtime.ParticleEmitterScript;
import net.worldseed.runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;

public record EmitterShapeEntityAABB(boolean surfaceOnly,
                                     EmitterDirectionType type,
                                     ParticleEmitterScript directionX, ParticleEmitterScript directionY, ParticleEmitterScript directionZ) implements EmitterShape {
    private static final JsonArray defaultDirection;

    static {
        defaultDirection = new JsonArray();
        defaultDirection.add("0");
        defaultDirection.add("0");
        defaultDirection.add("0");
    }

    public static EmitterShape parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        boolean surface_only = asJsonObject.has("surface_only") && asJsonObject.get("surface_only").getAsBoolean();

        JsonElement direction = asJsonObject.get("direction");
        if (direction == null) direction = defaultDirection;

        if (direction.isJsonPrimitive()) {
            EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase());
            return new EmitterShapeEntityAABB(surface_only, type, null, null, null);
        } else {
            JsonArray directionArray = direction.getAsJsonArray();

            var directionX = ParticleEmitterScript.fromString(directionArray.get(0).getAsString());
            var directionY = ParticleEmitterScript.fromString(directionArray.get(1).getAsString());
            var directionZ = ParticleEmitterScript.fromString(directionArray.get(2).getAsString());

            return new EmitterShapeEntityAABB(surface_only, EmitterDirectionType.VELOCITY,
                    directionX, directionY, directionZ);
        }
    }

    @Override
    public Vec emitPosition(ParticleInterface particleEmitter) {
        return null;
    }

    @Override
    public Vec emitDirection(Vec origin, ParticleInterface particleEmitter) {
        return null;
    }

    @Override
    public boolean canRotate() {
        return false;
    }
}
