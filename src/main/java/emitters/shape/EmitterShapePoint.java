package emitters.shape;

import emitters.EmitterShape;
import net.minestom.server.coordinate.Vec;

public record EmitterShapePoint(String offsetX, String offsetY, String offsetZ,
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
