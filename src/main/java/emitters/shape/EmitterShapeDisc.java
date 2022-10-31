package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import misc.EmitterPlaneNormalType;
import net.minestom.server.coordinate.Vec;
import runtime.ParticleEmitter;
import runtime.ParticleEmitterScript;

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
        double x = 0;
        double y = 0;
        double z = 0;

        if (planeNormalType == EmitterPlaneNormalType.CUSTOM) {
            x = planeX.evaluate(particleEmitter);
            y = planeY.evaluate(particleEmitter);
            z = planeZ.evaluate(particleEmitter);
        } else {
            switch (planeNormalType) {
                case X -> x = 1;
                case Y -> y = 1;
                case Z -> z = 1;
            }
        }

        double radius = this.radius.evaluate(particleEmitter);
        double angle = Math.random() * 2 * Math.PI;
        double distance = Math.random() * radius;

        double offsetX = this.offsetX.evaluate(particleEmitter);
        double offsetY = this.offsetY.evaluate(particleEmitter);
        double offsetZ = this.offsetZ.evaluate(particleEmitter);

        double x1 = x * distance * Math.cos(angle) + offsetX;
        double y1 = y * distance * Math.sin(angle) + offsetY;
        double z1 = z * distance * Math.cos(angle) + offsetZ;

        if (surfaceOnly) {
            double length = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
            x1 = x1 / length * radius;
            y1 = y1 / length * radius;
            z1 = z1 / length * radius;
        }

        return new Vec(x1, y1, z1);
    }

    @Override
    public Vec emitDirection(ParticleEmitter particleEmitter) {
        return null;
    }
}
