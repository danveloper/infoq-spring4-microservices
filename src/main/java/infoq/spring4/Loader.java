package infoq.spring4;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Loader {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        Context context = tomcat.addContext("/", new File(".").getAbsolutePath());
        context.addLifecycleListener(new ContextConfig());
        tomcat.start();
        tomcat.getServer().await();
    }
}
