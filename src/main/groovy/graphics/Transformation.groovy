package graphics

import core.GameItem
import org.joml.Matrix4f
/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class Transformation {

	private final Matrix4f projectionMatrix, modelViewMatrix, viewMatrix

	Transformation() {
		projectionMatrix = new Matrix4f()
		modelViewMatrix = new Matrix4f()
		viewMatrix = new Matrix4f()
	}

	Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height
		projectionMatrix.identity().perspective(fov, aspectRatio, zNear, zFar)
	}

	Matrix4f getModelViewMatrix(GameItem gi, Matrix4f viewMatrix) {
		modelViewMatrix
			.identity()
			.translate(gi.position)
			.rotateX(-gi.rotation.x)
			.rotateY(-gi.rotation.y)
			.rotateZ(-gi.rotation.z)
			.scale(gi.scale)
		new Matrix4f(viewMatrix).mul(modelViewMatrix)
	}

	Matrix4f getViewMatrix(Camera cam) {

		viewMatrix
			.identity()
			.rotateX(cam.rotation.x)
			.rotateY(cam.rotation.y)
//			.rotateZ(cam.rotation.z)
			.translate(-cam.position.x, -cam.position.y, -cam.position.z)
	}

}
