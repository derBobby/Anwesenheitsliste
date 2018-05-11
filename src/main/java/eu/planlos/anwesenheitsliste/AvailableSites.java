package eu.planlos.anwesenheitsliste;

public enum AvailableSites {

	USERLIST("/userlist"),
	USERDETAIL("/userdetail"),

	GROUPLIST("/grouplist"),
	GROUPDETAIL("/groupdetail")
	
	;
	
	private String path;
	
	private AvailableSites(String path) {
		this.setPath(path);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
