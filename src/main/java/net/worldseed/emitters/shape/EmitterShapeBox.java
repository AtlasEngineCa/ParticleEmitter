package net.worldseed.emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.worldseed.emitters.EmitterShape;
import net.worldseed.misc.EmitterDirectionType;
import net.minestom.server.coordinate.Vec;
import net.worldseed.runtime.ParticleEmitterScript;
import net.worldseed.runtime.ParticleInterface;

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
    public Vec emitPosition(ParticleInterface particleEmitter) {
        double x = offsetX.evaluate(particleEmitter);
        double y = offsetY.evaluate(particleEmitter);
        double z = offsetZ.evaluate(particleEmitter);

        double halfDimensionX = this.halfDimensionX.evaluate(particleEmitter);
        double halfDimensionY = this.halfDimensionY.evaluate(particleEmitter);
        double halfDimensionZ = this.halfDimensionZ.evaluate(particleEmitter);

        double randomX = Math.random() * halfDimensionX * 2 - halfDimensionX;
        double randomY = Math.random() * halfDimensionY * 2 - halfDimensionY;
        double randomZ = Math.random() * halfDimensionZ * 2 - halfDimensionZ;

        // Force to surface
        if (surfaceOnly) {
            double random = Math.random() * 6;
            if (random < 1) {
                randomX = -halfDimensionX;
            } else if (random < 2) {
                randomX = halfDimensionX;
            } else if (random < 3) {
                randomY = -halfDimensionY;
            } else if (random < 4) {
                randomY = halfDimensionY;
            } else if (random < 5) {
                randomZ = -halfDimensionZ;
            } else {
                randomZ = halfDimensionZ;
            }
        }

        return new Vec(x + randomX, y + randomY, z + randomZ);
    }

    @Override
    public Vec emitDirection(Vec origin, ParticleInterface particleEmitter) {
        return switch (type) {
            case INWARDS -> origin.sub(emitPosition(particleEmitter)).normalize();
            case OUTWARDS -> emitPosition(particleEmitter).sub(origin).normalize();
            case VELOCITY ->
                    new Vec(directionX.evaluate(particleEmitter), directionY.evaluate(particleEmitter), directionZ.evaluate(particleEmitter)).normalize();
        };
    }

    @Override
    public boolean canRotate() {
        return type == EmitterDirectionType.VELOCITY;
    }
}