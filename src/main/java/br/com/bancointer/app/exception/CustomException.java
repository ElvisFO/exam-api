package br.com.bancointer.app.exception;

/**
 * @author Elvis Fernandes on 26/06/19
 */
public class CustomException extends RuntimeException {

    private Object[] params;

    public CustomException(String msgCode, Object[] params) {
        super(msgCode);
        this.params = params;
    }

    public CustomException(String msgCode) {
        super(msgCode);
        this.params = new Object[]{};
    }

    public Object[] getParams() {
        return params;
    }
}
