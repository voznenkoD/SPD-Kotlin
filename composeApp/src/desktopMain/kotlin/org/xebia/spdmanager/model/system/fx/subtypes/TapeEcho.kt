package org.xebia.spdmanager.model.system.fx.subtypes

data class TapeEcho(
    override val fxType: FXType = FXType.TAPE_ECHO,
    val mode: TapeEchoMode,
    val bass: Float,      // -15dB to +15dB
    val treble: Float,    // -15dB to +15dB
    val headSPan: Int,    // L64 (-64) to R63 (+63)
    val headMPan: Int,    // L64 (-64) to R63 (+63)
    val headLPan: Int,    // L64 (-64) to R63 (+63)
    val tapeDist: Int,    // 0..5
    val wfRate: Int,      // 0..127
    val wfDepth: Int,     // 0..127
    val echoLevel: Int,   // 0..100
    val directLevel: Int, // 0..100
    val level: Int        // 0..100
): FxEffect() {
    companion object {
        fun fromValues(params: List<Int>): TapeEcho {
            return TapeEcho(
                mode = TapeEchoMode.fromValue(params[0]),
                bass = mapBassTreble(params[1]),       // Convert 0..30 to -15dB..+15dB
                treble = mapBassTreble(params[2]),   // Convert 0..30 to -15dB..+15dB
                headSPan = mapPan(params[3]),      // Convert 0..127 to -64..+63
                headMPan = mapPan(params[4]),      // Convert 0..127 to -64..+63
                headLPan = mapPan(params[5]),      // Convert 0..127 to -64..+63
                tapeDist = params[6].coerceIn(0, 5),
                wfRate = params[7].coerceIn(0, 127),
                wfDepth = params[8].coerceIn(0, 127),
                echoLevel = params[9].coerceIn(0, 100),
                directLevel = params[10].coerceIn(0, 100),
                level = params[11].coerceIn(0, 100)
            )
        }

        private fun mapBassTreble(value: Int): Float {
            return (value - 15).toFloat()
        }

        private fun mapPan(value: Int): Int {
            return value - 64
        }
    }
}

enum class TapeEchoMode(val value: Int) {
    S(0),
    M(1),
    L(2),
    S_M(3),
    S_L(4),
    M_L(5),
    S_M_L(6);

    companion object {
        fun fromValue(value: Int): TapeEchoMode {
            return entries.find { it.value == value } ?: S
        }
    }
}
