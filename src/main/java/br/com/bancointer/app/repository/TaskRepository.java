package br.com.bancointer.app.repository;

import br.com.bancointer.app.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCreatedAt(LocalDate createdAt);
}
