package br.com.bancointer.app.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@Entity
@Table(name = "job")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"tasks"})
public class Job extends AbstractEntity<Long> {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "parent_job_id")
    private Job parentJob;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    private List<Task> tasks;

    public int getTotalWeight() {
        if (tasks == null || tasks.isEmpty()) {
            return 0;
        }
        return tasks.stream().collect(Collectors.summingInt(Task::getWeight));
    }
}
