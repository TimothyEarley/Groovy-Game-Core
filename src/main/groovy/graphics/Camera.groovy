package graphics

import org.joml.Vector3f
/**
 * Created 28/05/16
 * @author Timothy Earley
 */
class Camera {

	// rotation in radians

	Vector3f position = new Vector3f(), rotation = new Vector3f()

	void movePosition(Vector3f offset) {
		movePosition(offset.x, offset.y, offset.z)
	}

	void movePosition(float offsetX, float offsetY, float offsetZ) {

		if (offsetZ) {
			position.x -= Math.sin(rotation.y) * offsetZ
			position.z += Math.cos(rotation.y) * offsetZ
		}

		if (offsetX) {
			position.x -= Math.sin(rotation.y - Math.PI / 2) * offsetX
			position.y += Math.cos(rotation.y - Math.PI / 2) * offsetX
		}

		position.y += offsetY

	}

	void moveRotation(Vector3f rotation) {
		moveRotation(rotation.x, rotation.y, rotation.z)
	}

	void moveRotation(float x, float y, float  z) {
		rotation.add(x, y, z)
	}
}
