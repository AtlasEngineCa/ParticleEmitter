package net.worldseed.emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.worldseed.emitters.EmitterShape;
import net.worldseed.misc.EmitterDirectionType;
import net.worldseed.misc.EmitterPlaneNormalType;
import net.minestom.server.coordinate.Vec;
import net.worldseed.runtime.ParticleEmitter;
import net.worldseed.runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;

public record EmitterShapeDisc(EmitterPlaneNormalType planeNormalType,
                               ParticleEmitterScript planeX, ParticleEmitterScript planeY, ParticleEmitterScript planeZ,
                               ParticleEmitterScript offsetX, ParticleEmitterScript offsetY, ParticleEmitterScript offsetZ,
                               ParticleEmitterScript radius, boolean surfaceOnly,
                               EmitterDirectionType type,
                               ParticleEmitterScript directionX, ParticleEmitterScript directionY, ParticleEmitterScript directionZ) implements EmitterShape {
    private static final JsonArray defaultPlaneNormal;
    private static final JsonArray defaultOffset;

    static {
        defaultPlaneNormal = new JsonArray();
        defaultPlaneNormal.add("0");
        defaultPlaneNormal.add("1");
        defaultPlaneNormal.add("0");

        defaultOffset = new JsonArray();
        defaultOffset.add("0");
        defaultOffset.add("0");
        defaultOffset.add("0");
    }

    public static EmitterShape parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        JsonElement plane = asJsonObject.get("plane_normal");
        if (plane == null) plane = defaultPlaneNormal;

        JsonElement offsetEl = asJsonObject.get("offset");
        JsonArray offset = offsetEl == null ? defaultOffset : offsetEl.getAsJsonArray();

        JsonElement radiusEl = asJsonObject.get("radius");
        ParticleEmitterScript radius = radiusEl == null ? ParticleEmitterScript.fromDouble(1) : ParticleEmitterScript.fromString(radiusEl.getAsString());

        boolean surface_only = asJsonObject.has("surface_only") && asJsonObject.get("surface_only").getAsBoolean();
        JsonElement direction = asJsonObject.get("direction");
        if (direction == null) direction = new JsonPrimitive("outwards");

        var offsetX = ParticleEmitterScript.fromString(offset.get(0).getAsString());
        var offsetY = ParticleEmitterScript.fromString(offset.get(1).getAsString());
        var offsetZ = ParticleEmitterScript.fromString(offset.get(2).getAsString());

        if (plane.isJsonPrimitive()) {
            EmitterPlaneNormalType planeNormalType = EmitterPlaneNormalType.valueOf(plane.getAsString().toUpperCase());
            if (direction.isJsonPrimitive()) {
                EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase());
                return new EmitterShapeDisc(planeNormalType,
                        null, null, null,
                        offsetX, offsetY, offsetZ,
                        radius, surface_only, type,
                        null, null, null);
            } else {
                JsonArray directionArray = direction.getAsJsonArray();
                var directionX = ParticleEmitterScript.fromString(directionArray.get(0).getAsString());
                var directionY = ParticleEmitterScript.fromString(directionArray.get(1).getAsString());
                var directionZ = ParticleEmitterScript.fromString(directionArray.get(2).getAsString());

                return new EmitterShapeDisc(planeNormalType, null, null, null,
                        offsetX, offsetY, offsetZ,
                        radius, surface_only, EmitterDirectionType.VELOCITY,
                        directionX, directionY, directionZ);
            }
        } else {
            JsonArray planeArray = plane.getAsJsonArray();
            var planeX = ParticleEmitterScript.fromString(planeArray.get(0).getAsString());
            var planeY = ParticleEmitterScript.fromString(planeArray.get(1).getAsString());
            var planeZ = ParticleEmitterScript.fromString(planeArray.get(2).getAsString());

            if (direction.isJsonPrimitive()) {
                EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase());
                return new EmitterShapeDisc(EmitterPlaneNormalType.CUSTOM,
                        planeX, planeY, planeZ,
                        offsetX, offsetY, offsetZ,
                        radius, surface_only, type,
                        null, null, null);
            } else {
                JsonArray directionArray = direction.getAsJsonArray();
                var directionX = ParticleEmitterScript.fromString(directionArray.get(0).getAsString());
                var directionY = ParticleEmitterScript.fromString(directionArray.get(1).getAsString());
                var directionZ = ParticleEmitterScript.fromString(directionArray.get(2).getAsString());

                return new EmitterShapeDisc(EmitterPlaneNormalType.CUSTOM,
                        planeX, planeY, planeZ,
                        offsetX, offsetY, offsetZ,
                        radius, surface_only, EmitterDirectionType.VELOCITY,
                        directionX, directionY, directionZ);
            }
        }
    }

    @Override
    public Vec emitPosition(ParticleEmitter particleEmitter) {
        double normalX = 0;
        double normalY = 0;
        double normalZ = 0;

        if (planeNormalType == EmitterPlaneNormalType.CUSTOM) {
            normalX = planeX.evaluate(particleEmitter);
            normalY = planeY.evaluate(particleEmitter);
            normalZ = planeZ.evaluate(particleEmitter);
        } else {
            switch (planeNormalType) {
                case X -> normalX = 1;
                case Y -> normalY = 1;
                case Z -> normalZ = 1;
            }
        }

        if (normalX == 0 && normalY == 0 && normalZ == 0) {
            normalY = 1;
        }

        double radius = this.radius.evaluate(particleEmitter);
        double angle = Math.random() * 2 * Math.PI;

        double offsetX = this.offsetX.evaluate(particleEmitter);
        double offsetY = this.offsetY.evaluate(particleEmitter);
        double offsetZ = this.offsetZ.evaluate(particleEmitter);

        if (!surfaceOnly) {
            radius *= Math.random();
        }

        Vec normal = new Vec(normalX, normalY, normalZ);
        Vec tangent = (normalX == 0 && normalY == 0 ? new Vec(0, -normalZ, normalY) : new Vec(-normalY, normalX, 0)).normalize();
        Vec w = normal.cross(tangent).normalize();

        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;

        return w.mul(x).add(tangent.mul(z)).add(offsetX, offsetY, offsetZ);
    }

    @Override
    public Vec emitDirection(ParticleEmitter particleEmitter) {
        return null;
    }
}
