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
 
Stretch goal of working wtih Spriter-type composite images, which let you animate body parts independently.

# Overall Concepts 

For this library we'll make a sprite a bit like the library's namesake, they'll track a lot of their 
own state, particularly around animations.  So we'll make Sprite the top-level object.

A Sprite contains a position and a number of animations. Animations contain a number of frames and a 
behavior (repeat, stop, transition). Frames can have join points that allow multiple sprites to 
connect to one another. 

# Implementation plan

General steps and milestones in roughly the order they should be tackled:

1. animating in place
1. Loading a set of images
1. sprite sheets
1. scaling 
1. custom origin points
1. fancy (semantic meaning from filename) file loading
1. sprite joining


## Namesake

TI Extended Basic added support for Sprites -- bitmapped, semi-autonomous pixel graphics with 
position, velocity, scaling, and collision detection.  A typical command could look like:

```
CALL SPRITE(#6, 108, 13, 80, 9, 90, 0)
```

(For the still-curious, that's sprite-number, character number, color, row, column, vertical velocity, horizontal velocity)

# Major Classes / unorganized thoughts 

Sprite is a visual representation. It has:
* one or more frames
* Location?
* animation run status, fwd, bwd, pause
** And/or anim speed multiplier
* Possibly other info like collision data though this could be on frames too
* Rotation/affine transform?
* joins of name to sprite
  * if some frames are missing join points, fail
  * ie, every frame should have the same number (and names) of join points

Frame class:
* data (maybe parameterized)
* duration
* maybe collision
* Maybe join points
* Transform?

Sprite join point
* name
* direction (vector)

Scene:
* background(s)
* sprites (implies sprites have locations)

Background
* data (should support tiled maps)
* transform/translate
* Should handle itself if larger than screen

Utilities
* loads tiled maps
* Loads sprite sheets
* Save/load sprite format 
* maybe power threading and timing with rx?

Editor
* preview anim
* Background with motion
* Magnification (default to fill space allotted)
* Nudging frames (origin editing)
* tweaking all params
* switch between sprites at same pos
* help loading a whole dir
  * ie show all imgs, and an anim of selected frames
  * try to help by filenames too... Common prefixes, number suffixes, etc

Samples
* find a couple free to use for non commercial
* android loader app

# Thoughts and questions:
pull in romainguy/kotlin-math if we need to do a lot of matrices

Location and velocity: do these belong in sprite?

Should we require frames in a frameset to be the same size?