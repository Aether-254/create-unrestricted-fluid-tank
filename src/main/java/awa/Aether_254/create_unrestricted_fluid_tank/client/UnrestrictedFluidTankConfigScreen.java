package awa.Aether_254.create_unrestricted_fluid_tank.client;

import awa.Aether_254.create_unrestricted_fluid_tank.UnrestrictedFluidTankConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class UnrestrictedFluidTankConfigScreen {
    private UnrestrictedFluidTankConfigScreen() {
    }

    public static void register(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (mod, parent) -> create(parent));
    }

    private static Screen create(Screen parent) {
        UnrestrictedFluidTankConfig.Data config = UnrestrictedFluidTankConfig.get();
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("Create: Unrestricted Fluid Tank"));
        ConfigCategory category = builder.getOrCreateCategory(Component.literal("Fluid Tank limits"));
        ConfigEntryBuilder entries = builder.entryBuilder();

        category.addEntry(entries.startBooleanToggle(Component.literal("Enable mod"), config.enabled)
            .setDefaultValue(true)
            .setSaveConsumer(value -> config.enabled = value)
            .build());
        category.addEntry(entries.startBooleanToggle(Component.literal("Enable horizontal tanks"),
                config.horizontalTanksEnabled)
            .setDefaultValue(true)
            .setTooltip(Component.literal("Sneak-place a tank against a side face to use that horizontal axis."))
            .setSaveConsumer(value -> config.horizontalTanksEnabled = value)
            .build());
        category.addEntry(entries.startIntField(Component.literal("Maximum cross-section width"), config.maxWidth)
            .setDefaultValue(3)
            .setMin(1)
            .setMax(64)
            .setSaveConsumer(value -> config.maxWidth = value)
            .build());
        category.addEntry(entries.startIntField(Component.literal("Maximum tank length"), config.maxLength)
            .setDefaultValue(32)
            .setMin(1)
            .setMax(4096)
            .setSaveConsumer(value -> config.maxLength = value)
            .build());

        builder.setSavingRunnable(UnrestrictedFluidTankConfig::save);
        return builder.build();
    }
}
