package dev.fooduhhh.craft_team.common.mixin;

import dev.fooduhhh.craft_team.common.config.CraftTeamConfig;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "canHarmPlayer", at = @At("RETURN"), cancellable = true)
    private void craftTeam$canHarmPlayer(Player other, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            Player attacker = (Player) (Object) this;
            boolean bothPVP = other.getPersistentData().getBoolean("teamPvP") || attacker.getPersistentData().getBoolean("teamPvP");
            boolean sameTeam = attacker.getPersistentData().getString("teamColor").equals(other.getPersistentData().getString("teamColor"));
            if (CraftTeamConfig.allowDamageSelf.get()) {
                cir.setReturnValue(bothPVP);
            } else {
                cir.setReturnValue(!sameTeam && bothPVP);
            }
        }
    }
}
