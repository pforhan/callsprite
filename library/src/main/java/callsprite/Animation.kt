package callsprite

import callsprite.TickAction.NoAction
import callsprite.TickAction.OnEndAction
import callsprite.TickAction.OnEndAction.Repeat
import callsprite.TickAction.OnEndAction.Stop
import callsprite.TickAction.OnEndAction.Switch

data class Animation<T>(
  val name: String,
  val frames: List<Frame<T>>,
  val frameMillis: Long,
  var onEnd: OnEndAction = Repeat
) {
  var paused = false
  private var currentMillis: Long = 0
  private val totalMillis: Long = frames.size * frameMillis
  val current: Frame<T>
    get() = frames[(currentMillis / frameMillis).toInt()]

  fun tick(millis: Long): TickAction {
    if (paused) return NoAction;

    currentMillis += millis
    if (currentMillis > totalMillis) {
      when (onEnd) {
        Stop -> paused = true
        Repeat -> currentMillis -= totalMillis
        is Switch<*> -> paused = true
      }
      return onEnd
    }
    return NoAction
  }

  fun reset() {
    currentMillis = 0
  }
}

// TODO would these be better separate from Animation?
sealed class TickAction {
  // TODO seems like a bad idea, but it keeps me from a null
  // Little strange that folks will interact with OnEndAction and not TickAction generally.
  object NoAction : TickAction()
  sealed class OnEndAction : TickAction() {
    object Stop : OnEndAction()
    object Repeat : OnEndAction()
    data class Switch<T>(
        // TODO maybe this should be a string name
      val animation: Animation<T>
    ) : OnEndAction()
  }
}

data class Frame<T>(
  val name: String,
  val data: T
)

interface FrameLoader<T> {
  /** Load image data from from disk or other medium */
  fun load(names: List<String>): List<Frame<T>>
}

// TODO determine if this class is useful
class FrameStorage<T> {
  val storage: MutableMap<String, Frame<T>> = mutableMapOf()

  fun add(frames: List<Frame<T>>) {
    // TODO should I just use the frame itself as the key?
    storage += frames.map { it.name to it }
  }

  fun merge(other: FrameStorage<T>) {
    storage += other.storage
  }

  operator fun FrameStorage<T>.plusAssign(other: FrameStorage<T>) = merge(other)
}
