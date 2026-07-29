package awa.Aether_254.create_unrestricted_fluid_tank.mixin;

import awa.Aether_254.create_unrestricted_fluid_tank.TankAxis;
import awa.Aether_254.create_unrestricted_fluid_tank.UnrestrictedFluidTankConfig;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
abstract class BlockItemMixin {
    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void cuft$selectTankAxis(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        if (state == null || !(state.getBlock() instanceof FluidTankBlock) || !state.hasProperty(TankAxis.AXIS))
            return;

        UnrestrictedFluidTankConfig.Data config = UnrestrictedFluidTankConfig.get();
        if (!config.enabled || !config.horizontalTanksEnabled)
            return;

        BlockPos neighbourPos = context.getClickedPos().relative(context.getClickedFace().getOpposite());
        BlockState neighbour = context.getLevel().getBlockState(neighbourPos);
        Direction.Axis axis = neighbour.getBlock() instanceof FluidTankBlock && neighbour.hasProperty(TankAxis.AXIS)
            ? neighbour.getValue(TankAxis.AXIS)
            : Direction.Axis.Y;
        Player player = context.getPlayer();
        if (player != null && player.isShiftKeyDown() && context.getClickedFace().getAxis().isHorizontal())
            axis = context.getClickedFace().getAxis();
        cir.setReturnValue(state.setValue(TankAxis.AXIS, axis));
    }
}
