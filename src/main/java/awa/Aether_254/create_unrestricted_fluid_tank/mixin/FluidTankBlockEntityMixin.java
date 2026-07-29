package awa.Aether_254.create_unrestricted_fluid_tank.mixin;

import awa.Aether_254.create_unrestricted_fluid_tank.TankAxis;
import awa.Aether_254.create_unrestricted_fluid_tank.UnrestrictedFluidTankConfig;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidTankBlockEntity.class)
abstract class FluidTankBlockEntityMixin {
    @Shadow(remap = false)
    protected int height;

    @Shadow(remap = false)
    protected boolean window;

    @Inject(method = "getMainConnectionAxis", at = @At("HEAD"), cancellable = true, remap = false)
    private void cuft$connectionAxis(CallbackInfoReturnable<Direction.Axis> cir) {
        if (UnrestrictedFluidTankConfig.get().enabled) {
            FluidTankBlockEntity self = (FluidTankBlockEntity) (Object) this;
            cir.setReturnValue(TankAxis.get(self.getBlockState()));
        }
    }

    @Inject(method = "getMaxWidth", at = @At("HEAD"), cancellable = true, remap = false)
    private void cuft$maximumWidth(CallbackInfoReturnable<Integer> cir) {
        if (UnrestrictedFluidTankConfig.get().enabled)
            cir.setReturnValue(UnrestrictedFluidTankConfig.get().maxWidth);
    }

    @Inject(method = "getMaxLength", at = @At("HEAD"), cancellable = true, remap = false)
    private void cuft$maximumLength(Direction.Axis axis, int width, CallbackInfoReturnable<Integer> cir) {
        if (UnrestrictedFluidTankConfig.get().enabled)
            cir.setReturnValue(UnrestrictedFluidTankConfig.get().maxLength);
    }

    @Redirect(
        method = {"onFluidStackChanged", "setWindows", "updateBoilerState"},
        at = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;")
    )
    private BlockPos cuft$orientPartLookup(BlockPos origin, int x, int y, int z) {
        FluidTankBlockEntity self = (FluidTankBlockEntity) (Object) this;
        return switch (TankAxis.get(self.getBlockState())) {
            case X -> origin.offset(y, x, z);
            case Z -> origin.offset(x, z, y);
            default -> origin.offset(x, y, z);
        };
    }

    @Inject(method = "updateBoilerState", at = @At("HEAD"), cancellable = true, remap = false)
    private void cuft$disableHorizontalBoilers(CallbackInfo ci) {
        FluidTankBlockEntity self = (FluidTankBlockEntity) (Object) this;
        if (TankAxis.get(self.getBlockState()) != Direction.Axis.Y)
            ci.cancel();
    }

    @Inject(method = "notifyMultiUpdated", at = @At("HEAD"), cancellable = true, remap = false)
    private void cuft$orientEndCaps(CallbackInfo ci) {
        FluidTankBlockEntity self = (FluidTankBlockEntity) (Object) this;
        Direction.Axis axis = TankAxis.get(self.getBlockState());
        if (axis == Direction.Axis.Y)
            return;

        BlockState state = self.getBlockState();
        if (FluidTankBlock.isTank(state)) {
            int blockCoordinate = axis.choose(self.getBlockPos().getX(), self.getBlockPos().getY(), self.getBlockPos().getZ());
            BlockPos controller = self.getController();
            int controllerCoordinate = axis.choose(controller.getX(), controller.getY(), controller.getZ());
            state = state.setValue(FluidTankBlock.BOTTOM, blockCoordinate == controllerCoordinate);
            state = state.setValue(FluidTankBlock.TOP, blockCoordinate == controllerCoordinate + height - 1);
            self.getLevel().setBlock(self.getBlockPos(), state, Block.UPDATE_CLIENTS | Block.UPDATE_INVISIBLE);
        }
        if (self.isController())
            self.setWindows(window);
        self.notifyUpdate();
        self.setChanged();
        ci.cancel();
    }
}
