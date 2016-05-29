package graphics

import org.joml.Vector3f

class PointLight {

  Vector3f colour, position
  float intensity
  Attenuation att = new Attenuation(constant: 1, linear: 0, exponent: 0)

  PointLight clone() {
    new PointLight(colour: new Vector3f(colour), position: new Vector3f(position), intensity: intensity, att: att)
  }

  static class Attenuation {
    float constant, linear, exponent
  }
}
