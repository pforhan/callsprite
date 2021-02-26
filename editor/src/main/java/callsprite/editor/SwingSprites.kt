package callsprite.editor

import callsprite.Animation
import callsprite.Frame
import callsprite.FrameLoader
import javax.swing.ImageIcon

private val classpathSwingloader = object : FrameLoader<ImageIcon>() {
  override fun load(name: String): ImageIcon = name.asClasspathImage()
}

fun loadAnimationFromClasspath(
  animationName: String,
  fileNames: List<String>
): Animation {
  return classpathSwingloader.createAnimation(
      animationName = animationName,
      names = fileNames
  )
}

// TODO this is ugly, using the static swingLoader and its storage
fun getImage(frame: Frame) : ImageIcon = classpathSwingloader.storage[frame]

private fun String.asClasspathImage() = ImageIcon(SpritePanel::class.java.getResource(this))
