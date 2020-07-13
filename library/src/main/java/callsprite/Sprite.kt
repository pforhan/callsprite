package callsprite

import callsprite.TickAction.OnEndAction.Switch

/**
 * Set of animations comprising a single object.
 * @param T Type of image data stored
 */
data class Sprite<T>(
  /** A sprite can have different animation sequences. */
  val animations: Map<String, Animation<T>>,
  /** Current active animation. */
  var current: Animation<T>
) {
  val frame: Frame<T>
    get() = current.current

  fun tick(millis: Long) {
    val action = current.tick(millis)
    if (action is Switch<*>) {
      // TODO ugly, is there a way to have a non-erased T?
      current = action.animation as Animation<T>
      current.reset()
    }
  }

  fun set(name:String) {
    current = animations.getOrElse(name) {
      error("Could not find animation $name")
    }
  }
}
