import Renderer
import core.Game
import core.GameItem
import core.GamePart
import core.Window
import graphics.Mesh
import graphics.Texture
import org.lwjgl.glfw.GLFW

import java.awt.*

/**
 * Created 19/05/16
 * @author Timothy Earley
 */

Game game

def root = new GamePart() {

	def renderer = new Renderer()
	GameItem rect

	int direction = 0
	int red, green, blue

	@Override
	void initSelf(Window window) {
		renderer.init(window)
		float[] positions = [
				// V0
				-0.5f, 0.5f, 0.5f,
				// V1
				-0.5f, -0.5f, 0.5f,
				// V2
				0.5f, -0.5f, 0.5f,
				// V3
				0.5f, 0.5f, 0.5f,
				// V4
				-0.5f, 0.5f, -0.5f,
				// V5
				0.5f, 0.5f, -0.5f,
				// V6
				-0.5f, -0.5f, -0.5f,
				// V7
				0.5f, -0.5f, -0.5f,

				// For text coords in top face
				// V8: V4 repeated
				-0.5f, 0.5f, -0.5f,
				// V9: V5 repeated
				0.5f, 0.5f, -0.5f,
				// V10: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V11: V3 repeated
				0.5f, 0.5f, 0.5f,

				// For text coords in right face
				// V12: V3 repeated
				0.5f, 0.5f, 0.5f,
				// V13: V2 repeated
				0.5f, -0.5f, 0.5f,

				// For text coords in left face
				// V14: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V15: V1 repeated
				-0.5f, -0.5f, 0.5f,

				// For text coords in bottom face
				// V16: V6 repeated
				-0.5f, -0.5f, -0.5f,
				// V17: V7 repeated
				0.5f, -0.5f, -0.5f,
				// V18: V1 repeated
				-0.5f, -0.5f, 0.5f,
				// V19: V2 repeated
				0.5f, -0.5f, 0.5f,

		]
		int[] indices = [
			// Front face
			0, 1, 3, 3, 1, 2,
			// Top Face
			8, 10, 11, 9, 8, 11,
			// Right face
			12, 13, 7, 5, 12, 7,
			// Left face
			14, 15, 6, 4, 14, 6,
			// Bottom face
			16, 18, 19, 17, 16, 19,
			// Back face
			4, 6, 7, 5, 4, 7,
		]
		float[] texCoords = [
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.0f,

			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,

			// For text coords in top face
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 1.0f,
			0.5f, 1.0f,

			// For text coords in right face
			0.0f, 0.0f,
			0.0f, 0.5f,

			// For text coords in left face
			0.5f, 0.0f,
			0.5f, 0.5f,

			// For text coords in bottom face
			0.5f, 0.0f,
			1.0f, 0.0f,
			0.5f, 0.5f,
			1.0f, 0.5f,
		]
		def mesh = new Mesh(positions, texCoords, indices, new Texture("/cube_texture.png"))
		rect = new GameItem(mesh)
		rect.position.z = -2
	}

	@Override
	void stopSelf() {
		renderer.stop()
		rect.cleanup()
	}

	@Override
	void inputSelf(Window window) {
		if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) direction = 1
		else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) direction = -1
		else direction = 0
	}

	@Override
	void updateSelf(float delta) {
		direction *= delta * 2E2

		red += direction
		green -= direction
		blue += 2 * direction

		red = restrict red
		green = restrict green
		blue = restrict blue

		float amt = 1f * delta * 2E2
		rect.rotation.add(amt, amt, amt)

	}

	int restrict(number) {
		restrict(number, 0, 255)
	}

	int restrict(number, min, max) {
		number >= max ? max : (number <= min) ? min : number
	}

	@Override
	void renderSelf(Window window) {
		// only after resize
		window.clearColor = new Color(red, green, blue)
		renderer.render(window, [rect])
	}
}

game = new Game(root: root, ups: 120)

game.window.with {
	height = 480
	width = 800
	title = "Hello world"
	bgColor = new Color(0x05eec0) // nice bright turquoise
}

game.start()
