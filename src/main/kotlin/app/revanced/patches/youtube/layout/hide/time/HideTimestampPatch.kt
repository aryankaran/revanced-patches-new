package app.revanced.patches.youtube.layout.hide.time

import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.annotation.CompatiblePackage
import app.revanced.patcher.patch.annotation.Patch
import app.revanced.patches.all.misc.resources.AddResourcesPatch
import app.revanced.patches.shared.misc.settings.preference.SwitchPreference
import app.revanced.patches.youtube.layout.hide.time.fingerprints.TimeCounterFingerprint
import app.revanced.patches.youtube.misc.integrations.IntegrationsPatch
import app.revanced.patches.youtube.misc.settings.SettingsPatch
import app.revanced.util.exception

@Patch(
    name = "Hide timestamp",
    description = "Adds an option to hide the timestamp in the bottom left of the video player.",
    dependencies = [IntegrationsPatch::class, SettingsPatch::class, AddResourcesPatch::class],
    compatiblePackages = [
        CompatiblePackage(
            "com.google.android.youtube.tv", [
                "18.37.36",
                "18.38.44",
                "18.43.45",
                "18.44.41",
                "18.45.43",
                "18.48.39",
                "18.49.37",
                "19.01.34",
                "19.02.39",
                "19.03.35",
                "19.03.36",
                "19.04.37"
            ]
        )
    ]
)
@Suppress("unused")
object HideTimestampPatch : BytecodePatch(
    setOf(TimeCounterFingerprint)
) {
    override fun execute(context: BytecodeContext) {
        AddResourcesPatch(this::class)

        SettingsPatch.PreferenceScreen.LAYOUT.addPreferences(SwitchPreference("revanced_hide_timestamp"))

        TimeCounterFingerprint.result?.apply {
            mutableMethod.addInstructionsWithLabels(
            0,
            """
                invoke-static { }, Lapp/revanced/integrations/youtube/patches/HideTimestampPatch;->hideTimestamp()Z
                move-result v0
                if-eqz v0, :hide_time
                return-void
                :hide_time
                nop
            """
            )
        } ?: throw TimeCounterFingerprint.exception
    }
}
