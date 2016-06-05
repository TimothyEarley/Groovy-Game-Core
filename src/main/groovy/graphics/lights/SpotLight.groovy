package graphics.lights

import org.joml.Vector3f
import groovy.transform.Canonical

@Canonical
class SpotLight {

   PointLight pointLight
   Vector3f direction
   float cutoff

   SpotLight clone() {
      new SpotLight(pointLight: pointLight.clone(), direction: new Vector3f(direction), cutoff: cutoff)
   }

   // In rad
   void setCutoffAngle(float angle) {
      cutoff = Math.cos(angle)
   }
}
