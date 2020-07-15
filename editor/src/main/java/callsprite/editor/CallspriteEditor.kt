package callsprite.editor

import java.awt.BorderLayout
import java.time.Clock
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

    val spritePanel = SpritePanel()
    mainLayout.add(spritePanel)

    frame.isVisible = true
    frame.size = mainLayout.preferredSize

    var lastCall = System.currentTimeMillis()
    // noinspection InfiniteLoopStatement
    while (true) {
      Thread.sleep(49)
      val now = System.currentTimeMillis()
      val diff = now - lastCall
      lastCall = now
      // TODO we're not on the awt event queue so multithreaded access here leads to occasional crashes
      spritePanel.tick(diff)
    }
  }
}
