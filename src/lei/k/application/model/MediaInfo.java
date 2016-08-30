package lei.k.application.model;

import javafx.beans.property.StringProperty;

public class MediaInfo {

	private Integer id;
	private String name;
	private StringProperty URI;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StringProperty getURI() {
		return URI;
	}

	public void setURI(StringProperty URI) {
		this.URI = URI;
	}
}
