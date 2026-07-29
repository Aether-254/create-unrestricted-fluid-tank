package awa.Aether_254.create_unrestricted_fluid_tank;

import awa.Aether_254.create_unrestricted_fluid_tank.client.UnrestrictedFluidTankConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(CreateUnrestrictedFluidTank.MOD_ID)
public final class CreateUnrestrictedFluidTank {
    public static final String MOD_ID = "create_unrestricted_fluid_tank";

    public CreateUnrestrictedFluidTank(ModContainer container) {
        UnrestrictedFluidTankConfig.load();
        if (FMLEnvironment.dist == Dist.CLIENT)
            UnrestrictedFluidTankConfigScreen.register(container);
    }
}
