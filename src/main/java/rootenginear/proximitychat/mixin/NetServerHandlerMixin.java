package rootenginear.proximitychat.mixin;

import net.minecraft.core.net.packet.Packet3Chat;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.ServerConfigurationManager;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rootenginear.proximitychat.ProximityChat;

import java.util.regex.Matcher;
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
        if (ProximityChat.RADIUS > 0) {
            Pattern globalChatPattern = Pattern.compile("<(.+?)> §0" + ProximityChat.GLOBAL_TAG + "(.+)");
            Matcher result = globalChatPattern.matcher(s);
            if (result.matches()) {
                instance.sendEncryptedChatToAllPlayers(ProximityChat.GLOBAL_STR.replace("{PLAYER}", result.group(1)).replace("{MSG}", result.group(2)));
                return;
            }
            instance.sendPacketToPlayersAroundPoint(
                    this.playerEntity.x,
                    this.playerEntity.y,
                    this.playerEntity.z,
                    ProximityChat.RADIUS,
                    this.playerEntity.dimension,
                    new Packet3Chat(s)
            );
            return;
        }
        instance.sendEncryptedChatToAllPlayers(s);
    }
}
