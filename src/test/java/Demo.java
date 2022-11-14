import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minestom.server.particle.Particle;
import net.worldseed.emitters.EmitterLifetime;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.timer.ExecutionType;
import net.minestom.server.timer.TaskSchedule;
import net.worldseed.runtime.ParticleEmitter;
import net.worldseed.runtime.ParticleParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Demo {
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));

        File file = new File("./src/test/resources/particles/rgb.particle.json");
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);

        List<ParticleEmitter> emitters = new ArrayList<>();

        {
            var emitter = ParticleParser.parse(Particle.DUST, 1000, map);
            emitters.add(emitter);
        }

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setPermissionLevel(2);
            player.setGameMode(GameMode.CREATIVE);
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));

            for (var emitter : emitters) {
                emitter.setPosition(new Vec(0, 60, 0));
            }
        });

        // globalEventHandler.addListener(PlayerMoveEvent.class, event -> {
        //     emitter.setRotation(-event.getPlayer().getPosition().yaw());
        //     emitter.setPosition(event.getPlayer().getPosition().add(0, 1.1, 0));
        // });

        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            try {
                for (var emitter : emitters) {
                    Collection<ParticlePacket> packets = emitter.tick();

                    if (emitter.status() != EmitterLifetime.LifetimeState.DEAD) {
                        packets.forEach(packet -> {
                            instanceContainer.getPlayers().forEach(p -> p.sendPackets(packet));
                        });
                    } else {
                        emitter.reset();
                    }
                }
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, TaskSchedule.immediate(), TaskSchedule.millis(1), ExecutionType.ASYNC);

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
    }
}
