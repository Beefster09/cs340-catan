package server.telnet;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	String[] args() default {};
	String info();
	String description() default "?";

}
