package component.configuration;

import cucumber.api.java.Before;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ComponentHelper {

    @Getter
    private static Map<String, Object> map = new HashMap<String, Object>();

    @Before()
    public void cleanDb(){
        map.clear();
    }
}
