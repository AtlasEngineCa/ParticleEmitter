import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.worldseed.particleemitter.emitters.EmitterLifetime;
import net.worldseed.particleemitter.runtime.ParticleEmitter;
import net.worldseed.particleemitter.runtime.ParticleParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ParticleManager {
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static List<ParticleEmitter> getParticleEmitters(String particleName,int amount) throws FileNotFoundException, UnsupportedEncodingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException{
        File file = new File("particles/"+particleName);
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);

        List<ParticleEmitter> emitters = new ArrayList<>();
        {
            var emitter = ParticleParser.parse(Particle.DUST_COLOR_TRANSITION, 1000*amount, map);
            emitters.add(emitter);
        }

        return emitters;
    }

    public static void playParticle(String particleName, Vec position, int amount, InstanceContainer instance, boolean playInstantlyOnce) throws FileNotFoundException, UnsupportedEncodingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        List<ParticleEmitter> emitters = getParticleEmitters(particleName,amount);
        for (var emitter : emitters) {
            emitter.setPosition(position);
        }

        if (playInstantlyOnce){
            try {
                for (var emitter : emitters) {
                    emitter.reset();
                    Collection<ParticlePacket> packets = new ArrayList<>();
                    for (int i = 0; i < amount; i++) {
                        packets.addAll(emitter.tick());
                    }
                    if (emitter.status() != EmitterLifetime.LifetimeState.DEAD) {
                        packets.forEach(packet -> {
                            instance.getPlayers().forEach(p -> p.sendPackets(packet));
                        });
                    } else {
                        emitter.reset();
                    }
                    emitter.reset();
                }
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            new Timer().schedule(new TimerTask() {
                public void run()  {
                    try {
                        for (var emitter : emitters) {
                            for (int i = 0; i < amount; i++) {
                                Collection<ParticlePacket> packets = emitter.tick();
                                if (emitter.status() != EmitterLifetime.LifetimeState.DEAD) {
                                    packets.forEach(packet -> {
                                        instance.getPlayers().forEach(p -> p.sendPackets(packet));
                                    });
                                } else {
                                    emitter.reset();
                                    this.cancel();
                                }
                            }
                        }
                    } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, 1, 1);
        }
    }
}
