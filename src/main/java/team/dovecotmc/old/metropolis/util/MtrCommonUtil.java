package team.dovecotmc.old.metropolis.util;

import net.minecraft.world.item.Item;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrCommonUtil {
    public static Item getBrushItem() {
        // MTR 3 (package "mtr"): mtr.Items.BRUSH is a RegistryObject<Item>.
        return mtr.Items.BRUSH.get();
    }
}
