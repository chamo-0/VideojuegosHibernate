package org.example;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="platforms")
public class platforms {

    @Id
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String nombre;

    @ManyToMany(mappedBy="platforms", cascade = CascadeType.REMOVE)
    private List<games> games;

    public List<games> getGames() {
        return games;
    }

    public void setGames(List<games> games) {
        this.games = games;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "platforms{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", games=" + games +
                '}';
    }
}
