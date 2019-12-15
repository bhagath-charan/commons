package in.philomath.commons.api.data.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Searchable
 *
 * @author Bhagath Charan 15-12-2019
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Searchable {

	String value() default "";

	boolean exactMatch() default false;

	boolean caseSensitive() default false;
}
