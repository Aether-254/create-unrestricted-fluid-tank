package awa.Aether_254.create_unrestricted_fluid_tank.mixin;

import awa.Aether_254.create_unrestricted_fluid_tank.TankAxis;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidTankBlock.class)
abstract class FluidTankBlockMixin extends Block {
    protected FluidTankBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void cuft$addAxis(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(TankAxis.AXIS);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cuft$setDefaultAxis(Block.Properties properties, boolean creative, CallbackInfo ci) {
        FluidTankBlock self = (FluidTankBlock) (Object) this;
        registerDefaultState(self.defaultBlockState().setValue(TankAxis.AXIS, Direction.Axis.Y));
    }
}
