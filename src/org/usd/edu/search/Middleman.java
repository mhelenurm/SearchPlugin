package org.usd.edu.search;

public class Middleman {
	private Expression _expression;
	private boolean _isReady;
	
	public synchronized Expression get() {
		while(!_isReady) {
			try {
				wait();
			} catch(InterruptedException e) { }
		}
		_isReady = false;
		
		notifyAll();
		return _expression;
	}
	
	public synchronized void put(Expression exp) {
		while(_isReady) {
			try {
				wait();
			} catch(InterruptedException e) { }
		}
		_isReady = true;
		_expression = exp;
		notifyAll();
	}
}