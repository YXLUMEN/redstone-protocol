package lumen.redstone_protocol;

import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;

public class RPProperties {
    public static final IntProperty LASER_MODE = IntProperty.of("laser_mode", 0, 4);
    public static final DirectionProperty OUTPUT_FACING = DirectionProperty.of("output_facing", Direction.Type.HORIZONTAL);
}
