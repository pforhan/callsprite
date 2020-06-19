# callsprite
Sprite loader, animator, editor in kotlin

Not the first, not the best, just a thing.

# General goals
Primarily intended for frame-based pixel sprites, such as those shown [over here](https://sanctumpixel.itch.io/fire-column-pixel-art-effect).
 
Stretch goal of working wtih Spriter-type composite images.

# Implementation plan

General steps and milestones in roughly the order they should be tackled:

1. Loading a set of images
1. animating in place
1. scaling 
1. custom origin points
1. sprite joining
1. fancy file loading

# Major Classes

Sprite is a visual representation. It has:
* one or more frames
* Location?
* animation run status, fwd, bwd, pause
** And/or anim speed multiplier
* Possibly other info like collision data though this could be on frames too
* Rotation/affine transform?
* joins of name to sprite
** if some frames are missing join points, fail
** ie, every frame should have the same number (and names) of join points

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
** ie show all imgs, and an anim of selected frames
** try to help by filenames too... Common prefixes, number suffixes, etc

Samples
* find a couple free to use for non commercial
* android loader app

# Thoughts and questions:
pull in romainguy/kotlin-math

Location and velocity: do these belong in sprite?

Theres likely to be a higher level object which supports swapping sprites, ie between standing, walking, running -- may be a per impl thing but the editor would need one anyway


Load individual images as sprite frames and play the resulting animation.