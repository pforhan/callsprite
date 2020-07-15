package callsprite

import callsprite.TickAction.OnEndAction.Switch

/**
 * Set of animations comprising a single object.
 */
data class Sprite(
  /** A sprite can have different animation sequences. */
  val animations: Map<String, Animation>,
  /** Current active animation. */
  var current: Animation
) {
  val frame: Frame
    get() = current.current

  fun tick(millis: Long) {
    val action = current.tick(millis)
    if (action is Switch<*>) {
      current = action.animation
      current.reset()
    }
  }

  fun set(name:String) {
    current = animations.getOrElse(name) {
      error("Could not find animation $name")
    }
  }
}
