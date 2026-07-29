package awa.Aether_254.create_unrestricted_fluid_tank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.neoforged.fml.loading.FMLPaths;

public final class UnrestrictedFluidTankConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH =
        FMLPaths.CONFIGDIR.get().resolve("create_unrestricted_fluid_tank.json");
    private static Data data = new Data();

    private UnrestrictedFluidTankConfig() {
    }

    public static Data get() {
        return data;
    }

    public static void load() {
        try {
            if (Files.isRegularFile(PATH)) {
                Data loaded = GSON.fromJson(Files.readString(PATH), Data.class);
                data = loaded == null ? new Data() : loaded;
            }
        } catch (IOException | RuntimeException ignored) {
            data = new Data();
        }
        save();
    }

    public static void save() {
        data.maxWidth = Math.max(1, Math.min(64, data.maxWidth));
        data.maxLength = Math.max(1, Math.min(4096, data.maxLength));
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(data));
        } catch (IOException ignored) {
        }
    }

    public static final class Data {
        public boolean enabled = true;
        public boolean horizontalTanksEnabled = true;
        public int maxWidth = 3;
        public int maxLength = 32;
    }
}
