package team.dovecotmc.metropolis.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegisterEvent;
import team.dovecotmc.metropolis.Metropolis;

public class MetroEntities {
    public static EntityType<EntitySittable> SITTABLE;

    public static void registerAll(RegisterEvent event) {
        SITTABLE = register(event, "sittable", EntityType.Builder.<EntitySittable>of(EntitySittable::new, MobCategory.MISC).build(Metropolis.MOD_ID + ":sittable"));
    }

    private static <T extends Entity> EntityType<T> register(RegisterEvent event, String id, EntityType<T> type) {
        event.register(Registries.ENTITY_TYPE, new ResourceLocation(Metropolis.MOD_ID, id), () -> type);
        return type;
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Entities");
    }
}
