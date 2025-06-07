package kr.ac.hansung.cse.hellospringdatajpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // ROLE_USER, ROLE_ADMIN

    // 기본 생성자
    public Role() {
    }

    // 매개변수 생성자
    public Role(String name) {
        this.name = name;
    }

    // Getter 메서드들
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}