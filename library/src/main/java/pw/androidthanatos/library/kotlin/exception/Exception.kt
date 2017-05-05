package pw.androidthanatos.library.kotlin.exception

/**
 * Created  on 2017/5/2.
 * @author thanatos
 */

/**
 * activity别名为空抛出的异常
 */
class RouterBootAliasNameIsEmptyException(message:String) : IllegalArgumentException(message)

class RouterBootBaseUrlException(message: String) : IllegalArgumentException(message)

class RouterBootMethodException(message: String) : RuntimeException(message)

class RouterBootMethodArgumentSizeException(message: String) : IllegalArgumentException(message)

class RouterBootMethodArgumentException(message: String) : IllegalArgumentException(message)

class RouterBootRequestMappingArgumentException(message: String) : IllegalArgumentException(message)

class RouterBootInitException(message: String) : RuntimeException(message)

class RouterBootReceiverMethodSizeException(message: String) : RuntimeException(message)

class RouterBootReceiverMethodParemeterException(message: String) : RuntimeException(message)

class RouterBootRequestURLException(message: String) : IllegalArgumentException(message)