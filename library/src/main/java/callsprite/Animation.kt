package callsprite

import callsprite.TickAction.NoAction
import callsprite.TickAction.OnEndAction
import callsprite.TickAction.OnEndAction.Repeat
import callsprite.TickAction.OnEndAction.Stop
import callsprite.TickAction.OnEndAction.Switch

data class Animation(
  val name: String,
  val frames: List<Frame>,
  val frameMillis: Long,
  var onEnd: OnEndAction = Repeat
) {
  var paused = false
  private var currentMillis: Long = 0
  private val totalMillis: Long = frames.size * frameMillis
  val current: Frame
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
      val animation: Animation
    ) : OnEndAction()
  }
}

data class Frame(
  val name: String
)

// TODO by their nature these are disconnected from the other classes -- do they belong here?
// TODO Not sure I like the auto-caching here
/**
 * Loads image data from disk or other media.
 * @param T Type of image data stored
 */
abstract class FrameLoader<T> {
  val storage = FrameStorage<T>()

  abstract fun load(name: String): T

  fun load(names: List<String>): List<Frame> {
    val pairs = names.map { Frame(it) to load(it) }
    storage.add(pairs)
    return pairs.map { it.first }
  }
}

// TODO bring closer to a map?  Support Pair?
class FrameStorage<T> {
  val storage: MutableMap<Frame, T> = mutableMapOf()

  fun add(frames: List<Pair<Frame, T>>) {
    storage += frames
  }

  fun merge(other: FrameStorage<T>) {
    storage += other.storage
  }

  operator fun FrameStorage<T>.plusAssign(other: FrameStorage<T>) = merge(other)

  operator fun get(frame: Frame): T = storage.getOrElse(frame) {
    error("Could not find frame $frame")
  }
}
