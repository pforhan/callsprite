package callsprite

import callsprite.TickResult.Ended.Switch

/**
 * Set of animations for one object, along with connections to other
 * sprites.
 */
data class Sprite(
  /** A sprite can have different animation sequences. */
  val animations: Map<String, Animation>,
  /** Current active animation. */
  var current: Animation,
  /** Associated sprites. */
  var connections: List<SpriteConnection>
) {
  val frame: Frame
    get() = current.current

  fun tick(millis: Long) {
    val action = current.tick(millis)
    if (action is Switch) {
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

data class SpriteConnection(
  var transform: Transformation,
  var other: Sprite
)

// TODO a real matrix class would be nice
// TODO this should also receive a tick()
data class Transformation(
  val x: Int,
  val y: Int,
  // TODO: Degrees?
  val rotation: Int
)