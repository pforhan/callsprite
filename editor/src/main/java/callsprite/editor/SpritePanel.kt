package callsprite.editor

import callsprite.UI
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.ImageIcon
import javax.swing.JPanel

// TODO should have a Sprite setter
class SpritePanel : JPanel(), UI {
  private var widthPx: Int = 0
  private var heightPx: Int = 0
  private var viewAspect: Float = 0f
  private val iconList = listOf(
      "/fire_column_medium_4.png".readFromClasspath(),
      "/fire_column_medium_5.png".readFromClasspath(),
      "/fire_column_medium_6.png".readFromClasspath(),
      "/fire_column_medium_7.png".readFromClasspath(),
      "/fire_column_medium_8.png".readFromClasspath(),
      "/fire_column_medium_9.png".readFromClasspath()
//    "/r_fire_column_medium_4.png".readFromClasspath()
  )
  private var current = 0

  init {
    isOpaque = true
    isFocusable = true
    addComponentListener(object : ComponentAdapter() {
      override fun componentResized(e: ComponentEvent) {
        widthPx = width
        heightPx = height
        viewAspect = widthPx.toFloat() / heightPx.toFloat()
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
    if (heightPx == 0 || widthPx == 0) return
    g.fillRect(0, 0, widthPx, heightPx)

    val image = iconList[current]
    val iconWidth = image.iconWidth
    val iconHeight = image.iconHeight
    val iconAspect = iconWidth.toFloat() / iconHeight.toFloat()

    var newWidth = 0
    var newHeight = 0
    if (iconAspect > viewAspect) {
      // icon is wider proportionally than view, so we match widths but allow height to be shorter.
      newWidth = widthPx
      // TODO optimize
      // TODO why divide here but multiply below? -- has to be related to how we calculated aspects?
      newHeight = (newWidth / iconAspect).toInt()
    } else {
      // icon is taller proportionally than view, so match heights but allow width to be smaller.
      newHeight = heightPx
      newWidth = (newHeight * iconAspect).toInt()
    }

    g.drawImage(
        image.image, 0, 0, newWidth, newHeight, null
    )

    g.color = Color.WHITE
    g.drawString("vw $widthPx x $heightPx a-$viewAspect", 0, 15)
    g.drawString("ic $iconWidth x $iconHeight a-$iconAspect", 0, 30)
    g.drawString("targ $newWidth x $newHeight", 0, 45)
  }

  override fun getPreferredSize(): Dimension {
    return PREFERRED_SIZE
  }

  private fun String.readFromClasspath() = ImageIcon(this::class.java.getResource(this))

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
