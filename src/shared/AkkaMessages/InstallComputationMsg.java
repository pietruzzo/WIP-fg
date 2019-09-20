package shared.AkkaMessages;

import shared.computation.Computation;

import java.io.Serializable;

public class InstallComputationMsg implements Serializable {
    private static final long serialVersionUID = 200013L;

    private final Computation computation;
    private final String identifier;



    public InstallComputationMsg(Computation computation, String identifier) {
        this.computation = computation;
        this.identifier = identifier;
    }

    public Computation getComputation() {
        return computation;
    }

    public String getIdentifier() {
        return identifier;
    }
}