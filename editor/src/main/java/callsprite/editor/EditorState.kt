package callsprite.editor

import callsprite.Animation
import callsprite.Sprite
import java.io.File

data class SaveState(
  val lastRoot: File
)

data class RuntimeState(
  var sprite: Sprite,
  var lastRoot: File? = null,
  var selectedAnim: Animation? = null
)