package org.vaadin.crudui.demo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

/**
 * @author Boniface Chacha
 */
@Entity
@Table(name = "technology")
public class Technology implements Serializable {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	@NotNull
	@Column(unique = true)
	private String name;

	@Positive
	private Double version;

	@Lob
	private String description;

	@Past
	private LocalDateTime lastPatchedAt;

	@ManyToOne
	private Technology parent;

	public Technology() {
	}

	public Technology(String name, Double version, String description, LocalDateTime lastPatchedAt, Technology parent) {
		this.name = name;
		this.version = version;
		this.description = description;
		this.lastPatchedAt = lastPatchedAt;
		this.parent = parent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getLastPatchedAt() {
		return lastPatchedAt;
	}

	public void setLastPatchedAt(LocalDateTime lastPatchedAt) {
		this.lastPatchedAt = lastPatchedAt;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Technology getParent() {
		return parent;
	}

	public void setParent(Technology parent) {
		this.parent = parent;
	}

	public String toString() {
		return name;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public boolean isRoot() {
		return !hasParent();
	}

}
