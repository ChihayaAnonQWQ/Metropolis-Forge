package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoorController;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public abstract class AbstractPlatformDoorController extends HorizontalDirectionalBlock implements EntityBlock {
    public static final int MAX_DETECTION_RADIUS = 512;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty ATTACHED_ON_WALL = BooleanProperty.create("attached");

    public AbstractPlatformDoorController(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) instanceof BlockEntityPlatformDoorController blockEntity) {
            if (level.getGameTime() - blockEntity.lastToggleTime <= 20) {
                return InteractionResult.SUCCESS;
            }

            blockEntity.openState = !blockEntity.openState;
            blockEntity.lastToggleTime = level.getGameTime();

            scanPlatformDoors(level, blockPos, blockState, blockEntity.openState, blockEntity.lastToggleTime);

            level.blockEntityChanged(blockPos);
        }

        return InteractionResult.SUCCESS;
    }

    private static final Direction[] HORIZONTALS = {
            Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    /** A platform block, or a platform fence/door: everything the door row can be built from. */
    private static boolean isPlatformPart(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof IBlockPlatform || block instanceof AbstractBlockPlatformFence;
    }

    /**
     * Finds the platform the controller drives.
     * <p>
     * The original implementation only accepted {@code IBlockPlatform} blocks (i.e. a platform floored
     * with {@code metropolis:platform_a}) and silently did nothing otherwise, so a door row built on
     * any other floor could never be opened. Both the platform blocks and the fence/door row itself
     * are accepted now.
     */
    private static BlockPos findPlatformSeed(Level level, BlockPos blockPos) {
        for (Direction direction : HORIZONTALS) {
            for (int dy = 0; dy >= -1; dy--) {
                BlockPos candidate = blockPos.relative(direction).offset(0, dy, 0);
                if (isPlatformPart(level, candidate)) {
                    return candidate;
                }
            }
        }
        for (int dy = -1; dy >= -2; dy--) {
            BlockPos candidate = blockPos.offset(0, dy, 0);
            if (isPlatformPart(level, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    /** Opens or closes the door at {@code pos}, normalised to its lower half. */
    private static boolean applyDoorState(Level level, BlockPos pos, boolean openState) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof AbstractBlockPlatformDoor door)) {
            return false;
        }

        BlockPos lower = state.getValue(AbstractBlockPlatformFence.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        door.setOpenState(level, lower, openState);
        return true;
    }

    private void scanPlatformDoors(Level level, BlockPos blockPos, BlockState blockState, boolean openState, long lastToggleTime) {
        BlockPos platformPos = findPlatformSeed(level, blockPos);
        if (platformPos == null) {
            Metropolis.LOGGER.info("[platform-door] controller at {} found no Metropolis platform block / door row next to it", blockPos);
            return;
        }

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        visited.add(platformPos);
        queue.add(platformPos);

        int doors = 0;

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            // A door sits on top of the platform block, or the row itself is made of doors/ fences.
            if (applyDoorState(level, current.above(), openState)) {
                doors++;
            }
            if (applyDoorState(level, current, openState)) {
                doors++;
            }

            for (Direction direction : HORIZONTALS) {
                BlockPos next = current.relative(direction);

                if (visited.contains(next)) {
                    continue;
                }

                if (Math.abs(next.getX() - platformPos.getX()) + Math.abs(next.getZ() - platformPos.getZ()) > MAX_DETECTION_RADIUS) {
                    continue;
                }

                if (!isPlatformPart(level, next)) {
                    continue;
                }

                visited.add(next);
                queue.add(next);
            }
        }

        if (doors == 0) {
            Metropolis.LOGGER.info("[platform-door] controller at {} reached the door row but found no door block to toggle", blockPos);
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(POWERED, false).setValue(ATTACHED_ON_WALL, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, ATTACHED_ON_WALL);
    }

    /**
     * Is the controller powered.
     * <p>
     * {@code Level#hasNeighborSignal} is the standard "is this block receiving redstone power" test,
     * which is what a lever / dust / repeater / redstone block next to the pillar produces. The
     * original code only used {@code Level#hasSignal(pos, direction)}, which reports the signal a
     * block *emits* towards a direction, so a normally wired pillar never registered as powered and
     * the doors stayed shut. Both sets of checks are kept.
     */
    private static boolean isControllerPowered(Level level, BlockPos blockPos) {
        return level.hasNeighborSignal(blockPos)
                || level.hasSignal(blockPos, Direction.DOWN)
                || level.hasSignal(blockPos.below(), Direction.DOWN)
                || level.hasSignal(blockPos.below().below(), Direction.DOWN)
                || level.hasSignal(blockPos.below().below().below(), Direction.DOWN)
                || level.hasSignal(blockPos, Direction.UP)
                || level.hasSignal(blockPos.below(), Direction.UP)
                || level.hasSignal(blockPos.below().below(), Direction.UP)
                || level.hasSignal(blockPos.below().below().below(), Direction.UP);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        // The controller is server authoritative. Ticking on the client would write block states
        // locally and fight the synced state coming from the server.
        if (level.isClientSide()) {
            return null;
        }

        return (level1, blockPos, blockState1, blockEntity) -> {
            if (!(blockEntity instanceof BlockEntityPlatformDoorController controller)) {
                return;
            }

            final boolean powered = isControllerPowered(level1, blockPos);
            if (powered == blockState1.getValue(POWERED)) {
                return;
            }

            Metropolis.LOGGER.info("[platform-door] controller at {} powered={}", blockPos, powered);

            level1.setBlockAndUpdate(blockPos, blockState1.setValue(POWERED, powered));

            if (level1.getGameTime() - controller.lastToggleTime > 20) {
                controller.lastToggleTime = level1.getGameTime();
                scanPlatformDoors(level1, blockPos, blockState1, powered, level1.getGameTime());
            }
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityPlatformDoorController(blockPos, blockState);
    }
}
