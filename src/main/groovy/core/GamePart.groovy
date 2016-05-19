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

	final void update() {
		if (!active) return
		updateSelf()
		children.each { it.update() }
	}

	final void render() {
		if (!active) return
		renderSelf()
		children.each { it.render() }
	}

	final void stop() {
		stopSelf()
		children.each { it.stop() }
	}


	void updateSelf() {}
	//TODO decide if every GamePart gets the full window or only parts
	// pro full: better control for different aspect ratios
	// pro parts: decoupling from actual position (multiple instances at different locations)
	void renderSelf() {}
	void stopSelf() {}

}
