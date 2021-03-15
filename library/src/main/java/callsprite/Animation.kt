package callsprite

import callsprite.TickResult.DidntEnd
import callsprite.TickResult.Ended
import callsprite.TickResult.Ended.Repeat
import callsprite.TickResult.Ended.Stop
import callsprite.TickResult.Ended.Switch

/**
 * An Animation is a list of frames with a fixed time spent on each frame.
 * @param onEnd what to do at the end of the animation
 */
data class Animation(
  val name: String,
  val frames: List<Frame>,
  val frameMillis: Long,
  var onEnd: Ended = Repeat,
  var paused: Boolean = false
) {
  val current: Frame
    get() = frames[(currentMillis / frameMillis).toInt()]

  private var currentMillis: Long = 0
  private val totalMillis: Long = frames.size * frameMillis

  /** Advance the clock by millis. */
  fun tick(millis: Long): TickResult {
    if (paused) return DidntEnd;

    currentMillis += millis
    if (currentMillis >= totalMillis) {
      when (onEnd) {
        Stop -> paused = true
        Repeat -> currentMillis -= totalMillis
        is Switch -> Unit
      }
      return onEnd
    }
    return DidntEnd
  }

  fun reset() {
    currentMillis = 0
  }
}

/**
 * Lets the engine know what happened during the [Animation.tick] call.
 */
sealed class TickResult {
  // Little strange that folks will interact with OnEndAction and not TickResult generally.
  object DidntEnd : TickResult()
  sealed class Ended : TickResult() {
    object Stop : Ended()
    object Repeat : Ended()
    data class Switch(
      // TODO maybe this should be a string name
      val animation: Animation
    ) : Ended()
  }
}
