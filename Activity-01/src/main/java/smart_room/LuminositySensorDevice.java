package smart_room;

public interface LuminositySensorDevice extends EventSource {

	double getLuminosity(); // value between 0 and 1 as double
		
}
