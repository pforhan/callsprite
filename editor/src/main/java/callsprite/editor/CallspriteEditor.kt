package callsprite.editor

import callsprite.Sprite
import callsprite.TickResult.Ended.Switch
import java.awt.BorderLayout
import java.awt.Color
import java.awt.GridLayout
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

/** All the glue to make a sample swing Wannabe application.  */
object CallspriteEditor {
  val state: RuntimeState = RuntimeState(
      lastRoot = loadLastRoot(),
      sprite = fire() // TODO if we have a last loaded then use it here
  )
  @Throws(InterruptedException::class) @JvmStatic
  fun main(args: Array<String>) {
    val frame = JFrame("Callsprite")
    frame.setLocation(20, 30)
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    val mainLayout = JPanel(BorderLayout())
    frame.contentPane = mainLayout

    val spritePanel = SpritePanel(state)
    mainLayout.add(spritePanel)
    mainLayout.add(createButtonPanel(), BorderLayout.EAST)

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

  private fun createButtonPanel(): JPanel {
    val loaded = JLabel()
    val loadTree = JButton("Load Tree")
    loadTree.addActionListener {
      val chooser = JFileChooser(state.lastRoot)
      chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
      val dialogResult = chooser.showOpenDialog(chooser)
      if (dialogResult == JFileChooser.APPROVE_OPTION) {
        val baseDir = chooser.selectedFile
        state.lastRoot = baseDir
        state.sprite = everyAnim(baseDir)
        loaded.text = baseDir.name
        saveLastRoot(baseDir)
      }
    }

    val panel = JPanel(GridLayout(5,1))
    panel.add(loaded)
    panel.add(loadTree)
    // TODO add animation list here
    return panel
  }

  private fun loadLastRoot(): File? {
    val config = File(System.getProperty("user.dir"), ".callsprite")
    // TODO this should be much more robust
    return if (config.exists()) File(config.readLines().first()) else null
  }

  private fun saveLastRoot(root: File) {
    val config = File(System.getProperty("user.dir"), ".callsprite")
    config.writeText(root.path)
  }

  private fun everyAnim(baseDir: File): Sprite {
    val animMap = loadManyAnimationsFromFiles(
        baseDir = baseDir,
        millis = 55
    )

    val allAnims = animMap
        .toList()
        .map { it.second }

     allAnims.forEachIndexed { index, animation ->
       val next: Int = if (index + 1 == allAnims.size) 0 else index + 1
       animation.onEnd = Switch(allAnims[next])
     }
    return Sprite(
        animations = animMap,
        current = allAnims.first()
    )
  }

  private fun fire(): Sprite {
    val iconList = listOf(
        "/fire_column_medium_4.png",
        "/fire_column_medium_5.png",
        "/fire_column_medium_6.png",
        "/fire_column_medium_7.png",
        "/fire_column_medium_8.png",
        "/fire_column_medium_9.png"
    )
    val animation = loadAnimationFromClasspath("fyre", iconList)
    return Sprite(
        animations = mapOf("center-flame" to animation),
        current = animation
    )
  }
}
