package core

import org.joml.Matrix4f
import org.joml.Vector3f

/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class Transformation {

	private final Matrix4f projectionMatrix, worldMatrix

	Transformation() {
		projectionMatrix = new Matrix4f()
		worldMatrix = new Matrix4f()
	}

	Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height
		projectionMatrix.identity().perspective(fov, aspectRatio, zNear, zFar)
	}

	Matrix4f getWorldMatrix(Vector3f offset, Vector3f rotation, float scale) {
		worldMatrix
			.identity()
			.translate(offset)
			.rotateX((float) Math.toRadians(rotation.x))
			.rotateY((float) Math.toRadians(rotation.y))
			.rotateZ((float) Math.toRadians(rotation.z))
			.scale(scale)
	}

}
