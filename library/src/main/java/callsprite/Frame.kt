package callsprite

import callsprite.TickResult.Ended
import callsprite.TickResult.Ended.Repeat

/** Metadata about an animation frame. */
data class Frame(
  val name: String
)

/**
 * Loads image data from disk or other media by name and saves in its cache.
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

  fun createAnimation(
    animationName: String,
    millis: Long = 150,
    onEnd: Ended = Repeat,
    names: List<String>
  ): Animation = Animation(
      name = animationName,
      frameMillis = millis,
      onEnd = onEnd,
      frames = load(names)
  )
}

/** Caches frame data. */
// TODO bring closer to a map?
class FrameStorage<T> {
  private val storage: MutableMap<Frame, T> = mutableMapOf()

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
