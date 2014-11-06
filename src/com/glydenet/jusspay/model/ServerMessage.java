package com.glydenet.jusspay.model;

public class ServerMessage {
	
	private boolean error = false;
	private String message;
	
	public ServerMessage(boolean error, String message){
		this.error = error;
		this.message = message;
	}

	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
