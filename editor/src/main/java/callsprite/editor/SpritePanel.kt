package callsprite.editor

import callsprite.Sprite
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
      "/fire_column_medium_4.png",
      "/fire_column_medium_5.png",
      "/fire_column_medium_6.png",
      "/fire_column_medium_7.png",
      "/fire_column_medium_8.png",
      "/fire_column_medium_9.png"
//    "/r_fire_column_medium_4.png".asClasspathImage()
  )
  val animation = loadAnimationFromClasspath("fyre", iconList)
  private var theSprite = Sprite(
      animations = mapOf("center-flame" to animation),
      current = animation
  )

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

  fun tick(millis: Long) {
    theSprite.tick(millis)
    repaint()
  }

  override fun paintBorder(g: Graphics) {}
  override fun paintChildren(g: Graphics) {}
  public override fun paintComponent(g: Graphics) {
    if (heightPx == 0 || widthPx == 0) return
    g.fillRect(0, 0, widthPx, heightPx)

    // TODO this is ugly, using the statig swingLoader and its storage
    val image = swingloader.storage[theSprite.current.current]
    val iconWidth = image.iconWidth
    val iconHeight = image.iconHeight
    val iconAspect = iconWidth.toFloat() / iconHeight.toFloat()

    val newWidth: Int
    val newHeight: Int
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
    g.drawString("view $widthPx x $heightPx aspect:$viewAspect", 0, 15)
    g.drawString("icon $iconWidth x $iconHeight aspect:$iconAspect", 0, 30)
    g.drawString("targ $newWidth x $newHeight", 0, 45)
  }

  override fun getPreferredSize(): Dimension {
    return PREFERRED_SIZE
  }

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
