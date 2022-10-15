package emitters;

import net.minestom.server.coordinate.Vec;

public interface EmitterShape {
    Vec emitPosition();
    Vec emitDirection();
}
