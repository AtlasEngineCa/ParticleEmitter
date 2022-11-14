package net.worldseed.generator;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.binary.BinaryWriter;

import java.util.Map;

public class ParticleGenerator {
    public static ParticlePacket buildParticle(Particle particleType, double x, double y, double z, double size, double velocityX, double velocityY, double velocityZ, double r, double g, double b) {
        if (particleType == Particle.DUST) return buildDust(x, y, z, size, r, g, b);
        else if (particleType == Particle.NOTE) return buildNote(x, y, z, r, g, b);
        else if (particleType == Particle.ENTITY_EFFECT) return buildEffect(x, y, z, r, g, b);
        else if (particleType == Particle.AMBIENT_ENTITY_EFFECT) return buildEffectAmbient(x, y, z, r, g, b);
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

    private static ParticlePacket buildEffect(double x, double y, double z, double r, double g, double b) {
        return new ParticlePacket(Particle.ENTITY_EFFECT.id(), true, x, y, z, (float) r, (float) g, (float) b, 1, 0, new byte[0]);
    }

    private static ParticlePacket buildEffectAmbient(double x, double y, double z, double r, double g, double b) {
        return new ParticlePacket(Particle.AMBIENT_ENTITY_EFFECT.id(), true, x, y, z, (float) r, (float) g, (float) b, 1, 0, new byte[0]);
    }

    private static final Map<Vec, Double> noteColours = Map.ofEntries(
        Map.entry(new Vec(0.35, 0.9129164779574352, 0.0), 0.0),Map.entry(new Vec(0.5195469841701994, 0.8086557231547051, 0.0), 0.042),Map.entry(new Vec(0.6738203274905119, 0.6761781872346715, 0.0), 0.083),Map.entry(new Vec(0.8096194188492563, 0.5182322975301983, 0.0), 0.125),Map.entry(new Vec(0.9135959661842593, 0.3486385542433554, 0.0), 0.167),Map.entry(new Vec(0.9774980720415881, 0.18308286651870628, 0.0), 0.208),Map.entry(new Vec(0.9999999999999992, 0.024999913104564397, 0.025000217393631285), 0.25),Map.entry(new Vec(0.9774980556955284, 0.0, 0.18308320609876777), 0.292),Map.entry(new Vec(0.9135959349646745, 0.0, 0.3486389056053107), 0.333),Map.entry(new Vec(0.8096193745372532, 0.0, 0.5182326369205168), 0.375),Map.entry(new Vec(0.6738202731541083, 0.0, 0.6761784911550788), 0.417),Map.entry(new Vec(0.5195469236729805, 0.0, 0.8086559721254418), 0.458),Map.entry(new Vec(0.34999993733336554, 0.0, 0.9129166536387483), 0.5),Map.entry(new Vec(0.18045295533258326, 0.0, 0.9782028287289668), 0.542),Map.entry(new Vec(0.02617961817308767, 0.023821866970487715, 0.9999985750040863), 0.583),Map.entry(new Vec(0.0, 0.18176776300112518, 0.9778517099534143), 0.625),Map.entry(new Vec(0.0, 0.3513615084231421, 0.9122344477415489), 0.667),Map.entry(new Vec(0.0, 0.5169171940464481, 0.8105808060125694), 0.708),Map.entry(new Vec(0.0, 0.6750001411663266, 0.6749997283354576), 0.75),Map.entry(new Vec(0.0, 0.8105811423808698, 0.5169167333360676), 0.792),Map.entry(new Vec(0.0, 0.9122346869536953, 0.35136103172819166), 0.833),Map.entry(new Vec(0.0, 0.9778518333314586, 0.18176730254816992), 0.875),Map.entry(new Vec(0.026179781182298356, 0.9999985740057371, 0.023821454639781303), 0.917),Map.entry(new Vec(0.18045313682423997, 0.9782027063156398, 0.0), 0.958),Map.entry(new Vec(0.35000012533326885, 0.9129164152907789, 0.0), 1.0)
    );

    private static double calculateMinDiff(double r, double g, double b) {
        if (r < 0.1 && g < 0.1 && b < 0.1) return 32768;
        double val = 0;
        double minDistance = 10;

        for (var entry : noteColours.entrySet()) {
            double distance = Math.sqrt(Math.pow(entry.getKey().x() - r, 2) + Math.pow(entry.getKey().y() - g, 2) + Math.pow(entry.getKey().z() - b, 2));
            if (distance < minDistance) {
                minDistance = distance;
                val = entry.getValue();
            }
        }

        return val;
    }

    private static ParticlePacket buildNote(double x, double y, double z, double r, double g, double b) {
        return new ParticlePacket(Particle.NOTE.id(), true, x, y, z, (float) (calculateMinDiff(r, g, b)), 0, 0, 1, 0, new byte[0]);
    }
}
