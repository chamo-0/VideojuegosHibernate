package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="genres")
public class genres {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @ManyToMany(mappedBy="genres")
    private List<games> games;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "genres{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
