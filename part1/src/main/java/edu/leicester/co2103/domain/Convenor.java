package edu.leicester.co2103.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
public class Convenor {

	@Id
	@GeneratedValue
	private long id;

	private String name;
	@Enumerated(EnumType.STRING)
	private Position position;

	@ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	@JoinColumn
	private List<Module> modules;

	public Convenor(String name, Position position) {
		this(name, position, new ArrayList<>());
	}

	public Convenor(String name, Position position, List<Module> modules) {
		super();
		this.name = name;
		this.position = position;
		this.modules = modules;
	}

	public Convenor() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

}
