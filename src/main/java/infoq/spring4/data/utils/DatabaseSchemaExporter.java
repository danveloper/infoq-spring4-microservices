package infoq.spring4.data.utils;

import infoq.spring4.data.Phone;
import infoq.spring4.data.User;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class DatabaseSchemaExporter {
    public static void main(String[] args) {
        Configuration cfg = new Configuration().addAnnotatedClass(Phone.class).addAnnotatedClass(User.class);
        cfg.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        SchemaExport schemaExport = new SchemaExport(cfg);
        schemaExport.execute(true, false, false, false);
    }
}
