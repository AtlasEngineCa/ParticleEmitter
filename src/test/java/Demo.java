import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.server.ServerTickMonitorEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import runtime.Parser;
import runtime.ParticleEmitter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Demo {
    private final ClassLoader classLoader = getClass().getClassLoader();
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();
        instanceContainer.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));

        List<ParticleEmitter> emitters = new ArrayList<>();

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            final Player player = event.getPlayer();
            player.setPermissionLevel(2);
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 42, 0));

            File file = new File("./src/particles/loading.particle.json");
            try {
                FileInputStream fis = new FileInputStream(file);
                JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
                JsonObject map = GSON.fromJson(reader, JsonObject.class);
                ParticleEmitter emitter = Parser.parse(map);
                emitters.add(emitter);
            } catch (FileNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        });

        globalEventHandler.addListener(ServerTickMonitorEvent.class, event -> {
            for (var emitter : emitters) {
                Collection<ParticlePacket> packets = null;
                try {
                    packets = emitter.tick();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                packets.forEach(packet -> {
                    instanceContainer.getPlayers().forEach(p -> p.sendPackets(packet));
                });
            }
        });

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
    }
}
