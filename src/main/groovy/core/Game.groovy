package core
/**
 *
 * Central manager of the game
 *
 * Created 12/05/16
 * @author Timothy Earley
 */
class Game implements Runnable {


	Window window

	GamePart root

	private running = false
	private Thread thread

	Game() {
		addShutdownHook { stop() }
		window = new GLWindow()
	}

	void start() {
		if (running) return
		running = true
		thread = new Thread(this, "Game Thread").start()
	}

	void stop() {
		running = false
		thread?.join(10000) // 10s max stop time
	}

	void run() {

		window.init()


		try {
			while (running) {
				//TODO implement main loop
				update()
				render()

				if (window.shouldClose()) running = false
			}
			root.stop()
			window.stop()
		} finally {
			window.terminate()
		}
	}

	def update() {
		window.poll()
		root.update()
	}

	def render() {
		window.nextRender()

		root.render()

	}

}
