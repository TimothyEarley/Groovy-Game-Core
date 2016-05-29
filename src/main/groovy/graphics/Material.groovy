package graphics

import org.joml.Vector3f

class Material {

  final static def DEFAULT_COLOUR = new Vector3f(1,1,1)

  Vector3f colour = DEFAULT_COLOUR
  float reflectance
  Texture texture

  boolean isTextured() {
    texture != null
  }

  void cleanup() {
    if (texture) texture.cleanup()
  }

}
