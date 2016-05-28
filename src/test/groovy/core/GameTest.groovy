package core

import spock.lang.Ignore
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

	def "all methods called on root"() {
		given: "a game and root"
		boolean updated, rendered, stopped, init, input
		// TODO mock GamePart and use Spock to test method calls?
		def root = new GamePart() {
			@Override
			void inputSelf(Window window, MouseInput mouseInput) { input = true }
			@Override
			void initSelf(Window window) { init = true}
			@Override
			void updateSelf(float delta) {	updated = true }
			@Override
			void renderSelf(Window window) { rendered = true }
			@Override
			void stopSelf() { stopped = true }
		}
		def game = new Game( root: root, window: windowStub )

		when: "the game is started"
		game.start()
		Thread.sleep 1000 // since this is working in a different thread, wait a bit
		game.stop()
		Thread.sleep 100

		then: "the methods were called"
		updated && rendered && stopped && init  && input
	}

}
