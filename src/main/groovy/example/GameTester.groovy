package example

import core.*
import graphics.*
import graphics.obj.OBJLoader
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW

import java.awt.Color

/**
 * Created 19/05/16
 * @author Timothy Earley
 */

Game game

def root = new GamePart() {

	Camera camera = new Camera()

	def renderer = new Renderer()
	GameItem rect

	int direction = 0
	int red, green, blue

	Vector3f camInc = new Vector3f()
	Vector2f camRot = new Vector2f()

	Vector3f ambientLight
	PointLight pointLight

	@Override
	void initSelf(Window window) {
		renderer.init(window)
		// Mesh mesh = OBJLoader.loadMesh('/bunny.obj')
		def mesh = OBJLoader.loadMesh('/cube.obj')
		def texture = new Texture('/cube_texture.png')
		mesh.material = new Material(texture: texture, reflectance: 1f)
		rect = new GameItem(mesh)
		rect.position.set (0, 0, -2)

		ambientLight = new Vector3f(0.3, 0.3, 0.3)

		Vector3f lightColour = new Vector3f(1,1,1)
		Vector3f lightPosition = new Vector3f(0,0,1)

		pointLight = new PointLight(colour: lightColour, position: lightPosition, intensity: 1f)
	}

	@Override
	void stopSelf() {
		renderer.stop()
		rect.cleanup()
	}

	@Override
	void inputSelf(Window window, MouseInput mouseInput) {
		if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) direction = 1
		else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) direction = -1
		else direction = 0

		if (window.isKeyPressed(GLFW.GLFW_KEY_W)) camInc.z = -1
		else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) camInc.z = 1
		else camInc.z = 0


		if (window.isKeyPressed(GLFW.GLFW_KEY_A)) camInc.x = -1
		else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) camInc.x = 1
		else camInc.x = 0

		if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) camInc.y = -1
		else if (window.isKeyPressed(GLFW.GLFW_KEY_SPACE)) camInc.y = 1
		else camInc.y = 0

		if (mouseInput.btnPressed[GLFW.GLFW_MOUSE_BUTTON_1]) {
			camRot = mouseInput.displVec
			println "Cam pos: ${camera.position}"
		}
		else
			camRot.x = camRot.y = 0
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

		float amt = delta
		rect.rotation.add(amt, 0, 0)

		camera.movePosition camInc.mul(delta)

		camera.moveRotation( (float) (camRot.x * delta), (float) (camRot.y *delta), 0 )

	}

	int restrict(number) {
		restrict(number, 0, 255)
	}

	int restrict(number, min, max) {
		number >= max ? max : (number <= min) ? min : number
	}

	@Override
	void renderSelf(Window window) {
		window.clearColor = new Color(red, green, blue)
		renderer.render(window, camera, [rect], ambientLight, pointLight)
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
