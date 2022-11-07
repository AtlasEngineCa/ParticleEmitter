package net.worldseed.generator;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.binary.BinaryWriter;

public class ParticleGenerator {
    public static ParticlePacket buildParticle(Particle particleType, double x, double y, double z, double size, double velocityX, double velocityY, double velocityZ, double r, double g, double b) {
        if (particleType == Particle.DUST) return buildDust(x, y, z, size, r, g, b);
        else if (particleType == Particle.FLAME
                || particleType == Particle.SMOKE
                || particleType == Particle.FIREWORK
                || particleType == Particle.SOUL_FIRE_FLAME
            ) return buildDirectional(particleType, x, y, z, velocityX, velocityY, velocityZ);
        else return buildGeneric(particleType, x, y, z);
    }

    private static ParticlePacket buildGeneric(Particle p, double x, double y, double z) {
        return new ParticlePacket(p.id(), true, x, y, z, 0, 0, 0, 0, 0, new byte[0]);
    }

    private static ParticlePacket buildDirectional(Particle p, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        Vec vec = new Vec(velocityX, velocityY, velocityZ);
        double size = vec.length();
        vec = vec.normalize();

        return new ParticlePacket(p.id(), true, x, y, z, (float) vec.x(), (float) vec.y(), (float) vec.z(), (float) size, 0, new byte[0]);
    }

    private static ParticlePacket buildDust(double x, double y, double z, double size, double r, double g, double b) {
        BinaryWriter writer = new BinaryWriter();
        writer.writeFloat((float) r);
        writer.writeFloat((float) g);
        writer.writeFloat((float) b);
        writer.writeFloat((float) size);
        return new ParticlePacket(Particle.DUST.id(), true, x, y, z, 0, 0, 0, 0, 0, writer.toByteArray());
    }
}
