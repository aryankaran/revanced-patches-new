package app.revanced.patches.youtube.misc.playeroverlay

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.youtube.misc.integrations.IntegrationsPatch
import app.revanced.patches.youtube.misc.playeroverlay.fingerprint.PlayerOverlaysOnFinishInflateFingerprint

@Patch(
    description = "Hook for adding custom overlays to the video player.",
    dependencies = [IntegrationsPatch::class],
    compatiblePackages = [
        CompatiblePackage("com.google.android.youtube.tv", [
            "18.32.39"
        ])
    ]
)
@Suppress("unused")
object PlayerOverlaysHookPatch : BytecodePatch( // TODO: delete this unused outdated patch and its integration code.
    setOf(PlayerOverlaysOnFinishInflateFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        // hook YouTubePlayerOverlaysLayout.onFinishInflate()
        val method = PlayerOverlaysOnFinishInflateFingerprint.result!!.mutableMethod
        method.addInstruction(
            method.implementation!!.instructions.size - 2,
            "invoke-static { p0 }, Lapp/revanced/integrations/youtube/patches/PlayerOverlaysHookPatch;->YouTubePlayerOverlaysLayout_onFinishInflateHook(Ljava/lang/Object;)V"
        )
    }
}