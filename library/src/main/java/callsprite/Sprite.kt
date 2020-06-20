package callsprite

import callsprite.Animation.Paused
import java.time.Duration

/** Set of images comprising a single object. */
data class Sprite(
  val position: Position,
  /** A sprite can have different animation sequences. */
  val frameSets: Map<String, FrameSet>,
  var currentSet: String
)

data class FrameSet(
  val frames: List<Frame>,
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
  val imageRef: Int
)

data class Scene(
  val layers: List<BackgroundLayer>,
  val sprites: List<Sprite>
)

interface BackgroundLayer {
  val scrollMultiplier: Float
  val translation: Translation
  val imageRef: Int
}

// TODO platform-specific operations
interface UI