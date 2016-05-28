package core

import groovy.util.logging.Log4j
import org.apache.log4j.*

/**
 *
 * Central manager of the game
 *
 * Created 12/05/16
 * @author Timothy Earley
 */
class Game implements Runnable {


	static isOSX = System.getProperty('os.name').contains('Mac')

	/* PUBLIC */

	GamePart root

	/**
	 * Updates per second
	 * Applied on start
	 */
	float ups = 60.0

	Window window

	MouseInput mouseInput

	/* PRIVATE */

	private running = false
	private Thread thread

	Game() {
		addShutdownHook { stop() }
		window = new GLWindow()
		mouseInput = new MouseInput()
	}

	void start() {
		if (running) return
		running = true
		thread = new Thread(this, "Game Thread")
		// for OSX the GLFW has to be on the main thread
		if (isOSX) thread.run()
		// otherwise run on the thread
		else thread.start()
	}

	void stop() {
		running = false
		thread?.join(10000) // 10s max stop time
	}

	void run() {
		try {
			init()
			gameLoop()
			root.stop()
			window.stop()
		} catch (Exception e) {
			e.printStackTrace() //TODO log exception / handle it
		} finally {
			window.terminate()
		}
	}

	private init() {

		window.init()
		mouseInput.init(window)
		root.init(window)
	}

	private gameLoop() {
		def current = System.nanoTime()
		def previous = current
		def updatesDue = 0.0
		def updatesPerNS = ups / 1E9
		float sPerUpdate = 1 / ups

		def maxUPS = ups * 2 // tolerate 10x lag

		// Debug
		long timer = System.currentTimeMillis()
		def updates = 0
		def frames = 0

		while (running) {

			current = System.nanoTime() // Grab the current time

			updatesDue += (current - previous) * updatesPerNS // calculate delta time since last round

			previous = current // reset round

			// Check input
			input()

			while (updatesDue > 1.0 && updates < maxUPS) { // while updates queued and not exceeding maximum
				update(sPerUpdate)
				updates++
				updatesDue--
			}

			render()
			frames++

			sync(current) //TODO recheck if sync actually works

			// print the UPS and FPS every second
			if (System.currentTimeMillis() - timer > 1000) {
				def s = (System.currentTimeMillis() - timer) / 1000.0
				timer += 1000
				println "$updates ups, $frames fps (1s = $s)" //TODO add debug flag to toggle output
				updates = frames = 0
			}


			if (window.shouldClose()) running = false
		}

	}

	private sync(current) {

		float loopSlot = 1E9/ups // desired time for one game loop

		long end = current + loopSlot

		while (System.nanoTime() < end) {
			try { Thread.sleep(1) } catch (Exception ignored) {}
		}

	}

	private input() {
		window.poll()
		mouseInput.input window
		root.input window, mouseInput
	}

	private update(float delta) {
		root.update(delta)
	}

	private render() {
		root.render(window)
		window.nextRender()
	}

}
