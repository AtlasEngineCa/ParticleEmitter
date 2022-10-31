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
        double radius = this.radius.evaluate(particleEmitter);
        double x = offsetX.evaluate(particleEmitter);
        double y = offsetY.evaluate(particleEmitter);
        double z = offsetZ.evaluate(particleEmitter);

        if (surfaceOnly) {
            double theta = Math.random() * 2 * Math.PI;
            double phi = Math.acos(2 * Math.random() - 1);

            x += radius * Math.sin(phi) * Math.cos(theta);
            y += radius * Math.sin(phi) * Math.sin(theta);
            z += radius * Math.cos(phi);
        } else {
            double theta = Math.random() * 2 * Math.PI;
            double phi = Math.acos(2 * Math.random() - 1);
            double r = Math.random() * radius;

            x += r * Math.sin(phi) * Math.cos(theta);
            y += r * Math.sin(phi) * Math.sin(theta);
            z += r * Math.cos(phi);
        }

        return new Vec(x, y, z);
    }

    @Override
    public Vec emitDirection(ParticleEmitter particleEmitter) {
        if (type == EmitterDirectionType.VELOCITY) {
            double x = directionX.evaluate(particleEmitter);
            double y = directionY.evaluate(particleEmitter);
            double z = directionZ.evaluate(particleEmitter);

            return new Vec(x, y, z);
        } else if (type == EmitterDirectionType.INWARDS){
            return emitPosition(particleEmitter).normalize();
        } else {
            return Vec.ZERO.sub(emitPosition(particleEmitter).normalize());
        }
    }
}
