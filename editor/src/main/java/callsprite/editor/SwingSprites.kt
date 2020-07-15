package callsprite.editor

import callsprite.Animation
import callsprite.Frame
import callsprite.FrameLoader
import callsprite.load
import javax.swing.ImageIcon

fun loadAnimationFromClasspath(
  animationName: String,
  fileNames: List<String>
): Animation {
  return load(
      animationName = animationName,
      loader = swingloader,
      names = fileNames
  )
}

val swingloader = object : FrameLoader<ImageIcon>() {
  override fun load(name: String): ImageIcon = name.asClasspathImage()

}
private fun String.asClasspathImage() = ImageIcon(SpritePanel::class.java.getResource(this))
