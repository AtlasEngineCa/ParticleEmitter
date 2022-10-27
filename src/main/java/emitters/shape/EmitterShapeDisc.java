package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import misc.EmitterPlaneNormalType;
import net.minestom.server.coordinate.Vec;
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
            System.out.println(planeArray);
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
    public Vec emitPosition() {
        return null;
    }

    @Override
    public Vec emitDirection() {
        return null;
    }
}
