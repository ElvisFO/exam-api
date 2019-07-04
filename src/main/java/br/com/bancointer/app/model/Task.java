package br.com.bancointer.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@Entity
@Table(name = "task")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"job"})
@ToString(exclude = {"job"})
public class Task extends AbstractEntity<Long> {

    @Column(nullable = false)
    private String name;

    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "completed")
    private Boolean completed;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name= "created_at")
    private LocalDate createdAt = LocalDate.now();

    @JsonIgnore
    @JoinColumn(name = "job_id")
    @ManyToOne
    private Job job;
}
