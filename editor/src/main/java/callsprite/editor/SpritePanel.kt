package callsprite.editor

import callsprite.UI
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.net.URL
import javax.swing.ImageIcon
import javax.swing.JPanel
import kotlin.math.min

// TODO should have a Sprite setter
class SpritePanel : JPanel(), UI {
  private var widthPx: Int = 0
  private var heightPx: Int = 0
  private var showPixelSize: Int = 0
  private val iconList = listOf(
      "/fire_column_medium_4.png".asIcon(),
      "/fire_column_medium_5.png".asIcon(),
      "/fire_column_medium_6.png".asIcon(),
      "/fire_column_medium_7.png".asIcon(),
      "/fire_column_medium_8.png".asIcon(),
      "/fire_column_medium_9.png".asIcon()
  )
  private var current = 0

  init {
    isOpaque = true
    isFocusable = true
    addComponentListener(object : ComponentAdapter() {
      override fun componentResized(e: ComponentEvent) {
        widthPx = width
        heightPx = height
        val shortest = min(widthPx, heightPx)
        val scale =
          shortest.toFloat() / PREFERRED_SIZE.height.toFloat()
        showPixelSize = (DEFAULT_PIXEL_SIZE * scale).toInt()
        if (showPixelSize < 1) showPixelSize = 1
      }
    })
  }

  fun tick() {
    // TODO need timing info or something
    current++
    if (current >= iconList.size) {
      current = 0
    }
    repaint()
  }

  override fun paintBorder(g: Graphics) {}
  override fun paintChildren(g: Graphics) {}
  public override fun paintComponent(g: Graphics) {
    g.fillRect(0, 0, widthPx, heightPx)
    val imageIcon = iconList[current]
//    println("icon is ${imageIcon.iconWidth}x${imageIcon.iconHeight}")
    // TODO scaling is wrong here, need to keep aspect ratio of original:
    g.drawImage(imageIcon.image, 0, 0, widthPx, heightPx, null)
  }

  override fun getPreferredSize(): Dimension {
    return PREFERRED_SIZE
  }

  private fun String.asIcon() = ImageIcon(this::class.java.getResource(this))

  companion object {
    const val DEFAULT_PIXEL_SIZE = 20
    private const val MIN_PLAYFIELD_HEIGHT = 10
    private const val MIN_PLAYFIELD_WIDTH = 10
    private val PREFERRED_SIZE = Dimension(
        DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_WIDTH,
        DEFAULT_PIXEL_SIZE * MIN_PLAYFIELD_HEIGHT
    )
  }
}
