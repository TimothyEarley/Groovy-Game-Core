package core
/**
 *
 * Part of a game
 *
 * Created 12/05/16
 * @author Timothy Earley
 */
class GamePart {

	def active = true

	List<GamePart> children = []

	final void input(Window window) {
		if (!active) return
		inputSelf window
		children.each { it.input window }
	}

	final void update(float delta) {
		if (!active) return
		updateSelf(delta)
		children.each { it.update(delta) }
	}

	final void render(Window window) {
		if (!active) return
		renderSelf(window)
		children.each { it.render(window) }
	}

	final void stop() {
		stopSelf()
		children.each { it.stop() }
	}

	final void init(Window window) {
		initSelf(window)
		children.each { it.init(window) }
	}


	//TODO decide if every GamePart gets the full window or only parts
	// pro full: better control for different aspect ratios
	// pro parts: decoupling from actual position (multiple instances at different locations)

	void updateSelf(float  delta) {}
	void inputSelf(Window window) {}
	void renderSelf(Window window) {}
	void initSelf(Window window) {}
	void stopSelf() {}
}
