package callsprite.editor

import callsprite.Sprite
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JPanel

/** All the glue to make a sample swing Wannabe application.  */
object CallspriteEditor {
  @Throws(InterruptedException::class) @JvmStatic
  fun main(args: Array<String>) {
    val frame = JFrame("Callsprite")
    frame.setLocation(20, 30)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val mainLayout = JPanel(BorderLayout())
    frame.contentPane = mainLayout

    val iconList = listOf(
        "/fire_column_medium_4.png",
        "/fire_column_medium_5.png",
        "/fire_column_medium_6.png",
        "/fire_column_medium_7.png",
        "/fire_column_medium_8.png",
        "/fire_column_medium_9.png"
//    "/r_fire_column_medium_4.png".asClasspathImage()
    )
    val animation = loadAnimationFromClasspath("fyre", iconList)
    val theSprite = Sprite(
        animations = mapOf("center-flame" to animation),
        current = animation
    )

    val spritePanel = SpritePanel(theSprite)
    mainLayout.add(spritePanel)

    frame.isVisible = true
    frame.size = mainLayout.preferredSize
    frame.background = Color.BLACK

    var lastCall = System.currentTimeMillis()
    // noinspection InfiniteLoopStatement
    while (true) {
      // TODO if the computer sleeps while this runs we seem to have overflows
      Thread.sleep(49)
      val now = System.currentTimeMillis()
      val diff = now - lastCall
      lastCall = now
      spritePanel.tick(diff)
    }
  }
}
