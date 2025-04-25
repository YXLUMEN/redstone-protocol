package lumen.redstone_protocol.entity_render;

import lumen.redstone_protocol.RedstoneProtocol;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EmptyEntityRender<T extends Entity> extends EntityRenderer<T> {
    public static final Identifier TEXTURE = Identifier.of(RedstoneProtocol.MOD_ID, "textures/item/null.png");

    public EmptyEntityRender(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(Entity entity) {
        return TEXTURE;
    }
}
