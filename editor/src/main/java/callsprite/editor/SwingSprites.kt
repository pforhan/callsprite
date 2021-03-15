package callsprite.editor

import callsprite.Animation
import callsprite.Frame
import callsprite.FrameLoader
import java.io.File
import javax.swing.ImageIcon

private val classpathSwingloader = object : FrameLoader<String, ImageIcon>() {
  override fun load(key: String): Pair<String, ImageIcon> = key to key.asClasspathImage()
}

private val fileSwingLoader = object : FrameLoader<File, ImageIcon>() {
  override fun load(key: File): Pair<String, ImageIcon> = key.name to key.asImage()
}

fun loadAnimationFromClasspath(
  animationName: String,
  fileNames: List<String>
): Animation {
  return classpathSwingloader.createAnimation(
      animationName = animationName,
      keys = fileNames
  )
}

// TODO this is ugly, using the static loaders and their storage.
fun getImage(frame: Frame): ImageIcon {
  return classpathSwingloader.storage.getOrNull(frame) ?: fileSwingLoader.storage[frame]
}

private fun String.asClasspathImage() = ImageIcon(SpritePanel::class.java.getResource(this))
private fun File.asImage() = ImageIcon(
    this.toURI()
        .toURL()
)

fun loadAnimationFromFiles(
  baseDir: File,
  baseName: String,
  millis: Long = 150
): Animation {
  val files = findFilesByName(baseDir, baseName)
  return createFromFiles(baseName, files, millis)
}

private fun createFromFiles(
  baseName: String,
  files: List<File>,
  millis: Long
) = fileSwingLoader.createAnimation(
    animationName = baseName,
    keys = files,
    millis = millis
)

private fun findFilesByName(
  baseDir: File,
  baseName: String
): List<File> = baseDir
    .walk()
    .filter { it.name.startsWith(baseName) && it.isFile}
    .sortedBy { it.name }
    .toList()

/**
 * Attempts to load animations semantically, looking for files prefixed with
 * the name of the folder they're in.
 * TODO ideally we'd grab all filenames right in one pass instead of finding all directories
 * TODO then finding their files, again.
 */
fun loadManyAnimationsFromFiles(
  baseDir: File,
  millis: Long = 150
): Map<String, Animation> = baseDir
    .walk()
    .filter { it.isDirectory }
    .map { NamedFiles(
        name = it.name,
        files = findFilesByName(it, it.name))
    }
    // filter out any dirs that have no files with matching names
    .filter { it.files.size > 0 }
    .map {
      it.name to createFromFiles(
          baseName = it.name,
          millis = millis,
          files = it.files
      )
    }
    .toMap()

private data class NamedFiles(
  val name: String,
  val files: List<File>
)
