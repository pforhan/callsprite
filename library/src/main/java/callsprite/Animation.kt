package callsprite

import callsprite.AnimationStatus.Paused
import java.time.Duration

data class Animation(
  val name: String,
  val frames: List<Frame>,
  val status: AnimationStatus = Paused,
  val onEnd: OnEndAction
)

data class Frame(
  val name: String,
  val duration: Duration,
  val imageRef: Int
)

sealed class OnEndAction {
  object Stop : OnEndAction()
  object Repeat : OnEndAction()
  object Switch : OnEndAction()
}

sealed class AnimationStatus {
  object Forward : AnimationStatus()
  object Backward : AnimationStatus()
  object Paused : AnimationStatus()
}