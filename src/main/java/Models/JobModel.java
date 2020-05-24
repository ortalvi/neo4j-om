package Models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class JobModel {
    @Getter @Setter
    String name;
    @Getter @Setter
    Set<String> dependencies;

    public JobModel() {
        name = "";
        dependencies = new HashSet<>();
    }

    public JobModel(String name, Set<String> dependencies ){
        this.name = name;
        this.dependencies = dependencies;
    }
}
