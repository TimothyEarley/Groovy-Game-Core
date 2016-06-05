package graphics.lights

import org.joml.Vector3f
import groovy.transform.Canonical

@Canonical
class PointLight {

  Vector3f colour, position
  float intensity
  Attenuation att = new Attenuation(constant: 1, linear: 0, exponent: 0)

  PointLight clone() {
    //TODO decide if Attenuation should be cloned as well, or at least add warning
    new PointLight(colour: new Vector3f(colour), position: new Vector3f(position), intensity: intensity, att: att)
  }

  @Canonical
  static class Attenuation {
    float constant, linear, exponent
  }
}
