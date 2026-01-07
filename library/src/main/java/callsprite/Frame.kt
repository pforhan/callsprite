package callsprite

import callsprite.TickResult.Ended
import callsprite.TickResult.Ended.Repeat

/** Metadata about an animation frame. */
data class Frame(
  val name: String
)

/**
 * Loads image data from disk or other media by name and saves in its cache.
 * @param K Key to load by
 * @param T Type of image data stored
 */
abstract class FrameLoader<K, T> {
  val storage = FrameStorage<T>()

  /** Creates a Pair of the string name of the frame and the image data. */
  abstract fun load(key: K): Pair<String, T>

  private fun load(keys: List<K>): List<Frame> {
    val pairs = keys
        .map { val pair = load(it)
          Frame(pair.first) to pair.second
        }

    storage.add(pairs)
    return pairs.map { it.first }
  }

  fun createAnimation(
    animationName: String,
    millis: Long = 150,
    onEnd: Ended = Repeat,
    keys: List<K>
  ): Animation = Animation(
      name = animationName,
      frameMillis = millis,
      onEnd = onEnd,
      frames = load(keys)
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

  fun has(frame: Frame) = storage.containsKey(frame)

  operator fun get(frame: Frame): T = storage.getOrElse(frame) {
    error("Could not find frame $frame")
  }

  fun getOrNull(frame: Frame): T? = if (storage.contains(frame)) storage[frame] else null
}
