package core

import spock.lang.Specification
/**
 * Created 12/05/16
 * @author Timothy Earley
 */
class GameTest extends Specification {

	def windowStub = Mock(Window)

	def "run and stop a game"() {

		given: "a game"
		def game = new Game( root: new GamePart(), window: windowStub )

		when: "the game is started"
		game.start()

		then: "it can be stopped"
		game.stop()

	}

	def "render and update called on root"() {
		given: "a game and root"
		boolean updated, rendered, stopped
		def root = new GamePart() {
			void updateSelf() {	updated = true }
			void renderSelf() { rendered = true }
			void stopSelf() { stopped = true }
		}
		def game = new Game( root: root, window: windowStub )

		when: "the game is started"
		game.start()
		Thread.sleep 1000 // since this is working in a different thread, wait a bit
		game.stop()
		Thread.sleep 100

		then: "update, render stop were called"
		updated && rendered && stopped
	}

}
