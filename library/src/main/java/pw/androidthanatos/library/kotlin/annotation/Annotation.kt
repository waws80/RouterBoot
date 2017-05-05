package pw.androidthanatos.library.kotlin.annotation

import org.jetbrains.annotations.NotNull
import pw.androidthanatos.library.kotlin.request.RequestMethod

/**
 * RouterBoot框架的注解
 */


/**
 * activity类上的注解，有此注解说明遵循RouterBoot框架的统一管理
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Alias(@NotNull val value:String)



@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestMapping(@NotNull val value:String,  val method: RequestMethod = RequestMethod.GET)



@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Controller


@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Receiver

