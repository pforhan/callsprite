package callsprite

import callsprite.Animation.Paused
import java.time.Duration

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
  // TODO is this a byte[]? And is that the best way to represent data anyway?
  // TODO --> maybe a type parameter instead? Or an index to a platform-specific resource
  val imageData: Array<Byte>
)

data class Scene(
  val background: List<Background>,
  val sprites: List<Sprite>
)

interface Background {
  val scrollMultiplier: Float
  val translation: Translation
  // probably some methods for doing stuff
}
