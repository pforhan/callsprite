package callsprite

import callsprite.Animation.Paused
import java.time.Duration

/** Set of images comprising a single object. */
data class Sprite(
  val frames: List<Frame>,
  val position: Position,
  val animation: Animation = Paused
)

sealed class Animation {
  object Forward: Animation()
  object Backward: Animation()
  object Paused: Animation()
}

data class Position(
  val x: Int,
  val y: Int
)
typealias Translation = Position

data class Frame(
  val name: String,
  val duration: Duration,
  val imageData: Int
)

data class Scene(
  val layers: List<BackgroundLayer>,
  val sprites: List<Sprite>
)

interface BackgroundLayer {
  val scrollMultiplier: Float
  val translation: Translation
  // probably some methods for doing stuff
  val imageData: Int
}

interface UI