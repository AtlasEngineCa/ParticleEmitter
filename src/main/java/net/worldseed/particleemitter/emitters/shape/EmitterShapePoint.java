package net.worldseed.particleemitter.emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.particleemitter.emitters.EmitterShape;
import net.minestom.server.coordinate.Vec;
import net.worldseed.particleemitter.runtime.ParticleEmitterScript;
import net.worldseed.particleemitter.runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;

public record EmitterShapePoint(ParticleEmitterScript offsetX, ParticleEmitterScript offsetY, ParticleEmitterScript offsetZ,
                                ParticleEmitterScript directionX, ParticleEmitterScript directionY, ParticleEmitterScript directionZ) implements EmitterShape {
    private static final JsonArray defaultOffset;

    static {
        defaultOffset = new JsonArray();
        defaultOffset.add("0");
        defaultOffset.add("0");
        defaultOffset.add("0");
    }

    public EmitterShapePoint() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this(ParticleEmitterScript.fromDouble(0), ParticleEmitterScript.fromDouble(0), ParticleEmitterScript.fromDouble(0),
                ParticleEmitterScript.fromDouble(0), ParticleEmitterScript.fromDouble(0), ParticleEmitterScript.fromDouble(0));
    }

    public static EmitterShape parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null) return new EmitterShapePoint(
                ParticleEmitterScript.fromDouble(0),
                ParticleEmitterScript.fromDouble(0),
                ParticleEmitterScript.fromDouble(0),
                ParticleEmitterScript.fromDouble(0),
                ParticleEmitterScript.fromDouble(0),
                ParticleEmitterScript.fromDouble(0)
        );

        JsonElement offsetEl = asJsonObject.get("offset");
        JsonArray offset = offsetEl == null ? defaultOffset : offsetEl.getAsJsonArray();

        JsonElement directionEl = asJsonObject.get("direction");

        JsonArray direction = directionEl == null ? null : directionEl.getAsJsonArray();

        ParticleEmitterScript offsetX = ParticleEmitterScript.fromString(offset.get(0).getAsString());
        ParticleEmitterScript offsetY = ParticleEmitterScript.fromString(offset.get(1).getAsString());
        ParticleEmitterScript offsetZ = ParticleEmitterScript.fromString(offset.get(2).getAsString());

        ParticleEmitterScript directionX = direction == null ? null : ParticleEmitterScript.fromString(direction.get(0).getAsString());
        ParticleEmitterScript directionY = direction == null ? null : ParticleEmitterScript.fromString(direction.get(1).getAsString());
        ParticleEmitterScript directionZ = direction == null ? null : ParticleEmitterScript.fromString(direction.get(2).getAsString());

        return new EmitterShapePoint(offsetX, offsetY, offsetZ, directionX, directionY, directionZ);
    }

    @Override
    public Vec emitPosition(ParticleInterface particleEmitter) {
        return new Vec(offsetX.evaluate(particleEmitter), offsetY.evaluate(particleEmitter), offsetZ.evaluate(particleEmitter));
    }

    @Override
    public Vec emitDirection(Vec origin, ParticleInterface particleEmitter) {
        if (directionX == null) {
            return new Vec(Math.random(), Math.random(), Math.random()).normalize();
        } else {
            return new Vec(directionX.evaluate(particleEmitter), directionY.evaluate(particleEmitter), directionZ.evaluate(particleEmitter)).normalize();
        }
    }

    @Override
    public boolean canRotate() {
        return true;
    }
}