package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityBumper extends BlockEntity {
    public BlockEntityBumper(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.BUMPER_BLOCK_ENTITY, pos, state);
    }
}
