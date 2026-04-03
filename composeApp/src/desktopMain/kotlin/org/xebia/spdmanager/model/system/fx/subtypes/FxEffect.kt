package org.xebia.spdmanager.model.system.fx.subtypes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.xebia.spdmanager.ui.components.fx.*

sealed class FxEffect {
    abstract val fxType: FXType
    abstract fun toParams(): List<Int>

    protected fun padTo20(params: List<Int>): List<Int> {
        return params + List(20 - params.size) { 0 }
    }

    companion object {
        fun fromValues(fxTypeInt: Int, params: List<Int>): FxEffect {
            require(params.isNotEmpty()) { "Parameter list must not be empty" }
            val fxType = FXType.fromValue(fxTypeInt)
            return when (fxType) {
                FXType.THRU -> Thru.fromValues(params)
                FXType.STEREO_DLY -> StereoDelay.fromValues(params)
                FXType.SYNC_DELAY -> SyncDelay.fromValues(params)
                FXType.TAPE_ECHO -> TapeEcho.fromValues(params)
                FXType.CHORUS -> Chorus.fromValues(params)
                FXType.FLANGER -> Flanger.fromValues(params)
                FXType.STEP_FLNGR -> StepFLNGR.fromValues(params)
                FXType.PHASER -> Phaser.fromValues(params)
                FXType.STEP_PHASR -> StepPHASR.fromValues(params)
                FXType.EQ -> EQ.fromValues(params)
                FXType.COMPRESSOR -> Compressor.fromValues(params)
                FXType.FILTER -> Filter.fromValues(params)
                FXType.FILT_DRIVE -> FiltDrive.fromValues(params)
                FXType.ISOLATOR -> Isolator.fromValues(params)
                FXType.TOUCH_WAH -> TouchWah.fromValues(params)
                FXType.DISTORTION -> Distortion.fromValues(params)
                FXType.RINGMOD -> RingMod.fromValues(params)
                FXType.PITCHSHIFT -> Pitchshift.fromValues(params)
                FXType.VIBRATO -> Vibrato.fromValues(params)
                FXType.REVERB -> Reverb.fromValues(params)
                FXType.SLICER -> Slicer.fromValues(params)
            }
        }
    }

    @Composable
    fun renderEditableParameters(onFxChange: (FxEffect) -> Unit) {
        when (this) {
            is Thru -> Text("Thru has no parameters.")
            is StereoDelay -> StereoDelayView(this, onFxChange)
            is SyncDelay -> SyncDelayView(this, onFxChange)
            is TapeEcho -> TapeEchoView(this, onFxChange)
            is Chorus -> ChorusView(this, onFxChange)
            is Flanger -> FlangerView(this, onFxChange)
            is StepFLNGR -> StepFlangerView(this, onFxChange)
            is Phaser -> PhaserView(this, onFxChange)
            is StepPHASR -> StepPhaserView(this, onFxChange)
            is EQ -> EqView(this, onFxChange)
            is Compressor -> CompressorView(this, onFxChange)
            is Filter -> FilterView(this, onFxChange)
            is FiltDrive -> FiltDriveView(this, onFxChange)
            is Isolator -> IsolatorView(this, onFxChange)
            is TouchWah -> TouchWahView(this, onFxChange)
            is Distortion -> DistortionView(this, onFxChange)
            is RingMod -> RingModView(this, onFxChange)
            is Pitchshift -> PitchshiftView(this, onFxChange)
            is Vibrato -> Text("Vibrato has no parameters.")
            is Reverb -> ReverbView(this, onFxChange)
            is Slicer -> SlicerView(this, onFxChange)
        }
    }
}