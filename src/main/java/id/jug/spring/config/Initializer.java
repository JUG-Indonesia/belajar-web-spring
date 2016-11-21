package id.jug.spring.config;

import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * Created by galih.lasahido@gmail.com
 */
public class Initializer extends AbstractHttpSessionApplicationInitializer { 

    public Initializer() {
    	super(RedisSecurityConfig.class); 
    }

}
