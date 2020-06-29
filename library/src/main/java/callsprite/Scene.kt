package callsprite

data class Scene(
  val layers: List<BackgroundLayer>,
  val sprites: List<Sprite>
)

interface BackgroundLayer {
  val scrollMultiplier: Float
  val translation: Translation
  val imageRef: Int
}

data class Position(
  val x: Int,
  val y: Int
)
typealias Translation = Position