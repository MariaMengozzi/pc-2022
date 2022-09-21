package smart_room;

public interface PresenceDetectionDevice extends EventSource {
	/**
	 *
	 * @return true or false occording if someone is detected or not inside the room
	 */
	boolean presenceDetected();

}
