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

	/**
	 * Updates per second
	 * Applied on start
	 */
	float ups = 60.0

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
		try {

			window.init()
			root.init()

			def now = System.nanoTime()
			def time = now
			def updatesDue = 0.0
			def updatesPerNS = ups / 1E9

			def maxUPS = ups * 2 // tolerate 10x lag

			boolean updated = false

			// Debug
			long timer = System.currentTimeMillis()
			def updates = 0
			def frames = 0

			while (running) {

				now = System.nanoTime() // Grab the current time

				updatesDue += (now - time) * updatesPerNS // calculate delta time since last round

				time = now // reset round

				updated = false
				while (updatesDue > 1.0 && updates < maxUPS) { // while updates queued and not exceeding maximum
					update()
					updates++
					updatesDue--
					updated = true
				}

				if (updated) {
					render()
					frames++
				}

				// print the UPS and FPS every second
				if (System.currentTimeMillis() - timer > 1000) {
					def s = (System.currentTimeMillis() - timer) / 1000.0
					timer += 1000
					println "$updates ups, $frames fps (1s = $s)" //TODO add debug flag to toggle output
					updates = frames = 0
				}


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
