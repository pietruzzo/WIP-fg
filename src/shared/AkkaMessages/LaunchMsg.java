package shared.AkkaMessages;

import java.io.Serializable;

public class LaunchMsg implements Serializable {
    private static final long serialVersionUID = 200006L;

    @Override
    public String toString(){
        return "Instance Launching";
    }

}
