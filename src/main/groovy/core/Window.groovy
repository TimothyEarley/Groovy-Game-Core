package core

/**
 * Created 19/05/16
 * @author Timothy Earley
 */
trait Window {

	int width = 400, height = 400

	abstract init()

	abstract boolean shouldClose()

	abstract stop()

	abstract terminate()

	abstract poll()

	abstract nextRender()

}