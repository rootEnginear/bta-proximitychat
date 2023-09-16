package rootenginear.proximitychat.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.core.util.helper.AES;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = {ServerConfigurationManager.class}, remap = false)
public class ServerConfigurationManagerMixin {
    @Shadow
    public List<EntityPlayerMP> playerEntities;

    @Inject(method = "func_28171_a", at = @At("HEAD"), cancellable = true)
    public void sendEncryptedChatToPlayersAroundPoint(EntityPlayer entityplayer, double d, double d1, double d2, double d3, int i, Packet packet, CallbackInfo ci) {
        if (packet instanceof Packet3Chat) {
            for (EntityPlayerMP playerEntity : this.playerEntities) {
                double d6;
                double d5;
                double d4;
                if (playerEntity == entityplayer || playerEntity.dimension != i || !((d4 = d - playerEntity.x) * d4 + (d5 = d1 - playerEntity.y) * d5 + (d6 = d2 - playerEntity.z) * d6 < d3 * d3))
                    continue;
                playerEntity.playerNetServerHandler.sendPacket(new Packet3Chat(((Packet3Chat) packet).message, AES.keyChain.get(playerEntity.username)));
            }
            ci.cancel();
        }
    }
}
