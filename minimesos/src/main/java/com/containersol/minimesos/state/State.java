package com.containersol.minimesos.state;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is populated with the results from a GET request to /state.json on a mesos-master.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class State {

    private String id;
    private Map<String, String> flags = new HashMap<>();

    @JsonProperty("activated_slaves")
    private int activatedAgents = 0; // NOSONAR

    @JsonProperty("version")
    private String version;

    private ArrayList<Framework> frameworks = new ArrayList<>();

    public static State fromJSON(String jsonString) throws JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, State.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Framework> getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(ArrayList<Framework> frameworks) {
        this.frameworks = frameworks;
    }

    public Framework getFramework(String name) {
        for (Framework fw : getFrameworks()) {
            if (fw.getName().equals(name)) return fw;
        }
        return null;
    }

    public Map<String, String> getFlags() {
        return flags;
    }

    public int getActivatedAgents() {
        return activatedAgents;
    }

    public String getVersion() {
        return version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
