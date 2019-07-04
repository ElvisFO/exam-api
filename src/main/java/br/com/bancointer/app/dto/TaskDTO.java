package br.com.bancointer.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Elvis Fernandes on 26/06/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Integer weight;
    @NotNull
    private Boolean completed;
    private LocalDate createdAt = LocalDate.now();
}
