package br.com.bancointer.app.repository;

import br.com.bancointer.app.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Elvis Fernandes on 25/06/19
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findOneByName(String name);

    Optional<Job> findByParentJobIsNull();
}
