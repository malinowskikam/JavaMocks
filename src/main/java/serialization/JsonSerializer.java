package serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonSerializer
{
	private static ObjectMapper om;
	
	static {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JodaModule());
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        om.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
	
    public Object getDatabase(String source,Class databaseClass)
    {
        try {
            return om.readValue(new File(source),databaseClass);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean writeDatabase(String target, Object database)
    {
        try {
            File file = new File(target);
            om.writeValue(file,database);
            return true;
        } catch ( Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}