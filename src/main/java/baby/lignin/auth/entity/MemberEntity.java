package baby.lignin.auth.entity;


import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class MemberEntity {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String profile;

    @Column
    private String email;

    @Column(nullable = false)
    private Long certificationId;

    @Column(nullable = false)
    private boolean deleted;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
