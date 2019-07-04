package br.com.bancointer.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Elvis Fernandes on 26/06/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseJobDTO {

    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    private Boolean active;
}
