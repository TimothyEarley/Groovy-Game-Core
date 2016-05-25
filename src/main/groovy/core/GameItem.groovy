package core

import graphics.Mesh
import org.joml.Vector3f

/**
 * Created 25/05/16
 * @author Timothy Earley
 */
class GameItem {

	//TODO integrate into ECS

	final Mesh mesh
	final Vector3f position
	final Vector3f rotation

	float scale

	GameItem(Mesh mesh) {

		this.mesh = mesh
		position = new Vector3f()
		rotation = new Vector3f()
		scale = 1
	}

	void cleanup() {
		mesh.cleanup()
	}
}
