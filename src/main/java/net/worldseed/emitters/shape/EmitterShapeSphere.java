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
        JsonElement offsetEl = asJsonObject.get("offset");
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
    public Vec emitPosition(ParticleInterface particleEmitter) {
        double radius = this.radius.evaluate(particleEmitter);
        double x = offsetX.evaluate(particleEmitter);
        double y = offsetY.evaluate(particleEmitter);
        double z = offsetZ.evaluate(particleEmitter);

        if (surfaceOnly) {
            var theta = Math.random() * Math.PI * 2;
            var v = Math.random();
            var phi = Math.acos((2*v)-1);
            x += radius * Math.sin(phi) * Math.cos(theta);
            y += radius * Math.sin(phi) * Math.sin(theta);
            z += radius * Math.cos(phi);
        } else {
            var theta = Math.random() * Math.PI * 2;
            var v = Math.random();
            var phi = Math.acos((2*v)-1);
            var r = Math.pow(Math.random(), 1.0/3) * radius;
            x += r * Math.sin(phi) * Math.cos(theta);
            y += r * Math.sin(phi) * Math.sin(theta);
            z += r * Math.cos(phi);
        }

        return new Vec(x, y, z);
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
