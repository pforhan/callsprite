package callsprite

import java.time.Duration

data class Sprite(
  val frames: List<SpriteFrame>,
  // TODO consider a position class
  val x: Int,
  val y: Int,
  val z: Int
)

data class SpriteFrame(
  val name: String,
  val duration: Duration,
  // TODO is this a byte[]? And is that the best way to represent data anyway?
  // TODO --> maybe a type parameter instead? Or an index to a platform-specific resource
  val imageData: Array<Byte>
)

// likely needed: container, data about collisions, state information, current frame, timer support