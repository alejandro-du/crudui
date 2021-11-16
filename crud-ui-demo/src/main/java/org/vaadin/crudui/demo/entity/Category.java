package org.vaadin.crudui.demo.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Boniface Chacha
 * @email boniface.chacha@niafikra.com
 * @email bonifacechacha@gmail.com
 * @date 8/30/17 3:16 PM
 */
@Entity
@Table(name = "category")
public class Category implements Serializable{

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Integer version;

    @NotBlank
    @NotNull
    @Column(unique = true)
    private String name;

    @Lob
    private String description;

    @ManyToOne
    private Category parent;

    public Category() {
    }

    public Category(String name, String description, Category parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
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
