package awa.Aether_254.create_unrestricted_fluid_tank;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public final class TankAxis {
    public static final EnumProperty<Direction.Axis> AXIS =
        EnumProperty.create("cuft_axis", Direction.Axis.class);

    private TankAxis() {
    }

    public static Direction.Axis get(BlockState state) {
        return state.hasProperty(AXIS) ? state.getValue(AXIS) : Direction.Axis.Y;
    }
}
