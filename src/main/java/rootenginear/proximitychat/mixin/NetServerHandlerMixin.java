package rootenginear.proximitychat.mixin;

import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Pattern;

@Mixin(value = {NetServerHandler.class}, remap = false)
public class NetServerHandlerMixin {
    @Shadow
    private EntityPlayerMP playerEntity;

    @Redirect(
            method = "handleChat(Lnet/minecraft/core/net/packet/Packet3Chat;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/net/ServerConfigurationManager;sendEncryptedChatToAllPlayers(Ljava/lang/String;)V"
            )
    )
    private void proximityChat(ServerConfigurationManager instance, String s) {
        Pattern globalChatPattern = Pattern.compile("<.+?> §0#");
        if (globalChatPattern.matcher(s).find()) {
            instance.sendEncryptedChatToAllPlayers(s.replace("> §0#", "> (Global) §0"));
            return;
        }
        instance.sendPacketToPlayersAroundPoint(
                this.playerEntity.x,
                this.playerEntity.y,
                this.playerEntity.z,
                50.0,
                this.playerEntity.dimension,
                new Packet3Chat(s)
        );
    }
}
