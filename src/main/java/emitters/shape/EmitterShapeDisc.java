package emitters.shape;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import emitters.EmitterShape;
import misc.EmitterDirectionType;
import misc.EmitterPlaneNormalType;
import net.minestom.server.coordinate.Vec;

public record EmitterShapeDisc(EmitterPlaneNormalType planeNormalType,
                               String planeX, String planeY, String planeZ,
                               String offsetX, String offsetY, String offsetZ,
                               String radius, boolean surfaceOnly,
                               EmitterDirectionType type,
                               String directionX, String directionY, String directionZ) implements EmitterShape {
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

    public static EmitterShape parse(JsonObject asJsonObject) {
        JsonElement plane = asJsonObject.get("plane_normal");
        if (plane == null) plane = defaultPlaneNormal;

        JsonElement offsetEl = asJsonObject.get("offset");
        JsonArray offset = offsetEl == null ? defaultOffset : offsetEl.getAsJsonArray();

        JsonElement radiusEl = asJsonObject.get("radius");
        String radius = radiusEl == null ? "1" : radiusEl.getAsString();

        boolean surface_only = asJsonObject.has("surface_only") && asJsonObject.get("surface_only").getAsBoolean();
        JsonElement direction = asJsonObject.get("direction");
        if (direction == null) direction = new JsonPrimitive("outwards");

        if (plane.isJsonPrimitive()) {
            EmitterPlaneNormalType planeNormalType = EmitterPlaneNormalType.valueOf(plane.getAsString().toUpperCase());
            if (direction.isJsonPrimitive()) {
                EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase());
                return new EmitterShapeDisc(planeNormalType, null, null, null,
                        offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                        radius, surface_only, type, null, null, null);
            } else {
                JsonArray directionArray = direction.getAsJsonArray();
                return new EmitterShapeDisc(planeNormalType, null, null, null,
                        offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                        radius, surface_only, EmitterDirectionType.VELOCITY, directionArray.get(0).getAsString(),
                        directionArray.get(1).getAsString(), directionArray.get(2).getAsString());
            }
        } else {
            JsonArray planeArray = plane.getAsJsonArray();
            if (direction.isJsonPrimitive()) {
                EmitterDirectionType type = EmitterDirectionType.valueOf(direction.getAsString().toUpperCase());
                return new EmitterShapeDisc(EmitterPlaneNormalType.CUSTOM, planeArray.get(0).getAsString(), planeArray.get(1).getAsString(), planeArray.get(2).getAsString(),
                        offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                        radius, surface_only, type, null, null, null);
            } else {
                JsonArray directionArray = direction.getAsJsonArray();
                return new EmitterShapeDisc(EmitterPlaneNormalType.CUSTOM, planeArray.get(0).getAsString(), planeArray.get(1).getAsString(), planeArray.get(2).getAsString(),
                        offset.get(0).getAsString(), offset.get(1).getAsString(), offset.get(2).getAsString(),
                        radius, surface_only, EmitterDirectionType.VELOCITY, directionArray.get(0).getAsString(),
                        directionArray.get(1).getAsString(), directionArray.get(2).getAsString());
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
