package org.xebia.spdmanager.model.system.fx.subtypes

sealed class FxEffect {
    abstract val fxType: FXType

    companion object {
        /**
         * Converts a universal list of 20 Int parameters into the corresponding FxEffect.
         * The first parameter indicates the FX type.
         */
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
}