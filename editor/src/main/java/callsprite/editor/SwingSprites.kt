package callsprite.editor

import callsprite.Animation
import callsprite.Frame
import callsprite.FrameLoader
import callsprite.load
import javax.swing.ImageIcon

fun loadAnimationFromClasspath(
  animationName: String,
  fileNames: List<String>
): Animation<ImageIcon> {
  val loader = object : FrameLoader<ImageIcon> {
    override fun load(names: List<String>): List<Frame<ImageIcon>> = names
        .map { Frame(it, it.asClasspathImage()) }
  }
  return load(
      animationName = animationName,
      loader = loader,
      names = fileNames
  )
}

private fun String.asClasspathImage() = ImageIcon(SpritePanel::class.java.getResource(this))
