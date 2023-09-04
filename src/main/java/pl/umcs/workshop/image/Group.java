package pl.umcs.workshop.image;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    // Relations
    @ManyToMany
    @JoinColumn(name = "image_id")
    private Set<Image> images;
}
