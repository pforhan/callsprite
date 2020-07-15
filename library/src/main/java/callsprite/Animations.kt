package callsprite

import callsprite.TickAction.OnEndAction
import callsprite.TickAction.OnEndAction.Repeat

fun <T> load(
  animationName: String,
  loader: FrameLoader<T>,
  millis: Long = 150,
  onEndAction: OnEndAction = Repeat,
  names: List<String>
): Animation = Animation(
    name = animationName,
    frameMillis = millis,
    onEnd = onEndAction,
    frames = loader.load(names)
)