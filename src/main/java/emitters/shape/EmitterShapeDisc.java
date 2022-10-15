package emitters.shape;

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
    @Override
    public Vec emitPosition() {
        return null;
    }

    @Override
    public Vec emitDirection() {
        return null;
    }
}
