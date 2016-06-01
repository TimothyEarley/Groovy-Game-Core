package graphics.lights

import org.joml.Vector3f

class DirectionalLight {

  Vector3f colour, direction

  float intensity

  DirectionalLight clone() {
    new DirectionalLight (colour: new Vector3f(colour), direction: new Vector3f(direction), intensity: intensity)
  }

}
