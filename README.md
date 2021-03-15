# callsprite
Sprite loader, animator, editor in kotlin

Not the first, not the best, just a thing.

# General goals
Primarily intended for frame-based pixel sprites (aka Pixel Art), such as these, courtesy 
[sanctumpixel](https://sanctumpixel.itch.io/fire-column-pixel-art-effect).

![frame4](https://github.com/pforhan/callsprite/raw/main/editor/src/main/resources/fire_column_medium_4.png)
![frame5](https://github.com/pforhan/callsprite/raw/main/editor/src/main/resources/fire_column_medium_5.png)
![frame6](https://github.com/pforhan/callsprite/raw/main/editor/src/main/resources/fire_column_medium_6.png)
![frame7](https://github.com/pforhan/callsprite/raw/main/editor/src/main/resources/fire_column_medium_7.png)
![frame8](https://github.com/pforhan/callsprite/raw/main/editor/src/main/resources/fire_column_medium_8.png)
![frame9](https://github.com/pforhan/callsprite/raw/main/editor/src/main/resources/fire_column_medium_9.png)

...which the tool can put together roughly like this:

![anim](https://github.com/pforhan/callsprite/raw/main/site/100ms-anim.gif)

or more complicated animations like these:

[![animation set](https://img.youtube.com/vi/2O4BTYVthDU/0.jpg)](https://www.youtube.com/watch?v=2O4BTYVthDU)

# Overall Concepts 

For this library we'll make a sprite a bit like the library's [namesake](#namesake), they'll track a
lot of their own state, particularly around animations.  So we'll make Sprite the top-level object.

A Sprite contains a number of animations. Animations contain a number of frames and a 
behavior (repeat, stop, transition). Frames can have join points that allow multiple sprites to 
connect to one another. 

## Guiding Principles

Two primary things we want to keep in mind:
* Keep all runtime code as simple as possible.  Loaders and utilities will be more complex, but the 
resulting Animation will be simple.
* This is a view layer that handles some logic but not much.  Animations, affine transforms, sure, 
but concepts like jumping and gravity, no.  Such should be in a model layer.

For example, if we want to run an animation in reverse, there should be a utility method to do so, 
and it will produce a new animation object.  But the two animations will always proceed forward in 
time in a nod to simplicity. 

Likewise, if we want one frame to last longer than another, just repeat the frame rather than deal
with differing frame lengths.

## Namesake

TI Extended Basic added support for Sprites -- bitmapped, semi-autonomous pixel graphics with 
position, velocity, scaling, and collision detection.  A typical command could look like:

```
CALL SPRITE(#6, 108, 13, 80, 9, 90, 0)
```

(For the still-curious, that's sprite-number, character number, color, row, column, vertical 
velocity, horizontal velocity)

# Thoughts and questions:
pull in romainguy/kotlin-math if we need to do a lot of matrices

Should we require frames in an Animation to be the same size?