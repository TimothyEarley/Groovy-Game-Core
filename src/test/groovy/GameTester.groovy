import core.Game
import core.GamePart

import java.awt.*
/**
 * Created 19/05/16
 * @author Timothy Earley
 */

Game game

def root = new GamePart() {
	@Override
	void renderSelf() {
		println game.window.width + "; " + game.window.height
	}
}

game = new Game(root: root)

game.window.with {
	height = 480
	width = 800
	title = "Hello world"
	bgColor = new Color( 0x05eec0 )
}

game.start()
