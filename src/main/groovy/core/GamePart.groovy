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

	final void input(Window window, MouseInput mouseInput) {
		if (!active) return
		inputSelf window, mouseInput
		children.each { it.input window, mouseInput }
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
	void inputSelf(Window window, MouseInput mouseInput) {}
	void renderSelf(Window window) {}
	void initSelf(Window window) {}
	void stopSelf() {}
}
