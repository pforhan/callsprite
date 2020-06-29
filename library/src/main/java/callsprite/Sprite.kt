package callsprite

/** Set of images comprising a single object. */
data class Sprite(
  /** A sprite can have different animation sequences. */
  val animations: Map<String, Animation>,
  /** Current active animation name. */
  var current: String
) {
  fun tick(millis: Int) {
    TODO()
  }
}
