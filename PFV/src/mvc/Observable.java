package mvc;
import java.util.ArrayList;

public class Observable {
	protected ArrayList<Observer> observers = new ArrayList<Observer>();
	public void attach(Observer o) {
		observers.add(o);
	}
	public void detach(Observer o) {
		observers.remove(o);
	}
	public void notifyObservers() {
		//System.out.println("Observers notified");
		for(Observer o: observers) {
			o.update(this);
		}
	}
	
	public void notifyObservers(Node node) {
		//System.out.println("Observers notified");
		for(Observer o: observers) {
			o.update(node);
		}
	}
	
}

