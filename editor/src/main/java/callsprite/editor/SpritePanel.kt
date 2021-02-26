package callsprite.editor

import callsprite.Sprite
import callsprite.UI
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JPanel

class SpritePanel(
  private val theSprite: Sprite
) : JPanel(), UI {
  private var widthPx: Int = 0
  private var heightPx: Int = 0
  private var viewAspect: Float = 0f

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

  override fun paintBorder(g: Graphics) = Unit
  override fun paintChildren(g: Graphics) = Unit

  public override fun paintComponent(g: Graphics) {
    if (heightPx == 0 || widthPx == 0) return
    g.color = Color.BLACK
    g.fillRect(0, 0, widthPx, heightPx)

    val image = getImage(theSprite.current.current)
    val iconWidth = image.iconWidth
    val iconHeight = image.iconHeight
    val iconAspect = iconWidth.toFloat() / iconHeight.toFloat()

    val newWidth: Int
    val newHeight: Int
    if (iconAspect > viewAspect) {
      // Icon is wider proportionally than view, so we match widths but allow height to be shorter.
      newWidth = widthPx
      // TODO optimize
      // TODO why divide here but multiply below? -- has to be related to how we calculated aspects?
      newHeight = (newWidth / iconAspect).toInt()
    } else {
      // Icon is taller proportionally than view, so match heights but allow width to be smaller.
      newHeight = heightPx
      newWidth = (newHeight * iconAspect).toInt()
    }

    // TODO probably abstract a per-sprite draw mechanism.
    g.drawSprite(theSprite)

    g.color = Color.WHITE
    g.drawRect(0, 0, newWidth, newHeight)
    g.drawString("view $widthPx x $heightPx aspect:$viewAspect", 0, 15)
    g.drawString("icon $iconWidth x $iconHeight aspect:$iconAspect", 0, 30)
    g.drawString("targ $newWidth x $newHeight", 0, 45)
  }

  private fun Graphics.drawSprite(sprite: Sprite) {
    // Draw the sprite first, then its connected sprites
    drawImage(
        image.image, 0, 0, newWidth, newHeight, null
    )
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
