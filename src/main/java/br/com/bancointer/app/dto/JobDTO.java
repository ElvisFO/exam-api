package br.com.bancointer.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Elvis Fernandes on 26/06/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"tasks"})
public class JobDTO extends BaseJobDTO {

    private BaseJobDTO parentJob;

    @Valid
    private List<TaskDTO> tasks;
}
