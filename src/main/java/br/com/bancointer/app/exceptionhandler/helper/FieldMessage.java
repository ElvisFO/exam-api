package br.com.bancointer.app.exceptionhandler.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Elvis Fernandes on 26/06/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fieldName;
    private String message;
}
