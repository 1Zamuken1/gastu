package gastu.gastu.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un usuario
 */
public class UsuarioNotFoundException extends BusinessException {

    public UsuarioNotFoundException(String correo) {
        super("Usuario no encontrado con correo: " + correo);
    }

    public UsuarioNotFoundException(Long usuarioId) {
        super("Usuario no encontrado con ID: " + usuarioId);
    }
}