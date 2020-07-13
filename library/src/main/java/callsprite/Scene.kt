package callsprite

data class Scene<T>(
  val layers: List<BackgroundLayer>,
  val sprites: List<SpritePos<T>>
) {
  fun tick(millis: Long) {
    sprites
        .distinctBy { it.sprite }
        .onEach { it.sprite.tick(millis) }
  }
}

interface BackgroundLayer {
  val scrollMultiplier: Float
  val translation: Translation
  val imageRef: Int
}

data class SpritePos<T>(
  val sprite: Sprite<T>,
  val pos: Position
)

data class Position(
  val x: Int,
  val y: Int
)
typealias Translation = Position